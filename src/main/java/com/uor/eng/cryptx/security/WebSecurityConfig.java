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
//@EnableMethodSecurity
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
            .authorizeHttpRequests(auth ->
                            auth.requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/v3/api-docs/**").permitAll()
                                    .requestMatchers("/swagger-ui/**").permitAll()
                                    .requestMatchers("/api/test/**").permitAll()
                                    .requestMatchers("/images/**").permitAll()
                                    .requestMatchers("/api/bookings/create").permitAll()
                                    .requestMatchers("/api/bookings/{referenceId}/{contactNumber}").permitAll()
                                    .requestMatchers("/api/schedules/{id}").permitAll()
                                    .requestMatchers("/api/schedules/getSeven").permitAll()
                                    .requestMatchers("/api/feedback/submit").permitAll()
                                    .requestMatchers("/api/contacts/submit").permitAll()
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                    .anyRequest().authenticated()
            );

    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    http.headers(headers -> headers.frameOptions(
            frameOptions -> frameOptions.sameOrigin()));

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
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


  @Bean
  public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      // Retrieve or create roles
      Role userRole = roleRepository.findByRoleName(AppRole.USER)
              .orElseGet(() -> {
                Role newUserRole = new Role(AppRole.USER);
                return roleRepository.save(newUserRole);
              });

      Role adminRole = roleRepository.findByRoleName(AppRole.ADMIN)
              .orElseGet(() -> {
                Role newAdminRole = new Role(AppRole.ADMIN);
                return roleRepository.save(newAdminRole);
              });

      Set<Role> userRoles = Set.of(userRole);
      Set<Role> adminRoles = Set.of(userRole, adminRole);


      if (!userRepository.existsByUserName("admin")) {
        User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
        userRepository.save(admin);
      }

      if (!userRepository.existsByUserName("user")) {
        User user = new User("user", "user@gmail.com", passwordEncoder.encode("userPass"));
        userRepository.save(user);
      }
      // Update roles for existing users
      userRepository.findByUserName("user").ifPresent(currentUser -> {
        currentUser.setRoles(userRoles);
        userRepository.save(currentUser);
      });

      userRepository.findByUserName("admin").ifPresent(admin -> {
        admin.setRoles(adminRoles);
        userRepository.save(admin);
      });
    };
  }
}

