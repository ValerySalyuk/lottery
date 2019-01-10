package lv.helloit.lottery.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:security.properties")
public class SecurityProperties {

    @Value("${auth.salt.salt}")
    private String salt;

    public String getSalt() {
        return salt;
    }

}
