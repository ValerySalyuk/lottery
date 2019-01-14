package lv.helloit.lottery.data.dao;

import lv.helloit.lottery.admin.Admin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminDAOImplementationTest {

    @Autowired
    private AdminDAOImplementation adminDAOImplementation;

    private Admin admin1 = new Admin("admin1", "hash1");
    private Admin admin2 = new Admin("admin2", "hash2");

    @Test
    public void shouldInsertAdmin() {

        admin1.setPassword("password");

        Long id = adminDAOImplementation.insert(admin1);
        assertNotNull(id);

        adminDAOImplementation.delete(id);
    }

    @Test
    public void shouldGetById() {

        admin1.setPassword("password");

        Long id = adminDAOImplementation.insert(admin1);
        assertTrue(adminDAOImplementation.getById(id).isPresent());

        adminDAOImplementation.delete(id);
    }

    @Test
    public void shouldGetAll() {

        admin1.setPassword("password");
        admin2.setPassword("password");

        Long id1 = adminDAOImplementation.insert(admin1);
        Long id2 = adminDAOImplementation.insert(admin2);
        assertEquals(2, adminDAOImplementation.getAll().size());

        adminDAOImplementation.delete(id1);
        adminDAOImplementation.delete(id2);
    }

    @Test
    public void shouldDeleteAdmin() {

        admin1.setPassword("password");

        Long id = adminDAOImplementation.insert(admin1);
        adminDAOImplementation.delete(id);
        assertFalse(adminDAOImplementation.getById(id).isPresent());
    }

    @Test
    public void shouldUpdateAdmin() {

        admin1.setPassword("password");

        Long id = adminDAOImplementation.insert(admin1);
        Admin existingAdmin = adminDAOImplementation.getById(id).get();
        existingAdmin.setLogin("updated_login");
        existingAdmin.setPassword("password");
        adminDAOImplementation.update(existingAdmin);
        Admin updatedExistingAdmin = adminDAOImplementation.getById(id).get();
        assertEquals("updated_login", updatedExistingAdmin.getLogin());

        adminDAOImplementation.delete(id);
    }

    @Test
    public void shouldGetByLogin() {

        admin1.setPassword("password");

        Long id = adminDAOImplementation.insert(admin1);
        assertTrue(adminDAOImplementation.getByLogin("admin1").isPresent());

        adminDAOImplementation.delete(id);

    }
}
