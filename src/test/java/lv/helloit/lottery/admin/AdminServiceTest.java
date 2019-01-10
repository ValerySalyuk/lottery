package lv.helloit.lottery.admin;

import lv.helloit.lottery.data.dao.AdminDAOImplementation;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.security.SecurityProperties;
import org.junit.Test;
import org.springframework.security.core.token.Sha512DigestUtils;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminServiceTest {

    private Long id = 1L;
    private Response response;
    private String login = "some_login";
    private String password = "some_password";

    private Admin adminMock = mock(Admin.class);

    private AdminDAOImplementation adminDAOImplementationMock = mock(AdminDAOImplementation.class);

    private SecurityProperties securityPropertiesMock = mock(SecurityProperties.class);

    private AdminService adminService = new AdminService(adminDAOImplementationMock, securityPropertiesMock);

    @Test
    public void shouldGetByLogin() {

        assertFalse(adminService.getByLogin(login).isPresent());

        when(adminDAOImplementationMock.getByLogin(login)).thenReturn(Optional.ofNullable(adminMock));
        assertTrue(adminService.getByLogin(login).isPresent());
    }

    @Test
    public void shouldAddAdmin() {

        response = adminService.addAdmin(adminMock);
        assertEquals("Fail", response.getStatus());

        when(adminMock.getLogin()).thenReturn(login);
        when(adminMock.getPassword()).thenReturn(password);
        when(adminDAOImplementationMock.getByLogin(login)).thenReturn(Optional.ofNullable(adminMock));
        response = adminService.addAdmin(adminMock);
        assertEquals("Fail", response.getStatus());

        when(adminDAOImplementationMock.getByLogin(login)).thenReturn(Optional.empty());
        response = adminService.addAdmin(adminMock);
        assertEquals("OK", response.getStatus());

    }

    @Test
    public void shouldDeleteAdmin() {

        response = adminService.deleteAdmin(id);
        assertEquals("Fail", response.getStatus());

        when(adminDAOImplementationMock.getById(id)).thenReturn(Optional.ofNullable(adminMock));
        response = adminService.deleteAdmin(id);
        assertEquals("OK", response.getStatus());
    }

    @Test
    public void shouldCheckCredentials() {

        response = adminService.checkCredentials(login, password);
        assertEquals("Fail", response.getStatus());

        when(adminDAOImplementationMock.getByLogin(login)).thenReturn(Optional.ofNullable(adminMock));
        when(securityPropertiesMock.getSalt()).thenReturn("salt");
        String hash = Sha512DigestUtils.shaHex("wrong_password" + securityPropertiesMock.getSalt());
        when(adminMock.getPasswordHash()).thenReturn(hash);
        response = adminService.checkCredentials(login, password);
        assertEquals("Fail", response.getStatus());

        hash = Sha512DigestUtils.shaHex(password + securityPropertiesMock.getSalt());
        when(adminMock.getPasswordHash()).thenReturn(hash);
        response = adminService.checkCredentials(login, password);
        assertEquals("OK", response.getStatus());
    }

}
