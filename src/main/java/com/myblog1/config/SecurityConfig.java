package com.myblog1.config;

import com.myblog1.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    public static final String[] WHITE_LIST_URLS = {
            "/v2/api-docs",
            "/swagger-resources/**",
            "/api/auth/**",
            "/v3/api-docs",
            "/webjars/**",
            "/swagger-ui/index.html",
    };

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(WHITE_LIST_URLS).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
    // The configure(AuthenticationManagerBuilder auth) method configures the authentication manager (auth) to use a custom UserDetailsService.
    //The userDetailsService is responsible for loading user details from a data source (e.g., a database) based on the provided username/email.
    //The password encoder (passwordEncoder) is also configured for secure password handling.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    // @Override
    // @Bean
    // protected UserDetailsService userDetailsService() {
    // UserDetails ramesh = User.builder().username("ramesh").password(passwordEncoder()
    // .encode("password")).roles("USER").build();
    // UserDetails admin = User.builder().username("admin").password(passwordEncoder()
    // .encode("admin")).roles("ADMIN").build();
    // return new InMemoryUserDetailsManager(ramesh, admin);
    // }
}