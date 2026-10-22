package com.pfc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService users(
            @org.springframework.beans.factory.annotation.Value("${spring.security.user.name}") String usuario,
            @org.springframework.beans.factory.annotation.Value("${spring.security.user.password}") String senha,
            @org.springframework.beans.factory.annotation.Value("${thindesk.tecnico.username:tecnico}") String tecnico,
            @org.springframework.beans.factory.annotation.Value("${thindesk.tecnico.password:tecnico123}") String senhaTecnico) {
        var encoder = org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new org.springframework.security.provisioning.InMemoryUserDetailsManager(
                org.springframework.security.core.userdetails.User.withUsername(usuario)
                        .password(encoder.encode(senha)).roles("USUARIO").build(),
                org.springframework.security.core.userdetails.User.withUsername(tecnico)
                        .password(encoder.encode(senhaTecnico)).roles("TECNICO").build());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(org.springframework.security.config.Customizer.withDefaults())
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
