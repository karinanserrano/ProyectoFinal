package com.saleggforce.egg.configuraciones;

import com.saleggforce.egg.servicios.EmpleadoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfiguracionSeguridad extends WebSecurityConfigurerAdapter {

    @Autowired
    public EmpleadoServicio empleadoServicio;

    @Autowired
    public void configureGlobant(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(empleadoServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                /*
                .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()
                .and().
                formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("usuario")
                .passwordParameter("password")
                .defaultSuccessUrl("/empleados")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll().
                and().csrf().disable();*/
                .authorizeRequests()
                /*.antMatchers("/admin/*").hasRole("ADMIN")*/
                .antMatchers("/css/*", "/js/*", "/img/*",
                        "/**").permitAll()
                .and().
                formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("usuario")
                .passwordParameter("password")
                .defaultSuccessUrl("/empleados")
                .permitAll()
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll().
                and().csrf().disable();
    }

}
