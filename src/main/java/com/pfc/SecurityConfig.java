package com.pfc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService users(
            @Value("${spring.security.user.name}") String usuario,
            @Value("${spring.security.user.password}") String senha,
            @Value("${thindesk.tecnico.username:tecnico}") String tecnico,
            @Value("${thindesk.tecnico.password:tecnico123}") String senhaTecnico) {
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new InMemoryUserDetailsManager(
                User.withUsername(usuario)
                        .password(encoder.encode(senha)).roles("USUARIO").build(),
                User.withUsername(tecnico)
                        .password(encoder.encode(senhaTecnico)).roles("TECNICO").build());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(errors -> errors.defaultAuthenticationEntryPointFor(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    new AntPathRequestMatcher("/api/**")))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/dist/**", "/plugins/**", "/js/**", "/images/**").permitAll() // Permitir acesso à página de login e arquivos estáticos
                .anyRequest().authenticated() // Exigir autenticação para qualquer outra página
            )
            .formLogin(form -> form
                .loginPage("/login") // Definir a URL da página de login personalizada
                .defaultSuccessUrl("/", true) // Redirecionar para /home após login bem-sucedido
                .permitAll() // Permitir que todos acessem a página de login
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Redirecionar para o login após logout
            );
        return http.build();
    }
}
