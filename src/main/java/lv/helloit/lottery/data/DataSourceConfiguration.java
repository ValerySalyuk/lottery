package lv.helloit.lottery.data;

import lv.helloit.lottery.lottery.Lottery;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration()
                .addAnnotatedClass(Lottery.class)
                .configure()
                .buildSessionFactory();
    }

}
