package lv.helloit.lottery.user;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.data.dao.UserDAOImplementation;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.lottery.LotteryService;
import lv.helloit.lottery.response.Response;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private Long id = 1L;

    private User userMock = mock(User.class);
    private Lottery lotteryMock = mock(Lottery.class);

    private UserDAOImplementation userDAOImplementationMock = mock(UserDAOImplementation.class);
    private LotteryDAOImplementation lotteryDAOImplementationMock = mock(LotteryDAOImplementation.class);

    private UserService userServiceMock = new UserService(userDAOImplementationMock, lotteryDAOImplementationMock);
    private LotteryService lotteryServiceMock = new LotteryService(lotteryDAOImplementationMock, userDAOImplementationMock);

    @Test
    public void shouldRegisterUser() throws ParseException {

        Response response;
        List<User> userList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        when(lotteryDAOImplementationMock.getById(id)).thenReturn(Optional.ofNullable(lotteryMock));
        response = userServiceMock.registerUser(userMock);
        assertEquals("Fail", response.getStatus());

        when(userMock.getLotteryId()).thenReturn(id);
        when(userMock.getLottery()).thenReturn(lotteryMock);
        when(lotteryMock.isOpen()).thenReturn(false);
        response = userServiceMock.registerUser(userMock);
        assertEquals("Fail", response.getStatus());

        when(lotteryMock.isOpen()).thenReturn(true);
        response = userServiceMock.registerUser(userMock);
        assertEquals("Fail", response.getStatus());

        when(userMock.getEmail()).thenReturn("some_email");
        when(userMock.getAge()).thenReturn((byte) 10);
        when(userMock.getCode()).thenReturn("some_code");
        userList.add(userMock);
        when(lotteryMock.getLimit()).thenReturn(1);
        when(lotteryMock.getUserList()).thenReturn(userList);
        response = userServiceMock.registerUser(userMock);
        assertEquals("Fail", response.getStatus());

        when(lotteryMock.getLimit()).thenReturn(10);
        response = userServiceMock.registerUser(userMock);
        assertEquals("Fail", response.getStatus());

        when(userMock.getEmail()).thenReturn("some@mail.com");
        response = userServiceMock.registerUser(userMock);
        assertEquals("Fail", response.getStatus());

        when(userMock.getAge()).thenReturn((byte) 21);
        response = userServiceMock.registerUser(userMock);
        assertEquals("Fail", response.getStatus());

        when(lotteryMock.getStartDate()).thenReturn(format.parse( "2019-01-09" ));
        when(userMock.getCode()).thenReturn("0901191300000001");
        when(lotteryDAOImplementationMock.getById(lotteryMock.getId())).thenReturn(Optional.of(lotteryMock));
        when(userDAOImplementationMock.getAll()).thenReturn(userList);
        response = userServiceMock.registerUser(userMock);
        assertEquals("Fail", response.getStatus());

        when(userDAOImplementationMock.getAll()).thenReturn(new ArrayList<>());
        response = userServiceMock.registerUser(userMock);
        assertEquals("OK", response.getStatus());

    }

}
