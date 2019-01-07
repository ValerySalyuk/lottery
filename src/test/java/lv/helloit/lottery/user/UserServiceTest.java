package lv.helloit.lottery.user;

import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.lottery.LotteryService;
import lv.helloit.lottery.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    private User user;
    private Lottery lottery;

    @Before
    public void setUp() {
        user = new User("some@mail.com", (byte) 21, "0601191312345678", 1L, null);
        lottery = new Lottery("Test lottery", true, 10, new Date(), null, null);
    }

    @Test
    public void shouldRegisterUser() {
        lotteryService.openRegistration(lottery);
        Response response = userService.registerUser(user);
        assertEquals("OK", response.getStatus());
    }

}
