package lv.helloit.lottery.controller;

import lv.helloit.lottery.admin.Admin;
import lv.helloit.lottery.admin.AdminService;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest {

    @Autowired
    private Controller controller;

    @Autowired
    private AdminService adminService;

    private Lottery lottery = new Lottery();

    private User user1 = new User();
    private User user2 = new User();

    private Admin admin = new Admin();

    private String code;
    private Long lotteryId;
    private Response response;

    private void prepareLottery() {
        lottery.setTitle("Lottery for testing");
        lottery.setLimit(10);

    }

    private void prepareUsers(Long id) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        String year = Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4);

        String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        if ((cal.get(Calendar.MONTH) + 1) < 10) {
            month = "0" + month;
        }

        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        if ((cal.get(Calendar.DAY_OF_MONTH)) < 10) {
            day = "0" + day;
        }
        // Sorry for code duplicating, it's only for test (didn't want to create a new class and implement it)

        code = day + month + year + 13 + "88888888";

        user1.setEmail("some@mail.com");
        user1.setAge((byte) 21);
        user1.setCode(code);
        user1.setLotteryId(id);

        user2.setEmail("some@mail.com");
        user2.setAge((byte) 21);
        user2.setCode(code.replace("88888888", "88888881"));
        user2.setLotteryId(id);

    }

    private void prepareAdmin() {
        admin.setLogin("some_login");
        admin.setPassword("some_password");
    }

    @Test
    public void shouldStartRegistration() {

        response = controller.startRegistration(lottery);
        assertEquals("Fail", response.getStatus());

        prepareLottery();
        response = controller.startRegistration(lottery);
        assertEquals("OK", response.getStatus());

        controller.delete(response.getId());
    }

    @Test
    public void shouldRegisterUser() {

        response = controller.register(user1);
        assertEquals("Fail", response.getStatus());

        prepareLottery();
        response = controller.startRegistration(lottery);
        lotteryId = response.getId();
        prepareUsers(lotteryId);
        response = controller.register(user1);
        assertEquals("OK", response.getStatus());

        controller.delete(lotteryId);
    }

    @Test
    public void shouldStopRegistration() {

        response = controller.stopRegistration(235235363567L);
        assertEquals("Fail", response.getStatus());

        prepareLottery();
        response = controller.startRegistration(lottery);
        lotteryId = response.getId();
        response = controller.stopRegistration(lotteryId);
        assertEquals("OK", response.getStatus());

        controller.delete(lotteryId);
    }

    @Test
    public void shouldChooseWinner() {

        prepareLottery();
        response = controller.startRegistration(lottery);
        lotteryId = response.getId();
        response = controller.chooseWinner(lotteryId);
        assertEquals("Fail", response.getStatus());

        prepareUsers(lotteryId);
        controller.register(user1);
        controller.register(user2);
        response = controller.chooseWinner(lotteryId);
        assertEquals("Fail", response.getStatus());

        controller.stopRegistration(lotteryId);
        response = controller.chooseWinner(lotteryId);
        assertEquals("OK", response.getStatus());

        controller.delete(lotteryId);
    }

    @Test
    public void shouldReturnStatus() {

        prepareLottery();
        response = controller.startRegistration(lottery);
        lotteryId = response.getId();
        prepareUsers(lotteryId);
        response = controller.getStatus(lotteryId, user1.getEmail(), user1.getCode());
        assertEquals("ERROR", response.getStatus());

        controller.register(user1);
        response = controller.getStatus(lotteryId, user1.getEmail(), user1.getCode());
        assertEquals("PENDING", response.getStatus());

        controller.stopRegistration(lotteryId);
        response = controller.getStatus(lotteryId, user1.getEmail(), user1.getCode());
        assertEquals("PENDING", response.getStatus());

        controller.chooseWinner(lotteryId);
        response = controller.getStatus(lotteryId, user1.getEmail(), user1.getCode());
        assertEquals("WIN", response.getStatus());

        controller.delete(lotteryId);
    }

    @Test
    public void shouldDeleteLottery() {

        response = controller.delete(lotteryId);
        assertEquals("Fail", response.getStatus());

        prepareLottery();
        response = controller.startRegistration(lottery);
        lotteryId = response.getId();
        response = controller.delete(lotteryId);
        assertEquals("OK", response.getStatus());
    }

    @Test
    public void shouldAddAdmin() {

        response = controller.addAdmin(admin);
        assertEquals("Fail", response.getStatus());

        prepareAdmin();
        response = controller.addAdmin(admin);
        assertEquals("OK", response.getStatus());

        adminService.deleteAdmin(response.getId());

    }

    @Test
    public void shouldCheckCredentials() {

        Long id;

        response = controller.checkCredentials(admin.getLogin(), admin.getPassword());
        assertEquals("Fail", response.getStatus());

        prepareAdmin();
        response = controller.addAdmin(admin);
        id = response.getId();
        response = controller.checkCredentials(admin.getLogin(), admin.getPassword());
        assertEquals("OK", response.getStatus());

        adminService.deleteAdmin(id);
    }
}
