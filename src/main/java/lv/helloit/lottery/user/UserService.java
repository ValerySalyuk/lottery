package lv.helloit.lottery.user;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.data.dao.UserDAOImplementation;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

        if (!isAdult(user)) {
            response.setStatus("Fail");
            response.setReason("Participation allowed only from 21 years");
        } else if(!hasValidEmail(user)) {
            response.setStatus("Fail");
            response.setReason("Invalid e-mail");
        } else if (!hasValidCode(user)) {
            response.setStatus("Fail");
            response.setReason("Code is invalid");
        } else if (!codeUnique(user)) {
            response.setStatus("Fail");
            response.setReason("This code is already registered");
        } else {
            userDAOImplementation.insert(user);
            response.setStatus("OK");
        }

        return response;

    }

    private boolean isAdult(User user) {
        return user.getAge() >= 21;
    }

    private boolean hasValidEmail(User user) {
        return user.getEmail().contains("@") && user.getEmail().contains(".");
    }

    private boolean hasValidCode(User user) {

        Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(user.getLottery().getId());

        if (wrappedLottery.isPresent()) {

            Lottery lottery = wrappedLottery.get();
            String year = lottery.getStartDate().toString().substring(2, 4);
            String month = lottery.getStartDate().toString().substring(5, 7);
            String day = lottery.getStartDate().toString().substring(8, 10);

            String validFirstPart = day + month + year + user.getEmail().length();

            String userCodeFirstPart = user.getCode().substring(0, 8);

            return userCodeFirstPart.equals(validFirstPart);

        }

        return false;
    }

    private boolean codeUnique(User user) {

        List<User> userList = userDAOImplementation.getAll();

        for (User u : userList) {
            if (user.getCode().equals(u.getCode())) {
                return false;
            }
        }

        return true;
    }

}
