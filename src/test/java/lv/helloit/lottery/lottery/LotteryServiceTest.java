package lv.helloit.lottery.lottery;


import lv.helloit.lottery.response.Response;
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

    private Lottery lottery;

    @Before
    public void setUp() {
        lottery = new Lottery("Test lottery", true, 10, null, null, null);
    }

    @Test
    public void shouldCreateLottery() {

        Response response = lotteryService.openLottery(lottery);

        assertEquals(1, response.getId().intValue());
        assertEquals("OK", response.getStatus());

    }

    @Test
    public void shouldCloseLottery() {
        lotteryService.openLottery(lottery);
        Response response = lotteryService.closeLottery(1L);

        assertEquals("OK", response.getStatus());
    }

}
