package lv.helloit.lottery.data.dao;

import lv.helloit.lottery.admin.Admin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class AdminDAOImplementation extends DAOImplementation<Admin> {

    @Autowired
    public AdminDAOImplementation(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Admin> getAll() {
        return super.getAll(Admin.class);
    }

    public Optional<Admin> getById(Long id) {
        return super.getById(id, Admin.class, false);
    }

    public Optional<Admin> getByLogin(String login) {

        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Admin> query =  builder.createQuery(Admin.class);
        Root<Admin> root = query.from(Admin.class);
        query.where(builder.equal(root.get("login"), login));
        query.select(root);

        Admin admin;

        try {
            admin = session.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }

        session.close();

        return Optional.ofNullable(admin);

    }

    public void delete(Long id) {
        super.delete(id, Admin.class);
    }

}
