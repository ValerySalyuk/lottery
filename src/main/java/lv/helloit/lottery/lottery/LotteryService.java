package lv.helloit.lottery.lottery;

import lv.helloit.lottery.lottery.dao.LotteryDAO;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LotteryService {

    private final LotteryDAO lotteryDAO;

    @Autowired
    public LotteryService(LotteryDAO lotteryDAO) {
        this.lotteryDAO = lotteryDAO;
    }

    public Response openLottery(Lottery lottery) {

        Response response = new Response();

        lottery.setStartDate(new Date());
        lottery.setOpen(true);

        try {
            Long id = lotteryDAO.insert(lottery);
            response.setStatus("OK");
            response.setId(id);
        } catch (Exception e) {
            response.setStatus("Fail");
            response.setReason("Lottery name already exists!");
        }

        return response;
    }

    public Response registerUser(User user) {

        Response response = new Response();

        if (!isAdult(user)) {
            response.setStatus("Fail");
            response.setReason("Participation allowed only from 21 years");
        } else if (!hasValidCode(user)) {
            response.setStatus("Fail");
            response.setReason("Code is invalid");
        } else if (!codeUnique(user)) {
            response.setStatus("Fail");
            response.setReason("This code already participates in this lottery");
        } else {
            Optional<Lottery> wrappedLottery = lotteryDAO.getById(user.getLotteryId());
            if (wrappedLottery.isPresent()) {
                Lottery lottery = wrappedLottery.get();
                List<User> userList = lottery.getUserList();
                userList.add(user);
                lottery.setUserList(userList);
                lotteryDAO.update(lottery);
                response.setStatus("OK");
            }
        }

        return response;

    }

    private boolean isAdult(User user) {
        return user.getAge() >= 21;
    }

    private boolean hasValidCode(User user) {

        Optional<Lottery> wrappedLottery = lotteryDAO.getById(user.getLotteryId());

        if (wrappedLottery.isPresent()) {

            Lottery lottery = wrappedLottery.get();
            Integer day = lottery.getStartDate().getDay();
            Integer month = lottery.getStartDate().getMonth();
            Integer year = lottery.getStartDate().getYear();

            String validFirstPart = day.toString()
                    + month.toString()
                    + year.toString().substring(year.toString().length()-2)
                    + user.getEmail().length();

            String userCodeFirstPart = user.getCode().toString().substring(0, 7);

            return userCodeFirstPart.equals(validFirstPart);

        }

        return false;
    }

    private boolean codeUnique(User user) {
        //todo
        return false;
    }

}
