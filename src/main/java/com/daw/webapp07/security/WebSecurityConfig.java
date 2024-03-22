package com.daw.webapp07.security;


import com.daw.webapp07.security.jwt.JwtRequestFilter;
import com.daw.webapp07.security.jwt.UnauthorizedHandlerJwt;


import com.daw.webapp07.service.RepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;



    @Autowired
    public RepositoryUserDetailsService userDetailService;




    //we need to encode the password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //we need to create an authentication provider and insert the userDetailService with the users and their passwords
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username(admin)
                .password(adminpass)
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
    **/


    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .securityMatcher("/api/**")
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PRIVATE ENDPOINTS
                        .requestMatchers(HttpMethod.POST,"/api/projects/").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.PUT,"/api/projects/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/projects/**").hasAnyRole("USER")

                        .requestMatchers(HttpMethod.POST,"/api/comments/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.PUT,"/api/comments/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/comments/**").hasAnyRole("USER")
                        // PUBLIC ENDPOINTS
                        .anyRequest().permitAll()
                );

        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT Token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());
        http
                .authorizeHttpRequests(authorize -> authorize

                        // STATIC RESOURCES
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/assets2/**", "/scss/**", "/vendor/**", "/video/**").permitAll()

                        // PUBLIC PAGES
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/project-details/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/projects/**").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/projects").permitAll()
                        .requestMatchers("/error-page").permitAll()
                        .requestMatchers("/error").permitAll()



                        // PRIVATE PAGES
                        .requestMatchers("/editproject/*").hasAnyRole("USER")
                        .requestMatchers("/createProject").hasAnyRole("USER")
                        .requestMatchers("/myProjects").hasAnyRole("USER")
                        .requestMatchers("/newProject").hasAnyRole("USER")
                        .requestMatchers("/rec-projects").hasAnyRole("USER")
                        .requestMatchers("/editProject/**").hasAnyRole("USER")
                        .requestMatchers("/editProfile").hasAnyRole("USER")
                        .requestMatchers("/project-details/*/comment").hasAnyRole("USER")
                        .requestMatchers("/project-details/*/donate").hasAnyRole("USER")
                        .requestMatchers("/project-details/*/delete").hasAnyRole("USER")
                        .requestMatchers("/ranking").hasAnyRole("USER")
                        .requestMatchers("/comment/**").hasAnyRole("USER")

                        .anyRequest().anonymous()

                )
                // LOGIN AND LOGOUT
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );


        return http.build();
    }



}
