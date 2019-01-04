package lv.helloit.lottery.lottery;

import lv.helloit.lottery.lottery.dao.LotteryDAOImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LotteryService {

    private final LotteryDAOImplementation lotteryDAOImplementation;

    @Autowired
    public LotteryService(LotteryDAOImplementation lotteryDAOImplementation) {
        this.lotteryDAOImplementation = lotteryDAOImplementation;
    }

    public Long openLottery(Lottery lottery) {

        lottery.setStartDate(new Date());
        lottery.setOpen(true);

        return lotteryDAOImplementation.insert(lottery);

    }

}
