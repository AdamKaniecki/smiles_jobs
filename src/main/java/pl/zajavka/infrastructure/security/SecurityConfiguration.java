//package pl.zajavka.infrastructure.security;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authManager(
//        HttpSecurity http,
//        PasswordEncoder passwordEncoder,
//        UserDetailsService userDetailService
//    )
//        throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//            .userDetailsService(userDetailService)
//            .passwordEncoder(passwordEncoder)
//            .and()
//            .build();
//    }
//
//    @Bean
//    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
//    SecurityFilterChain securityEnabled(HttpSecurity http) throws Exception {
//        http.csrf()
//            .disable()
//            .authorizeHttpRequests()
//                .requestMatchers("/").permitAll()
//                .requestMatchers( "/candidate_registry").permitAll()
//                .requestMatchers( "/company_registry").permitAll()
//                .requestMatchers("/candidate_portal/**").hasAuthority("CANDIDATE")
//                .requestMatchers(HttpMethod.POST,"/createAdvertisement").hasAuthority("CANDIDATE")
//
//
////                .requestMatchers("/candidate_portal/**").hasAuthority("CANDIDATE")
//                .requestMatchers("/company_portal/**").hasAuthority("COMPANY")
//                .requestMatchers("/create_advertisement/**").hasAuthority("CANDIDATE")
//                .requestMatchers("/show/{userId}").hasAuthority("CANDIDATE")
//                .requestMatchers("/user_details").hasAuthority("CANDIDATE")
////                .requestMatchers("/{userId}").hasAuthority("CANDIDATE")
//
//            .and()
//
//            .formLogin()
//            .permitAll()
//            .and()
//            .logout()
//            .logoutSuccessUrl("/login")
//            .invalidateHttpSession(true)
//            .deleteCookies("JSESSIONID")
//            .permitAll();
//
//        return http.build();
//    }
//
//    @Bean
//    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "false")
//    SecurityFilterChain securityDisabled(HttpSecurity http) throws Exception {
//        http.csrf()
//                .disable()
//                .authorizeHttpRequests()
//                .anyRequest()
//                .permitAll();
//
//        return http.build();
//    }
//
//}
