package lv.helloit.lottery.lottery.dao;

import lv.helloit.lottery.lottery.Lottery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

public class LotteryDAO {

    private final SessionFactory sessionFactory;

    public LotteryDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Long insert(Lottery lottery) {

        Session session = sessionFactory.openSession();

        Transaction tx = session.beginTransaction();

        Long id = (Long) session.save(lottery);

        tx.commit();
        session.close();

        return id;

    }

}
