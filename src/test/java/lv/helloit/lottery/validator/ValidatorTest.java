package lv.helloit.lottery.validator;

import lv.helloit.lottery.data.dao.LotteryDAOImplementation;
import lv.helloit.lottery.data.dao.UserDAOImplementation;
import lv.helloit.lottery.lottery.Lottery;
import lv.helloit.lottery.user.User;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ValidatorTest {

    private Long id = 1L;
    private Lottery lotteryMock = mock(Lottery.class);
    private User userMock = mock(User.class);
    private LotteryDAOImplementation lotteryDAOImplementationMock = mock(LotteryDAOImplementation.class);
    private UserDAOImplementation userDAOImplementationMock = mock(UserDAOImplementation.class);

    @Test
    public void shouldCheckUserRequiredData() {

        assertFalse(Validator.userHasRequiredData(userMock));

        when(userMock.getEmail()).thenReturn("some_email");
        assertFalse(Validator.userHasRequiredData(userMock));

        when(userMock.getAge()).thenReturn((byte) 10);
        assertFalse(Validator.userHasRequiredData(userMock));

        when(userMock.getCode()).thenReturn("some_code");
        when(userMock.getEmail()).thenReturn("");
        assertFalse(Validator.userHasRequiredData(userMock));

        when(userMock.getEmail()).thenReturn("some_email");
        assertTrue(Validator.userHasRequiredData(userMock));
    }

    @Test
    public void shouldCheckIfUserIsAdult() {

        when(userMock.getAge()).thenReturn((byte) 10);
        assertFalse(Validator.userIsAdult(userMock));

        when(userMock.getAge()).thenReturn((byte) 21);
        assertTrue(Validator.userIsAdult(userMock));
    }

    @Test
    public void shouldCheckUserLimit() {

        List<User> userList = new ArrayList<>();

        userList.add(userMock);
        when(lotteryMock.getUserList()).thenReturn(userList);
        when(lotteryMock.getLimit()).thenReturn(20);
        assertFalse(Validator.userLimitReached(lotteryMock));

        when(lotteryMock.getLimit()).thenReturn(1);
        assertTrue(Validator.userLimitReached(lotteryMock));
    }

    @Test
    public void shouldValidateEmail() {

        assertFalse(Validator.emailValid("some.mail"));

        assertFalse(Validator.emailValid("some.mail@com"));

        assertTrue(Validator.emailValid("some@mail.com"));
    }

    @Test
    public void shouldValidateUserCode() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        when(userMock.getCode()).thenReturn("0901191300000000");
        when(userMock.getEmail()).thenReturn("some@mail.com");
        when(lotteryMock.getId()).thenReturn(id);
        when(userMock.getLottery()).thenReturn(lotteryMock);
        when(lotteryDAOImplementationMock.getById(lotteryMock.getId())).thenReturn(Optional.of(lotteryMock));
        when(lotteryMock.getStartDate()).thenReturn(format.parse( "2019-01-09" ));
        assertTrue(Validator.userHasValidCode(userMock, lotteryDAOImplementationMock));

        when(userMock.getCode()).thenReturn("090119130000000");
        assertFalse(Validator.userHasValidCode(userMock, lotteryDAOImplementationMock));

        when(userMock.getCode()).thenReturn("090119130000000a");
        assertFalse(Validator.userHasValidCode(userMock, lotteryDAOImplementationMock));

        when(userMock.getCode()).thenReturn("0701191300000000");
        assertFalse(Validator.userHasValidCode(userMock, lotteryDAOImplementationMock));
    }

    @Test
    public void shouldCheckIfCodeIsUnique() {

        List<User> userList = new ArrayList<>();
        when(userDAOImplementationMock.getAll()).thenReturn(userList);
        when(userMock.getCode()).thenReturn("some_code");
        assertTrue(Validator.codeUnique(userMock, userDAOImplementationMock));

        userList.add(userMock);
        assertFalse(Validator.codeUnique(userMock, userDAOImplementationMock));
    }

    @Test
    public void shouldCheckLotteryRequiredData() {

        assertFalse(Validator.lotteryHasRequiredData(lotteryMock));

        when(lotteryMock.getLimit()).thenReturn(10);
        assertFalse(Validator.lotteryHasRequiredData(lotteryMock));

        when(lotteryMock.getTitle()).thenReturn("");
        assertFalse(Validator.lotteryHasRequiredData(lotteryMock));

        when(lotteryMock.getTitle()).thenReturn("title");
        assertTrue(Validator.lotteryHasRequiredData(lotteryMock));
    }

    @Test
    public void shouldCheckIfLotteryExists() {

        assertFalse(Validator.lotteryExists(id, lotteryDAOImplementationMock));

        when(lotteryDAOImplementationMock.getById(id)).thenReturn(Optional.ofNullable(lotteryMock));
        assertTrue(Validator.lotteryExists(id, lotteryDAOImplementationMock));

    }

    @Test
    public void shouldCheckIfUserExistsInLottery() {

        String email = "some_mail";
        String code = "some_code";
        List<User> userList = new ArrayList<>();

        when(lotteryDAOImplementationMock.getById(id)).thenReturn(Optional.ofNullable(lotteryMock));
        assertFalse(Validator.userInLotteryExists(id, email, code, lotteryDAOImplementationMock));

        when(lotteryMock.getUserList()).thenReturn(userList);
        assertFalse(Validator.userInLotteryExists(id, email, code, lotteryDAOImplementationMock));

        when(userMock.getEmail()).thenReturn(email);
        when(userMock.getCode()).thenReturn(code);
        userList.add(userMock);
        when(lotteryMock.getUserList()).thenReturn(userList);
        assertTrue(Validator.userInLotteryExists(id, email, code, lotteryDAOImplementationMock));

    }

    @Test
    public void shouldCheckIfLotteryHasUsers() {

        List<User> userList = new ArrayList<>();

        assertFalse(Validator.lotteryHasUsers(lotteryMock));

        userList.add(userMock);
        when(lotteryMock.getUserList()).thenReturn(userList);
        assertTrue(Validator.lotteryHasUsers(lotteryMock));
    }

    @Test
    public void shouldCheckIfLotteryHasWinner() {

        assertFalse(Validator.lotteryHasWinner(lotteryMock));

        when(lotteryMock.getWinnerCode()).thenReturn("some_code");
        assertTrue(Validator.lotteryHasWinner(lotteryMock));
    }

}
