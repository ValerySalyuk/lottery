package lv.helloit.lottery.data.dao;

import lv.helloit.lottery.lottery.Lottery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LotteryDAOImplementation extends DAOImplementation<Lottery> {

    @Autowired
    public LotteryDAOImplementation(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Lottery> getAll() {
        return super.getAll(Lottery.class);
    }

    public Optional<Lottery> getById(Long id) {
        return super.getById(id, Lottery.class, false);
    }

    public void delete(Long id) {
        super.delete(id, Lottery.class);
    }

}
