package lv.helloit.lottery.lottery;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.data.dao.UserDAOImplementation;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import lv.helloit.lottery.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LotteryService {

    private final LotteryDAOImplementation lotteryDAOImplementation;
    private final UserDAOImplementation userDAOImplementation;

    @Autowired
    public LotteryService(LotteryDAOImplementation lotteryDAOImplementation, UserDAOImplementation userDAOImplementation) {
        this.lotteryDAOImplementation = lotteryDAOImplementation;
        this.userDAOImplementation = userDAOImplementation;
    }

    public Response openRegistration(Lottery lottery) {

        Response response = new Response();
        response.setStatus("Fail");

        lottery.setStartDate(new Date());
        lottery.setOpen(true);

        if (Validator.lotteryHasRequiredData(lottery)) {
            try {
                Long id = lotteryDAOImplementation.insert(lottery);
                response.setStatus("OK");
                response.setId(id);
            } catch (Exception e) {
                response.setReason("Lottery name already exists!");
            }
        } else {
            response.setReason("Please provide all required data");
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
            } else if (!Validator.lotteryHasUsers(lottery)) {
                response.setReason("Lottery with ID: " + id + " has no participants");
            } else if (Validator.lotteryHasWinner(lottery)) {
                response.setReason("Lottery with ID: " + id + " already has a winner");
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
            response.setReason("There is no such participant in this lottery");
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

    public List<Lottery> getStats() {
        return new ArrayList<>(lotteryDAOImplementation.getAll());
    }

    public List<Lottery> getLotteries() {

        List<Lottery> lotteriesSimplified = new ArrayList<>();
        List<Lottery> actualLotteries = lotteryDAOImplementation.getAll();

        for (Lottery l : actualLotteries) {
            l.setUserList(null);
            l.setWinnerCode(null);
            l.setLimit(null);
            lotteriesSimplified.add(l);
        }

        return lotteriesSimplified;
    }

    public Response deleteLottery(Long id) {

        Response response = new Response();
        response.setStatus("Fail");

        if (Validator.lotteryExists(id, lotteryDAOImplementation)) {
            Lottery lottery = lotteryDAOImplementation.getById(id).get();
            if (Validator.lotteryHasUsers(lottery)) {
                for (User u : lottery.getUserList()) {
                    userDAOImplementation.delete(u.getId());
                }
            }
            lotteryDAOImplementation.delete(id);
            response.setStatus("OK");
        } else {
            response.setReason("Lottery with ID: " + id + " does not exist");
        }

        return response;

    }

}
