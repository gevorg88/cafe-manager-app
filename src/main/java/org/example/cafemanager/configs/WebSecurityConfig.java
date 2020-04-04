package org.example.cafemanager.configs;

import org.example.cafemanager.domain.enums.Role;
import org.example.cafemanager.repositories.UserRepository;
import org.example.cafemanager.exceptionhandlers.LoggingAccessDeniedHandler;
import org.example.cafemanager.services.user.impls.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private LoggingAccessDeniedHandler loggingHandler;

    @Autowired
    public void setLoggingHandler(LoggingAccessDeniedHandler handler) {
        loggingHandler = handler;
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl((UserRepository) getApplicationContext().getBean("userRepo"));
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css/**", "/js/**", "/image/**", "/fonts/**", "/", "/login").permitAll()
                .antMatchers("/welcome", "/tables").authenticated()
                .antMatchers("/users", "/tables/manager/**", "/users/**", "/products", "/products/**")
                .hasAuthority(Role.MANAGER.getAuthority()).antMatchers("/orders", "/orders/**", "/tables/waiter/**")
                .hasAuthority(Role.WAITER.getAuthority()).and().formLogin().failureUrl("/login?error=Wrong Credentials")
                .loginPage("/login").usernameParameter("username").defaultSuccessUrl("/welcome").permitAll().and()
                .logout().invalidateHttpSession(true).clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login").permitAll().and()
                .exceptionHandling().accessDeniedHandler(loggingHandler);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
    }
}
