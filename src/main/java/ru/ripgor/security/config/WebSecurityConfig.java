package ru.ripgor.security.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ripgor.security.service.UserDetailsServiceImpl;

/**
 * Класс с основными настройками секьюрности.
 * Сюда стекается вся логика Security и именно здесь мы ее настраиваем.
 * В методе configure(HttpSecurity) мы разграничиваем доступ на различные запросы
 * в зависимости от ролей.
 * В методе configure(AuthenticationManagerBuilder) мы определяем, собственно, вид,
 * в котором будут храниться данные о пользователях, а также необходимые детали
 * вроде UserDetailsService для работы security и PasswordEncoder для конфиденциальности паролей.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    private SuccessUserHandler successUserHandler;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setSuccessUserHandler(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   // Отключаем csrf-защиту (чтобы не париться)
                .authorizeRequests()    // настройка авторизованных запросов
                .antMatchers("/").authenticated()    // В корень пускаем после аутентификации
                .and()
                .formLogin().permitAll()    // Настройка страницы логина -- пускаем всех
                .successHandler(successUserHandler)     // Прикручиваем к ней логику перенаправления на нужные запросы (Да, знаю что не используется)
                .and()
                .logout().permitAll();      // на страницу логаута пускаем всех

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
