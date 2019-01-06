package lv.helloit.lottery.data.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    List<T> getAll(Class<T> tClass);

    Optional<T> getById(Long id, Class<T> tClass);

    Long insert(T t);

    void delete(Long id, Class<T> tClass);

    void update(T t);

}
