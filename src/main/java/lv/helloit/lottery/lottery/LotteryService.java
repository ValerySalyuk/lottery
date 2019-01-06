package lv.helloit.lottery.lottery;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
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

        if (wrappedLottery.isPresent()) {
            lottery = wrappedLottery.get();
            if (!lottery.isOpen()) {
                response.setReason("Lottery with ID: " + id + " is already stopped");
            } else {
                lottery.setOpen(false);
                lottery.setEndDate(new Date());
                lotteryDAOImplementation.update(lottery);
                response.setStatus("OK");
            }
        } else {
            response.setReason("Lottery with ID: " + id + " does not exist");
        }

        return response;
    }

    public Response chooseWinner(Long id) {

        Response response = new Response();
        response.setStatus("Fail");

        Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(id);
        Lottery lottery;

        if (wrappedLottery.isPresent()) {
            lottery = wrappedLottery.get();
            if (lottery.isOpen()) {
                response.setReason("Lottery with ID: " + id + " is still open for registration");
            } else if (lottery.getWinnerCode() != null) {
                response.setReason("Lottery with ID: " + id + " already has a winner");
            } else if (lottery.getUserList() == null) {
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
        } else {
            response.setReason("Lottery with ID: " + id + " does not exist");
        }

        return response;

    }

}
