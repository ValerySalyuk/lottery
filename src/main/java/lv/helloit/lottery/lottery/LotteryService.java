package lv.helloit.lottery.lottery;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import org.hibernate.SessionFactory;
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

    public Response closeLottery(Long id) {

        Response response = new Response();

        Optional<Lottery> wrappedLottery = lotteryDAOImplementation.getById(id);
        Lottery lottery;

        if (wrappedLottery.isPresent()) {
            lottery = wrappedLottery.get();
            if (!lottery.isOpen()) {
                response.setStatus("Fail");
                response.setReason("Lottery with ID: " + id + " is already stopped");
            } else {
                lottery.setOpen(false);
                lotteryDAOImplementation.update(lottery);
                response.setStatus("OK");
            }
        } else {
            response.setStatus("Fail");
            response.setReason("Lottery with ID: " + id + " does not exist");
        }

        return response;
    }

}
