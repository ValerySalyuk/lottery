package lv.helloit.lottery.data.dao;

import lv.helloit.lottery.lottery.Lottery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryDAOImplementationTest {

    @Autowired
    private LotteryDAOImplementation lotteryDAOImplementation;

    private Lottery lottery1 = new Lottery("Lottery for testing 1", true, 10, null, null, null);
    private Lottery lottery2 = new Lottery("Lottery for testing 2", true, 10, null, null, null);

    @Test
    public void shouldInsertLottery() {

        Long id = lotteryDAOImplementation.insert(lottery1);
        assertNotNull(id);

        lotteryDAOImplementation.delete(id);
    }

    @Test
    public void shouldGetById() {

        Long id = lotteryDAOImplementation.insert(lottery1);
        assertTrue(lotteryDAOImplementation.getById(id).isPresent());

        lotteryDAOImplementation.delete(id);
    }

    @Test
    public void shouldGetAll() {
        Long id1 = lotteryDAOImplementation.insert(lottery1);
        Long id2 = lotteryDAOImplementation.insert(lottery2);
        assertEquals(2, lotteryDAOImplementation.getAll().size());

        lotteryDAOImplementation.delete(id1);
        lotteryDAOImplementation.delete(id2);
    }

    @Test
    public void shouldDeleteLottery() {

        Long id = lotteryDAOImplementation.insert(lottery1);
        lotteryDAOImplementation.delete(id);
        assertFalse(lotteryDAOImplementation.getById(id).isPresent());
    }

    @Test
    public void shouldUpdateLottery() {

        Long id = lotteryDAOImplementation.insert(lottery1);
        Lottery existingLottery = lotteryDAOImplementation.getById(id).get();
        existingLottery.setTitle("Updated title");
        lotteryDAOImplementation.update(existingLottery);
        Lottery updatedExistingLottery = lotteryDAOImplementation.getById(id).get();
        assertEquals("Updated title", updatedExistingLottery.getTitle());

        lotteryDAOImplementation.delete(id);
    }

}
