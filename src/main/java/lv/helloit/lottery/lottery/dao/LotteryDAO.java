package lv.helloit.lottery.lottery.dao;

import lv.helloit.lottery.lottery.Lottery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class LotteryDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public LotteryDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Lottery> getAll() {

        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Lottery> query =  builder.createQuery(Lottery.class);
        Root<Lottery> root = query.from(Lottery.class);
        query.select(root);

        List<Lottery> lotteryList = session.createQuery(query).getResultList();

        return lotteryList;

    }

    public Optional<Lottery> getById(Long id) {

        Session session = sessionFactory.openSession();
        Lottery lottery = null;
        if (id != null) {
            lottery = session.get(Lottery.class, id);
        }

        return Optional.ofNullable(lottery);

    }

    public Long insert(Lottery lottery) throws ConstraintViolationException {

        Session session = sessionFactory.openSession();

        Transaction tx = session.beginTransaction();

        Long id = (Long) session.save(lottery);

        tx.commit();
        session.close();

        return id;

    }

    public void delete(Long id) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Lottery lottery = this.getById(id).get();

        session.delete(lottery);

        tx.commit();
        session.close();

    }

    public void update(Lottery lottery) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.update(lottery);

        tx.commit();
        session.close();

    }

}
