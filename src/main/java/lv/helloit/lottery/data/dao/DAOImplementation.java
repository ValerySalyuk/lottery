package lv.helloit.lottery.data.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class DAOImplementation<T> implements DAO<T> {

    protected final SessionFactory sessionFactory;

    protected DAOImplementation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<T> getAll(Class<T> tClass) {

        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query =  builder.createQuery(tClass);
        Root<T> root = query.from(tClass);
        query.select(root);

        List<T> tList = session.createQuery(query).getResultList();
        session.close();

        return tList;

    }

    @Override
    public Optional<T> getById(Long id, Class<T> tClass) {

        Session session = sessionFactory.openSession();
        T obj = null;
        if (id != null) {
            obj = session.get(tClass, id);
        }

        session.close();

        return Optional.ofNullable(obj);

    }

    @Override
    public Long insert(T obj) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Long id = (Long) session.save(obj);

        tx.commit();
        session.close();

        return id;

    }

    @Override
    public void delete(Long taskId, Class<T> tClass) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        T obj = this.getById(taskId, tClass).get();

        session.delete(obj);

        tx.commit();
        session.close();

    }

    @Override
    public void update(T obj) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.merge(obj);

        tx.commit();
        session.close();

    }

}
