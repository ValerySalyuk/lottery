package lv.helloit.lottery.controller;

import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.lottery.LotteryService;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import lv.helloit.lottery.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class Controller {

    private final static Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private final LotteryService lotteryService;
    private final UserService userService;

    @Autowired
    public Controller(LotteryService lotteryService, UserService userService) {
        this.lotteryService = lotteryService;
        this.userService = userService;
    }

    @PostMapping("/start-registration")
    public Response startRegistration(@RequestBody Lottery lottery) {

        LOGGER.info("Starting registration. Lottery: " + lottery.getTitle());
        return lotteryService.openRegistration(lottery);

    }

    @PostMapping("/register")
    public Response register(@RequestBody User user) {
        LOGGER.info("Registering user.");
        return userService.registerUser(user);
    }

    @PutMapping("/stop-registration/{id}")
    public Response stopRegistration (@PathVariable Long id) {
        LOGGER.info("Stopping registration. Lottery ID: " + id);
        return lotteryService.closeRegistration(id);
    }

    @PutMapping("/choose-winner/{id}")
    public Response chooseWinner(@PathVariable Long id) {
        LOGGER.info("Choosing winner. Lottery ID: " + id);
        return lotteryService.chooseWinner(id);
    }

    @GetMapping("/status")
    public Response getStatus(@RequestParam("id") Long lotteryId,
                               @RequestParam("email") String email,
                               @RequestParam("code") String code) {
        LOGGER.info("Status request. Lottery ID: " + lotteryId + ". E-mail: " + email + ". Codse: " + code);
        return lotteryService.getStatus(lotteryId, email, code);
    }

    @GetMapping("/stats")
    public Collection<Lottery> getStats() {
        LOGGER.info("Statistics request");
        return lotteryService.getStats();
    }

    @GetMapping("/lotteries")
    public Collection<Lottery> getLotteries() {
        LOGGER.info("Loading lotteries");
        return lotteryService.getLotteries();
    }

    @DeleteMapping("/delete-lottery/{id}")
    public Response delete(@PathVariable Long id) {
        LOGGER.info("Request to delete lottery with ID : " + id);
        return lotteryService.deleteLottery(id);
    }

}
