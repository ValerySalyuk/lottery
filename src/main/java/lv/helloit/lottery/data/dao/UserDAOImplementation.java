package lv.helloit.lottery.data.dao;

import lv.helloit.lottery.user.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImplementation extends DAOImplementation<User> {

    @Autowired
    public UserDAOImplementation(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<User> getAll() {
        return super.getAll(User.class);
    }

    public Optional<User> getById(Long id) {
        return super.getById(id, User.class);
    }

    public void delete(Long id) {
        super.delete(id, User.class);
    }

}
