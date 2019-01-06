package lv.helloit.lottery.validator;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.data.dao.UserDAOImplementation;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.user.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class Validator {

    public static boolean isAdult(User user) {
        return user.getAge() >= 21;
    }

    public static boolean limitReached(Lottery lottery) {
        return lottery.getUserList().size() == lottery.getLimit();
    }

    public static boolean emailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    public static boolean hasValidCode(User user, LotteryDAOImplementation lotteryDAOImplementation) {

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

    public static boolean codeUnique(User user, UserDAOImplementation userDAOImplementation) {

        List<User> userList = userDAOImplementation.getAll();

        for (User u : userList) {
            if (user.getCode().equals(u.getCode())) {
                return false;
            }
        }

        return true;
    }

    public static boolean lotteryExists(Long id, LotteryDAOImplementation lotteryDAOImplementation) {
        Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(id);
        return wrappedLottery.isPresent();
    }

    public static boolean userInLotteryExists(Long lotteryId,
                                              String email,
                                              String code,
                                              LotteryDAOImplementation lotteryDAOImplementation) {
        Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(lotteryId);
        Lottery lottery = wrappedLottery.get();
        if (lotteryHasUsers(lottery)) {
            for (User u : lottery.getUserList()) {
                if (u.getEmail().equals(email) && u.getCode().equals(code)) {
                    return true;
                }
            }
        }

        return false;

    }

    public static boolean lotteryHasUsers(Lottery lottery) {
        return lottery.getUserList() != null;
    }

    public static boolean lotteryHasWinner(Lottery lottery) {
        return lottery.getWinnerCode() != null;
    }

}
