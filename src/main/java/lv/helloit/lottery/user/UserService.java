package lv.helloit.lottery.user;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.data.dao.UserDAOImplementation;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserDAOImplementation userDAOImplementation;
    private final LotteryDAOImplementation lotteryDAOImplementation;

    @Autowired
    public UserService(UserDAOImplementation userDAOImplementation, LotteryDAOImplementation lotteryDAOImplementation) {
        this.userDAOImplementation = userDAOImplementation;
        this.lotteryDAOImplementation = lotteryDAOImplementation;
    }

    public Response registerUser(User user) {

        Response response = new Response();

        Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(user.getLotteryId());
        if (wrappedLottery.isPresent()) {
            user.setLottery(wrappedLottery.get());
        }

        if (!user.getLottery().isOpen()) {
            response.setStatus("Fail");
            response.setReason("Lottery with ID: " + user.getLotteryId() + " is closed");
        } else if (Validator.limitReached(user.getLottery())) {
            response.setStatus("Fail");
            response.setReason("Participants limit is reached");
        }else if (!Validator.isAdult(user)) {
            response.setStatus("Fail");
            response.setReason("Participation allowed only from 21 years");
        } else if(!Validator.emailValid(user.getEmail())) {
            response.setStatus("Fail");
            response.setReason("Invalid e-mail");
        } else if (!Validator.hasValidCode(user, lotteryDAOImplementation)) {
            response.setStatus("Fail");
            response.setReason("Code is invalid");
        } else if (!Validator.codeUnique(user, userDAOImplementation)) {
            response.setStatus("Fail");
            response.setReason("This code is already registered");
        } else {
            userDAOImplementation.insert(user);
            response.setStatus("OK");
        }

        return response;

    }

}
