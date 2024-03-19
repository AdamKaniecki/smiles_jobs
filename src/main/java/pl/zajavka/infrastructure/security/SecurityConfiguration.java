package pl.zajavka.infrastructure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailService
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    SecurityFilterChain securityEnabled(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/", "/login", "/candidate_registry", "/candidateRegistry/**",
                                                         "/company_registry","/companyRegistry/**" )
                .permitAll()

                .requestMatchers("/sendCV/**","/CvForm/**","/createCV/**", "/updateCvForm/**","/updateCVDone/**",
                         "/deleteCV/**","/ShowMyCV/**","/changeMeetingDate/**","/acceptMeetingDate/**"
                ).hasAuthority("ROLE_CANDIDATE")

//                .requestMatchers("/createJobOffer").hasAuthority("ROLE_COMPANY")
                .requestMatchers("/candidate_portal/**").hasAuthority("ROLE_CANDIDATE")
                .requestMatchers("cv_already_sent","/updateAddressDone/**","/jobOffer/{jobOfferId}", "/businessCard/{businessCardId}"
                       )
                .permitAll()
                .requestMatchers("/company_portal/**").hasAuthority("ROLE_COMPANY")
                .requestMatchers("/arrangeInterview/**","/decline/**","/hired/**").hasAuthority("ROLE_COMPANY")
                .requestMatchers("/showCV/**","/cv/**",
                        "/JobOfferForm/**","/createJobOffer/**","/showMyJobOffers/**", "/updateJobOfferForm/**",
                        "/updateJobOfferDone/**","/deleteJobOffer/**",
                        "/BusinessCardForm/**","/createBusinessCard/**","/showMyBusinessCard/**",
                        "/updateBusinessCardDone/**",  "/updateBusinessCardForm/**","/deleteBusinessCard/**"
                        ).hasAuthority("ROLE_COMPANY")
                .requestMatchers("/api/createCandidate/**","/api/createCompany/**","/api/showCV/**","/api/updateCv/**","/api/deleteCV/**",
                        "/api/createJobOffer/**","/api/showJobOffer/**","/api/showBusinessCard/**").permitAll()
                .requestMatchers("/api/ShowMyCV/**","/api/createCV/**",
               "/api/sendCV/**","/api/changeMeetingDate/**","/api/acceptMeetingDate").hasAuthority("ROLE_CANDIDATE")
                .requestMatchers("api/createJobOffer/**", "/api/ShowMyJobOffers/**","/api/deleteJobOffer/**",
                        "/api/createBusinessCard/**","/api/showMyBusinessCard/**","/api/deleteBusinessCard/**","/api/arrangeInterview/**","/api/decline/**","/api/hired/**").hasAuthority("ROLE_COMPANY")
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();

        return http.build();
    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new CustomAuthenticationSuccessHandler();
//    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "false")
    SecurityFilterChain securityDisabled(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests()
                .anyRequest()
                .permitAll();

        return httpSecurity.build();
    }
}
