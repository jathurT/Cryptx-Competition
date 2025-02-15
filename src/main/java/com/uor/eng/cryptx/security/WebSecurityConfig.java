package com.uor.eng.cryptx.security;

import com.uor.eng.cryptx.model.AppRole;
import com.uor.eng.cryptx.model.Role;
import com.uor.eng.cryptx.model.User;
import com.uor.eng.cryptx.repository.RoleRepository;
import com.uor.eng.cryptx.repository.UserRepository;
import com.uor.eng.cryptx.security.jwt.AuthEntryPointJwt;
import com.uor.eng.cryptx.security.jwt.AuthTokenFilter;
import com.uor.eng.cryptx.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Public endpoints:
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/api/test/**").permitAll()
            .requestMatchers("/images/**").permitAll()
            // Add more patterns if needed:
            .requestMatchers("/api/bookings/create").permitAll()
            .requestMatchers("/api/bookings/{referenceId}/{contactNumber}").permitAll()
            .requestMatchers("/api/schedules/{id}").permitAll()
            .requestMatchers("/api/schedules/getSeven").permitAll()
            .requestMatchers("/api/feedback/submit").permitAll()
            .requestMatchers("/api/contacts/submit").permitAll()
            // Everything else:
            .anyRequest().authenticated()
        );

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers(
        "/v2/api-docs",
        "/configuration/ui",
        "/swagger-resources/**",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**"
    );
  }

  /**
   * Creates default roles if they don't exist, then creates a few default users.
   * This ensures we never try to persist an already-existing Role,
   * preventing "detached entity passed to persist" errors.
   */
  @Bean
  public CommandLineRunner initData(
      RoleRepository roleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder
  ) {
    return args -> {
      // 1) Find or create roles:
      Role userRole = roleRepository.findByRoleName(AppRole.BUYER)
          .orElseGet(() -> roleRepository.save(new Role(AppRole.BUYER)));

      Role adminRole = roleRepository.findByRoleName(AppRole.ADMIN)
          .orElseGet(() -> roleRepository.save(new Role(AppRole.ADMIN)));

      Role farmerOwnerRole = roleRepository.findByRoleName(AppRole.FARMER_OWNER)
          .orElseGet(() -> roleRepository.save(new Role(AppRole.FARMER_OWNER)));

      Role farmerEmployeeRole = roleRepository.findByRoleName(AppRole.FARMER_EMPLOYEE)
          .orElseGet(() -> roleRepository.save(new Role(AppRole.FARMER_EMPLOYEE)));

      Role formerFarmerRole = roleRepository.findByRoleName(AppRole.FORMER_FARMER)
          .orElseGet(() -> roleRepository.save(new Role(AppRole.FORMER_FARMER)));

      Set<Role> adminRoles = Set.of(userRole, adminRole);

      // 2) Create default users if needed:
      if (!userRepository.existsByUserName("admin")) {
        User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
        admin.setRoles(adminRoles);
        userRepository.save(admin);
      }

      if (!userRepository.existsByUserName("buyer")) {
        User buyer = new User("buyer", "buyer@gmail.com", passwordEncoder.encode("buyerPass"));
        buyer.setRoles(Set.of(userRole));
        userRepository.save(buyer);
      }

      if (!userRepository.existsByUserName("farmerOwner")) {
        User farmerOwner = new User("farmerOwner", "farmer@gmail.com", passwordEncoder.encode("farmerOwnerPass"));
        farmerOwner.setRoles(Set.of(farmerOwnerRole));
        userRepository.save(farmerOwner);
      }

      if (!userRepository.existsByUserName("farmerEmployee")) {
        User farmerEmployee = new User("farmerEmployee", "farmerEmployee@gmail.com", passwordEncoder.encode("farmerEmployeePass"));
        farmerEmployee.setRoles(Set.of(farmerEmployeeRole));
        userRepository.save(farmerEmployee);
      }

      if (!userRepository.existsByUserName("formerFarmer")) {
        User formerFarmer = new User("formerFarmer", "formerFarmer@gmail.com", passwordEncoder.encode("formerFarmerPass"));
        formerFarmer.setRoles(Set.of(formerFarmerRole));
        userRepository.save(formerFarmer);
      }
    };
  }
}
