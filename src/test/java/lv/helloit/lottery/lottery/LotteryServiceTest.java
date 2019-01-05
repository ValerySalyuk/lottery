package lv.helloit.lottery.lottery;


import lv.helloit.lottery.response.Response;
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

    @Test
    public void shouldCreateLottery() {

        Lottery lottery = new Lottery("Test lottery", true, 10, null, null, null);

        Response response = lotteryService.openLottery(lottery);

        assertEquals(response.getId().intValue(), 1);
        assertEquals(response.getStatus(), "OK");

    }

    @Test
    public void shouldValidateCode() {
        // todo
    }

    @Test
    public void codeShouldBeUnique() {
        // todo
    }

    @Test
    public void shouldRegisterUser() {
        // todo
    }

}
