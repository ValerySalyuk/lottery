package lv.helloit.lottery.validator;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.data.dao.UserDAOImplementation;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.user.User;

import java.util.List;
import java.util.Optional;

public class Validator {

    public static boolean userHasRequiredData(User user) {
        return !user.getEmail().isEmpty() && user.getAge() != null && !user.getCode().isEmpty();
    }

    public static boolean userIsAdult(User user) {
        return user.getAge() >= 21;
    }

    public static boolean userLimitReached(Lottery lottery) {
        return lottery.getUserList().size() == lottery.getLimit();
    }

    public static boolean emailValid(String email) {
        return email.contains("@")
                && email.contains(".")
                && email.lastIndexOf(".") > email.lastIndexOf("@");
    }

    public static boolean userHasValidCode(User user, LotteryDAOImplementation lotteryDAOImplementation) {

        Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(user.getLottery().getId());

        if (wrappedLottery.isPresent() && user.getCode().length() == 16 && user.getCode().matches("[0-9]+")) {

            Lottery lottery = wrappedLottery.get();
            String year = lottery.getStartDate().toString().substring(2, 4);
            String month = lottery.getStartDate().toString().substring(5, 7);
            String day = lottery.getStartDate().toString().substring(8, 10);
            String mailLength = user.getEmail().length() > 9
                    ? Integer.toString(user.getEmail().length())
                    : "0" + user.getEmail().length();

            String validFirstPart = day + month + year + mailLength;

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

    public static boolean lotteryHasRequiredData(Lottery lottery) {
        return !lottery.getTitle().isEmpty() && lottery.getLimit() != null;
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
        return lottery.getUserList() != null && lottery.getUserList().size() != 0;
    }

    public static boolean lotteryHasWinner(Lottery lottery) {
        return lottery.getWinnerCode() != null;
    }

}
