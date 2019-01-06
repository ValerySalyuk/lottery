package lv.helloit.lottery.lottery;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import lv.helloit.lottery.validator.Validator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class LotteryService {

    private final LotteryDAOImplementation lotteryDAOImplementation;

    @Autowired
    public LotteryService(LotteryDAOImplementation lotteryDAOImplementation) {
        this.lotteryDAOImplementation = lotteryDAOImplementation;
    }

    public Response openRegistration(Lottery lottery) {

        Response response = new Response();

        lottery.setStartDate(new Date());
        lottery.setOpen(true);

        try {
            Long id = lotteryDAOImplementation.insert(lottery);
            response.setStatus("OK");
            response.setId(id);
        } catch (Exception e) {
            response.setStatus("Fail");
            response.setReason("Lottery name already exists!");
        }

        return response;
    }

    public Response closeRegistration(Long id) {

        Response response = new Response();
        response.setStatus("Fail");

        Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(id);
        Lottery lottery;

        if (!Validator.lotteryExists(id, lotteryDAOImplementation)) {
            response.setReason("Lottery with ID: " + id + " does not exist");
        } else {
            lottery = wrappedLottery.get();
            if (!lottery.isOpen()) {
                response.setReason("Lottery with ID: " + id + " is already stopped");
            } else {
                lottery.setOpen(false);
                lottery.setEndDate(new Date());
                lotteryDAOImplementation.update(lottery);
                response.setStatus("OK");
            }
        }

        return response;
    }

    public Response chooseWinner(Long id) {

        Response response = new Response();
        response.setStatus("Fail");

        Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(id);
        Lottery lottery;

        if (!Validator.lotteryExists(id, lotteryDAOImplementation)) {
            response.setReason("Lottery with ID: " + id + " does not exist");
        } else {
            lottery = wrappedLottery.get();
            if (lottery.isOpen()) {
                response.setReason("Lottery with ID: " + id + " is still open for registration");
            } else if (Validator.lotteryHasWinner(lottery)) {
                response.setReason("Lottery with ID: " + id + " already has a winner");
            } else if (!Validator.lotteryHasUsers(lottery)) {
                response.setReason("Lottery with ID: " + id + " has no participants");
            } else {
                Random random = new Random();
                int winner = random.nextInt(lottery.getUserList().size()) + 1;
                String winCode = lottery.getUserList().get(winner - 1).getCode();
                lottery.setWinnerCode(winCode);
                lotteryDAOImplementation.update(lottery);
                response.setStatus("OK");
                response.setWinnerCode(winCode);
            }
        }

        return response;

    }

    public Response getStatus(Long lotteryId, String email, String code) {

        Response response = new Response();
        response.setStatus("ERROR");

        if (!Validator.lotteryExists(lotteryId, lotteryDAOImplementation)) {
            response.setReason("Lottery with ID: " + lotteryId + " does not exist");
        } else if (!Validator.userInLotteryExists(lotteryId, email, code, lotteryDAOImplementation)) {
            response.setReason("There is no such user in lottery with ID: " + lotteryId);
        } else {
            Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(lotteryId);
            Lottery lottery = wrappedLottery.get();
            if (lottery.isOpen() || !Validator.lotteryHasWinner(lottery)) {
                response.setStatus("PENDING");
            } else if (lottery.getWinnerCode().equals(code)) {
                response.setStatus("WIN");
            } else {
                response.setStatus("LOOSE");
            }
        }

        return response;

    }

}
