package lv.helloit.lottery.lottery;

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
    public StartRegistrationResponse startRegistration(@RequestBody Lottery lottery) {

        StartRegistrationResponse startRegistrationResponse = new StartRegistrationResponse();
        Long id = lotteryService.openLottery(lottery);

        if (id != null) {
            startRegistrationResponse.setStatus("OK");
            startRegistrationResponse.setId(id);
        } else {
            startRegistrationResponse.setStatus("Fail");
            startRegistrationResponse.setReason("Reason"); //todo
        }

        LOGGER.info("Started registration. Lottery: " + lottery.getTitle() + ". Status: " + startRegistrationResponse.getStatus());

        return startRegistrationResponse;
    }

}
