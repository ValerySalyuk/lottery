package lv.helloit.lottery.validator;

import lv.helloit.lottery.admin.Admin;
import lv.helloit.lottery.data.dao.AdminDAOImplementation;
import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.data.dao.UserDAOImplementation;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.user.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class Validator {

    public static boolean userHasRequiredData(User user) {

        return user.getEmail() != null
                && !user.getEmail().isEmpty()
                && user.getAge() != null
                && user.getCode() != null;
    }

    public static boolean userIsAdult(User user) {
        return user.getAge() >= 21;
    }

    public static boolean userLimitReached(Lottery lottery) {
        return lottery.getUserList().size() >= lottery.getLimit();
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

            Calendar cal = Calendar.getInstance();
            cal.setTime(lottery.getStartDate());

            String year = Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4);

            String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
            if ((cal.get(Calendar.MONTH) + 1) < 10) {
                month = "0" + month;
            }

            String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            if ((cal.get(Calendar.DAY_OF_MONTH)) < 10) {
                day = "0" + day;
            }

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
        return lottery.getTitle() != null && lottery.getLimit() != null && !lottery.getTitle().isEmpty();
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
        return lottery.getWinnerCode() != null && !lottery.getWinnerCode().isEmpty();
    }

    public static boolean adminHasRequiredData(Admin admin) {
        return admin.getLogin() != null && !admin.getLogin().isEmpty()
                && admin.getPassword() != null && !admin.getPassword().isEmpty();
    }

    public static boolean adminLoginExists(Admin admin, AdminDAOImplementation adminDAOImplementation) {
        return adminDAOImplementation.getByLogin(admin.getLogin()).isPresent();
    }

}
