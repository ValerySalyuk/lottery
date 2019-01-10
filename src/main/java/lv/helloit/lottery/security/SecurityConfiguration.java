package lv.helloit.lottery.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DefaultAuthEntryPoint defaultAuthEntryPoint;
    private final CustomAuthProvider customAuthProvider;

    @Autowired
    public SecurityConfiguration(DefaultAuthEntryPoint defaultAuthEntryPoint, CustomAuthProvider customAuthProvider) {
        this.defaultAuthEntryPoint = defaultAuthEntryPoint;
        this.customAuthProvider = customAuthProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admintools.html").authenticated()
                .antMatchers("/newlottery.html").authenticated()
                .antMatchers("/start-registration**").authenticated()
                .antMatchers("/stop-registration**").authenticated()
                .antMatchers("/choose-winner**").authenticated()
                .antMatchers("/stats**").authenticated()
                .antMatchers("/delete-lottery**").authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(defaultAuthEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
