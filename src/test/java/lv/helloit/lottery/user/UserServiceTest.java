package lv.helloit.lottery.user;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.lottery.LotteryService;
import lv.helloit.lottery.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private LotteryDAOImplementation lotteryDAOImplementation;

    private User user;
    private Lottery lottery;

    @Before
    public void setUp() {
        user = new User("some@mail.com", (byte) 21, null, 1L, null);
        lottery = new Lottery("Lottery for testing", true, 10, new Date(), null, null);
    }

    private void prepare(Long id) {
        // Prepare valid code
        String date = lotteryDAOImplementation.getById(id).get().getStartDate().toString();
        String year = date.substring(2, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        String validFirstPart = day + month + year + 13;

        // Prepare users
        user.setLotteryId(id);
        user.setCode(validFirstPart + "00000000");
    }

    @Test
    @Rollback
    public void shouldRegisterUser() {

        Response response;

        response = lotteryService.openRegistration(lottery);
        Long id = response.getId();
        prepare(id);

        response = userService.registerUser(user);
        assertEquals("OK", response.getStatus());

        lotteryService.deleteLottery(id);
    }

}
