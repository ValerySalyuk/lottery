package lv.helloit.lottery.data;

import lv.helloit.lottery.admin.Admin;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.user.User;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration()
                .addAnnotatedClass(Lottery.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Admin.class)
                .configure()
                .buildSessionFactory();
    }

}
