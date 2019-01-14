package lv.helloit.lottery.data.dao;

import lv.helloit.lottery.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDAOImplementationTest {

    @Autowired
    private UserDAOImplementation userDAOImplementation;

    private User user1 = new User("some@mail.com", (byte) 21, null, null, null);
    private User user2 = new User("some@mail.com", (byte) 22, null, null, null);

    @Test
    public void shouldInsertUser() {

        Long id = userDAOImplementation.insert(user1);
        assertNotNull(id);

        userDAOImplementation.delete(id);
    }

    @Test
    public void shouldGetById() {

        Long id = userDAOImplementation.insert(user1);
        assertTrue(userDAOImplementation.getById(id).isPresent());

        userDAOImplementation.delete(id);
    }

    @Test
    public void shouldGetAll() {
        Long id1 = userDAOImplementation.insert(user1);
        Long id2 = userDAOImplementation.insert(user2);
        assertEquals(2, userDAOImplementation.getAll().size());

        userDAOImplementation.delete(id1);
        userDAOImplementation.delete(id2);
    }

    @Test
    public void shouldDeleteUser() {

        Long id = userDAOImplementation.insert(user1);
        userDAOImplementation.delete(id);
        assertFalse(userDAOImplementation.getById(id).isPresent());
    }

    @Test
    public void shouldUpdateUser() {

        Long id = userDAOImplementation.insert(user1);
        User existingUser = userDAOImplementation.getById(id).get();
        existingUser.setEmail("another@mail.com");
        userDAOImplementation.update(existingUser);
        User updatedExistingUser = userDAOImplementation.getById(id).get();
        assertEquals("another@mail.com", updatedExistingUser.getEmail());

        userDAOImplementation.delete(id);
    }
}
