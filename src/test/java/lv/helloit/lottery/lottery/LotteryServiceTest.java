package lv.helloit.lottery.lottery;


import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import lv.helloit.lottery.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryServiceTest {

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private UserService userService;

    private Lottery lottery;
    private User user1;
    private User user2;

    @Before
    public void setUp() {
        lottery = new Lottery("Test lottery", true, 10, null, null, null);
        user1 = new User("some@mail.com", (byte) 21, "0601191312345678", 1L, null);
        user2 = new User("some@mail.com", (byte) 21, "0601191312345679", 1L, null);
    }

    @Test
    public void shouldCreateLottery() {

        Response response = lotteryService.openRegistration(lottery);

        assertEquals(1, response.getId().intValue());
        assertEquals("OK", response.getStatus());

    }

    @Test
    public void shouldCloseLottery() {
        lotteryService.openRegistration(lottery);
        Response response = lotteryService.closeRegistration(1L);

        assertEquals("OK", response.getStatus());
    }

    @Test
    public void shouldChooseWinner() {
        lotteryService.openRegistration(lottery);
        userService.registerUser(user1);
        userService.registerUser(user2);
        lotteryService.closeRegistration(1L);
        Response response = lotteryService.chooseWinner(1L);

        assertEquals("OK", response.getStatus());
    }

}
