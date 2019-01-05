package lv.helloit.lottery.lottery;

import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LotteryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(LotteryController.class);

    private final LotteryService lotteryService;

    @Autowired
    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @PostMapping("/start-registration")
    public Response startRegistration(@RequestBody Lottery lottery) {

        LOGGER.info("Starting registration. Lottery: " + lottery.getTitle());
        return lotteryService.openLottery(lottery);

    }

    @PostMapping("register")
    public Response register(@RequestBody User user) {
        LOGGER.info("Registering user.");
        return lotteryService.registerUser(user);
    }

}
