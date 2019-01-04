package lv.helloit.lottery.lottery.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LotteryDAOImplementation extends LotteryDAO {

    @Autowired
    public LotteryDAOImplementation(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
