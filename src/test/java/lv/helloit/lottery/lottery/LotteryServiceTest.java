package lv.helloit.lottery.lottery;


import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.data.dao.UserDAOImplementation;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.user.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LotteryServiceTest {

    private Long id = 1L;
    private Response response;

    private User userMock = mock(User.class);
    private Lottery lotteryMock = mock(Lottery.class);

    private UserDAOImplementation userDAOImplementationMock = mock(UserDAOImplementation.class);
    private LotteryDAOImplementation lotteryDAOImplementationMock = mock(LotteryDAOImplementation.class);

    private LotteryService lotteryService = new LotteryService(lotteryDAOImplementationMock, userDAOImplementationMock);

    @Test
    public void shouldCreateLottery() {

        response = lotteryService.openRegistration(lotteryMock);
        assertEquals("Fail", response.getStatus());

        when(lotteryDAOImplementationMock.insert(lotteryMock)).thenReturn(id);
        when(lotteryMock.getTitle()).thenReturn("title");
        when(lotteryMock.getLimit()).thenReturn(10);
        response = lotteryService.openRegistration(lotteryMock);
        assertEquals("OK", response.getStatus());

    }

    @Test
    public void shouldCloseLottery() {

        Response response;

        response = lotteryService.closeRegistration(id);
        assertEquals("Fail", response.getStatus());

        when(lotteryDAOImplementationMock.getById(id)).thenReturn(Optional.ofNullable(lotteryMock));
        when(lotteryMock.isOpen()).thenReturn(false);
        response = lotteryService.closeRegistration(id);
        assertEquals("Fail", response.getStatus());

        when(lotteryMock.isOpen()).thenReturn(true);
        response = lotteryService.closeRegistration(id);
        assertEquals("OK", response.getStatus());

    }

    @Test
    public void shouldChooseWinner() {

        List<User> userList = new ArrayList<>();

        response = lotteryService.chooseWinner(id);
        assertEquals("Fail", response.getStatus());

        when(lotteryDAOImplementationMock.getById(id)).thenReturn(Optional.ofNullable(lotteryMock));
        when(lotteryMock.isOpen()).thenReturn(true);
        response = lotteryService.chooseWinner(id);
        assertEquals("Fail", response.getStatus());

        when(lotteryMock.isOpen()).thenReturn(false);
        response = lotteryService.chooseWinner(id);
        assertEquals("Fail", response.getStatus());

        userList.add(userMock);
        when(lotteryMock.getUserList()).thenReturn(userList);
        when(lotteryMock.getWinnerCode()).thenReturn("some_code");
        response = lotteryService.chooseWinner(id);
        assertEquals("Fail", response.getStatus());

        when(lotteryMock.getWinnerCode()).thenReturn(null);
        response = lotteryService.chooseWinner(id);
        assertEquals("OK", response.getStatus());

    }

    @Test
    public void shouldReturnStatus() {

        List<User> userList = new ArrayList<>();
        Long id = 1L;
        String email = "some_email";
        String code = "some_code";

        response = lotteryService.getStatus(id, email, code);
        assertEquals("ERROR", response.getStatus());

        when(lotteryDAOImplementationMock.getById(id)).thenReturn(Optional.ofNullable(lotteryMock));
        response = lotteryService.getStatus(id, email, code);
        assertEquals("ERROR", response.getStatus());

        userList.add(userMock);
        when(lotteryMock.getUserList()).thenReturn(userList);
        when(userMock.getEmail()).thenReturn(email);
        when(userMock.getCode()).thenReturn(code);
        when(lotteryMock.isOpen()).thenReturn(true);
        response = lotteryService.getStatus(id, email, code);
        assertEquals("PENDING", response.getStatus());

        when(lotteryMock.isOpen()).thenReturn(false);
        response = lotteryService.getStatus(id, email, code);
        assertEquals("PENDING", response.getStatus());

        when(lotteryMock.getWinnerCode()).thenReturn(code);
        response = lotteryService.getStatus(id, email, code);
        assertEquals("WIN", response.getStatus());

        when(lotteryMock.getWinnerCode()).thenReturn("another_code");
        response = lotteryService.getStatus(id, email, code);
        assertEquals("LOOSE", response.getStatus());

    }

    @Test
    public void shouldReturnStats() {

        List<Lottery> list = new ArrayList<>();

        assertTrue(lotteryService.getStats().isEmpty());

        list.add(lotteryMock);
        when(lotteryDAOImplementationMock.getAll()).thenReturn(list);
        assertFalse(lotteryService.getStats().isEmpty());
    }

    @Test
    public void shouldDeleteLottery() {

        response = lotteryService.deleteLottery(id);
        assertEquals("Fail", response.getStatus());

        when(lotteryDAOImplementationMock.getById(id)).thenReturn(Optional.ofNullable(lotteryMock));
        response = lotteryService.deleteLottery(id);
        assertEquals("OK", response.getStatus());

    }

}
