package lv.helloit.lottery.lottery;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class LotteryService {

    private final LotteryDAOImplementation lotteryDAOImplementation;

    @Autowired
    public LotteryService(LotteryDAOImplementation lotteryDAOImplementation) {
        this.lotteryDAOImplementation = lotteryDAOImplementation;
    }

    public Response openLottery(Lottery lottery) {

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

}
