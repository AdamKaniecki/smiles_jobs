//package pl.zajavka.infrastructure.security;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.context.annotation.Configuration;
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
//@Bean
//    public PasswordEncoder passwordEncoder(){
//    return new BCryptPasswordEncoder();
//}
//
//@Bean
//    AuthenticationManager authenticationManager(
//            HttpSecurity http,
//            PasswordEncoder passwordEncoder,
//            UserDetailsService userDetailsService) throws Exception {
//    return http.getSharedObject(AuthenticationManagerBuilder.class)
//            .userDetailsService(userDetailsService)
//            .passwordEncoder(passwordEncoder)
//            .and()
//            .build();
//}
////konfiguracja seurity kiedy bedzie wlaczone
//    @Bean
//    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
//    SecurityFilterChain securityEnabled (HttpSecurity http) throws Exception {
//    http.authorizeHttpRequests()
//            .requestMatchers("/login", "/error", "/obraz.png").permitAll()
//            .requestMatchers("/widok1/**").hasAnyAuthority("ENCJA")
//            .requestMatchers("/widok2/**","/widok3/**","/widok4/**").hasAnyAuthority("ENCJA2")
//            .requestMatchers("/", "widok5").hasAnyAuthority("ENCJA1", "ENCJA2")
//            .and()
//            .formLogin()
//            .permitAll()
//            .and()
//            .logout()
//            .logoutSuccessUrl("/login")
//            .invalidateHttpSession(true)
//            .deleteCookies("JSESSIONIO")
//            .permitAll();
//    return http.build();
//    }
////    wylaczone security
//
//    @Bean
//    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "false")
//    SecurityFilterChain securityDisabled (HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .anyRequest()
//                .permitAll();
//        return http.build();
//    }
//}
