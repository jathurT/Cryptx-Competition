package com.uor.eng.cryptx.controller;

import com.uor.eng.cryptx.model.AppRole;
import com.uor.eng.cryptx.model.Role;
import com.uor.eng.cryptx.model.User;
import com.uor.eng.cryptx.payload.auth.ForgotPasswordRequest;
import com.uor.eng.cryptx.payload.auth.ResetPasswordRequest;
import com.uor.eng.cryptx.repository.RoleRepository;
import com.uor.eng.cryptx.repository.UserRepository;
import com.uor.eng.cryptx.security.jwt.JwtUtils;
import com.uor.eng.cryptx.security.request.LoginRequest;
import com.uor.eng.cryptx.security.request.SignupRequest;
import com.uor.eng.cryptx.security.response.MessageResponse;
import com.uor.eng.cryptx.security.response.UserInfoResponse;
import com.uor.eng.cryptx.security.services.UserDetailsImpl;
import com.uor.eng.cryptx.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private PasswordResetService passwordResetService;

  /**
   * Logs in the user and returns a JWT cookie plus user info.
   */
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication;
    try {
      authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUserNameOrEmail(),
              loginRequest.getPassword()
          )
      );
    } catch (AuthenticationException ex) {
      Map<String, Object> map = new HashMap<>();
      map.put("message", "Bad credentials");
      map.put("status", false);
      return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // Generate JWT as a cookie:
    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    // Build a simple response with user info & the cookie
    UserInfoResponse response = new UserInfoResponse(
        userDetails.getId(),
        userDetails.getUsername(),
        roles,
        jwtCookie.toString()
    );

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(response);
  }

  /**
   * Registers a new user.
   * Uses existing roles from DB, which are guaranteed by the CommandLineRunner in WebSecurityConfig.
   */
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    // Check username
    if (userRepository.existsByUserName(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    // Check email
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create a new user
    User user = new User(
        signUpRequest.getUsername(),
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword())
    );

    // We rely on roles from the DB, created in the CommandLineRunner
    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null || strRoles.isEmpty()) {
      // Default role is BUYER
      Role defaultRole = roleRepository.findByRoleName(AppRole.BUYER)
          .orElseThrow(() -> new RuntimeException("Error: Default role (BUYER) not found."));
      roles.add(defaultRole);
    } else {
      // For each requested role, find it in DB:
      for (String roleStr : strRoles) {
        // Convert e.g. "admin" to AppRole.ADMIN, "buyer" to AppRole.BUYER, etc.
        String lowercase = roleStr.trim().toLowerCase();
        switch (lowercase) {
          case "admin":
            Role adminRole = roleRepository.findByRoleName(AppRole.ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Admin role not found."));
            roles.add(adminRole);
            break;
          case "farmer_owner":
            Role ownerRole = roleRepository.findByRoleName(AppRole.FARMER_OWNER)
                .orElseThrow(() -> new RuntimeException("Error: FarmerOwner role not found."));
            roles.add(ownerRole);
            break;
          case "farmer_employee":
            Role employeeRole = roleRepository.findByRoleName(AppRole.FARMER_EMPLOYEE)
                .orElseThrow(() -> new RuntimeException("Error: FarmerEmployee role not found."));
            roles.add(employeeRole);
            break;
          case "former_farmer":
            Role formerFarmerRole = roleRepository.findByRoleName(AppRole.FORMER_FARMER)
                .orElseThrow(() -> new RuntimeException("Error: FormerFarmer role not found."));
            roles.add(formerFarmerRole);
            break;
          default:
            // if none matched, assume it's buyer
            Role buyerRole = roleRepository.findByRoleName(AppRole.BUYER)
                .orElseThrow(() -> new RuntimeException("Error: Buyer role not found."));
            roles.add(buyerRole);
            break;
        }
      }
    }

    user.setRoles(roles);
    // Save the user normally. Because we removed CascadeType.PERSIST on the roles side,
    // we won't "re-persist" any existing roles.
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  /**
   * Logs out user by clearing JWT cookie
   */
  @PostMapping("/signout")
  public ResponseEntity<?> signoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }

  /**
   * Returns basic user info of the currently authenticated user
   */
  @GetMapping("/user")
  public ResponseEntity<?> getUserDetails(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AuthenticationCredentialsNotFoundException("No user is currently authenticated");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());
    UserInfoResponse response = new UserInfoResponse(
        userDetails.getId(),
        userDetails.getUsername(),
        roles
    );
    return ResponseEntity.ok(response);
  }

  @GetMapping("/username")
  public ResponseEntity<String> currentUserName(Authentication authentication) {
    if (authentication != null) {
      return ResponseEntity.ok(authentication.getName());
    } else {
      return ResponseEntity.ok("anonymousUser");
    }
  }

  /**
   * Send password reset link
   */
  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
    passwordResetService.initiatePasswordReset(forgotPasswordRequest);
    return ResponseEntity.ok(new MessageResponse("Password reset link sent to your email."));
  }

  /**
   * Resets user's password
   */
  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    passwordResetService.resetPassword(request);
    return ResponseEntity.ok(new MessageResponse("Password has been reset successfully."));
  }
}
