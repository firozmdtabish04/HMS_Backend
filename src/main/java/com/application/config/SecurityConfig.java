package com.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.application.filter.JwtFilter;
import com.application.service.UserRegistrationService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRegistrationService registrationService;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(registrationService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/authenticate",
                        "/",
                        "/loginuser",
                        "/logindoctor",
                        "/registeruser",
                        "/registerdoctor",
                        "/addDoctor",
                        "/gettotalusers",
                        "/doctorlist",
                        "/gettotaldoctors",
                        "/gettotalslots",
                        "/acceptstatus/**",
                        "/rejectstatus/**",
                        "/acceptpatient/**",
                        "/rejectpatient/**",
                        "/addBookingSlots",
                        "/doctorlistbyemail/**",
                        "/slotDetails/**",
                        "/slotDetails",
                        "/slotDetailsWithUniqueDoctors",
                        "/slotDetailsWithUniqueSpecializations",
                        "/patientlistbydoctoremail/**",
                        "/addPrescription",
                        "/doctorProfileDetails/**",
                        "/updatedoctor",
                        "/patientlistbydoctoremailanddate/**",
                        "/userlist",
                        "/getprescriptionbyname/**",
                        "/patientlistbyemail/**",
                        "/patientlist",
                        "/gettotalpatients",
                        "/gettotalappointments",
                        "/gettotalprescriptions",
                        "/profileDetails/**",
                        "/updateuser",
                        "/bookNewAppointment",
                        "/deletedoctor/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**")
                .permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    AccessDeniedHandler defaultAccessDeniedHandler = new AccessDeniedHandlerImpl();
                    defaultAccessDeniedHandler.handle(request, response, accessDeniedException);
                })
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}