package lv.helloit.lottery.admin;

import lv.helloit.lottery.data.dao.AdminDAOImplementation;
import lv.helloit.lottery.response.Response;
import lv.helloit.lottery.security.SecurityProperties;
import lv.helloit.lottery.validator.Validator;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
    private final AdminDAOImplementation adminDAOImplementation;
    private final SecurityProperties securityProperties;

    @Autowired
    public AdminService(AdminDAOImplementation adminDAOImplementation, SecurityProperties securityProperties) {
        this.adminDAOImplementation = adminDAOImplementation;
        this.securityProperties = securityProperties;
    }

    public Optional<Admin> getByLogin(String login) {
        return adminDAOImplementation.getByLogin(login);
    }

    public Response addAdmin(Admin admin) {

        Long id;
        Response response = new Response();
        response.setStatus("Fail");

        if (!Validator.adminHasRequiredData(admin)) {
            response.setReason("Please provide all required data");
        } else if (Validator.adminLoginExists(admin, adminDAOImplementation)) {
            response.setReason("This user name is already in use");
        } else {
            String password = admin.getPassword();
            LOGGER.info("Password: " + password);
            String passwordHash = generatePasswordHash(password);
            LOGGER.info("Password hash: " + passwordHash);
            admin.setPasswordHash(passwordHash);
            id = adminDAOImplementation.insert(admin);
            response.setStatus("OK");
            response.setId(id);
        }

        return response;

    }

    private String generatePasswordHash(String password) {
        return Sha512DigestUtils.shaHex(password + securityProperties.getSalt());
    }

    public Response deleteAdmin(Long id) {

        Response response = new Response();
        response.setStatus("Fail");

        if (adminDAOImplementation.getById(id).isPresent()) {
            adminDAOImplementation.delete(id);
            response.setStatus("OK");
        } else {
            response.setReason("Admin with ID: " + id + " does not exist");
        }

        return response;
    }

    public Response checkCredentials(String login, String password) {

        Response response = new Response();
        response.setStatus("Fail");

        Optional<Admin> wrappedAdmin = adminDAOImplementation.getByLogin(login);

        if (!wrappedAdmin.isPresent()) {
            response.setReason("Login does not exist");
        } else {
            String realPasswordHash = wrappedAdmin.get().getPasswordHash();
            String incomingPasswordHash = generatePasswordHash(password);

            if (realPasswordHash.equals(incomingPasswordHash)) {
                response.setStatus("OK");
            } else {
                response.setReason("Wrong password");
            }
        }

        return response;
    }

}
