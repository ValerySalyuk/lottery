package lv.helloit.lottery.lottery;


import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import lv.helloit.lottery.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryServiceTest {

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private UserService userService;

    @Autowired
    LotteryDAOImplementation lotteryDAOImplementation;  // required only to dynamically have correct codes

    private Lottery lottery;
    private User user1;
    private User user2;

    @Before
    public void setUp() {
        lottery = new Lottery("Lottery for testing", true, 10, null, null, null);
        user1 = new User("some@mail.com", (byte) 21, null, 1L, null);
        user2 = new User("some@mail.com", (byte) 21, null, 1L, null);
    }

    private void prepare(Long id) {
        // Prepare valid code
        String date = lotteryDAOImplementation.getById(id).get().getStartDate().toString();
        String year = date.substring(2, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        String validFirstPart = day + month + year + 13;

        // Prepare users
        user1.setLotteryId(id);
        user1.setCode(validFirstPart + "00000000");
        user2.setLotteryId(id);
        user2.setCode(validFirstPart + "00000001");
    }

    @Test
    public void shouldCreateLottery() {

        Response response = lotteryService.openRegistration(lottery);

        assertEquals("OK", response.getStatus());

        lotteryService.deleteLottery(response.getId());

    }

    @Test
    public void shouldCloseLottery() {

        Response response;
        Long id;

        response = lotteryService.openRegistration(lottery);
        id = response.getId();

        response = lotteryService.closeRegistration(id);
        assertEquals("OK", response.getStatus());

        response = lotteryService.closeRegistration(id);
        assertEquals("Fail", response.getStatus());

        lotteryService.deleteLottery(id);

    }

    @Test
    public void shouldChooseWinner() {

        Response response;
        Long id;

        response = lotteryService.openRegistration(lottery);
        id = response.getId();
        response = lotteryService.chooseWinner(id);
        assertEquals("Fail", response.getStatus());

        prepare(id);

        userService.registerUser(user1);
        userService.registerUser(user2);
        response = lotteryService.chooseWinner(id);
        assertEquals("Fail", response.getStatus());

        lotteryService.closeRegistration(id);
        response = lotteryService.chooseWinner(id);
        assertEquals("OK", response.getStatus());

        lotteryService.deleteLottery(id);

    }

    @Test
    public void shouldReturnStatus() {

        Response response;
        Long id;

        response = lotteryService.getStatus(19468763L, user1.getEmail(), user1.getCode());
        assertEquals("ERROR", response.getStatus());

        response = lotteryService.openRegistration(lottery);
        id = response.getId();
        prepare(id);
        response = lotteryService.getStatus(id, user1.getEmail(), user1.getCode());
        assertEquals("ERROR", response.getStatus());

        response = lotteryService.getStatus(id, user2.getEmail(), user2.getCode());
        assertEquals("ERROR", response.getStatus());

        userService.registerUser(user1);
        response = lotteryService.getStatus(id, user1.getEmail(), user1.getCode());
        assertEquals("PENDING", response.getStatus());

        lotteryService.closeRegistration(id);
        response = lotteryService.getStatus(id, user1.getEmail(), user1.getCode());
        assertEquals("PENDING", response.getStatus());

        lotteryService.chooseWinner(id);
        response = lotteryService.getStatus(id, user1.getEmail(), user1.getCode());
        assertEquals("WIN", response.getStatus());

        lotteryService.deleteLottery(id);

    }

    @Test
    public void shouldDeleteLottery() {

        Response response;
        response = lotteryService.openRegistration(lottery);
        Long id = response.getId();
        response = lotteryService.deleteLottery(id);

        assertEquals("OK", response.getStatus());

        response = lotteryService.deleteLottery(id);
        assertEquals("Fail", response.getStatus());

    }

}
