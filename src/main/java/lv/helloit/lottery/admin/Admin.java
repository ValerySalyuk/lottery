package lv.helloit.lottery.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "VS_ADMINS")
@Valid
public class Admin {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "login", unique = true)
    @NotBlank
    private String login;
    @Transient
    @NotBlank
    private String password;
    @Column(name = "password_hash")
    @JsonIgnore
    private String passwordHash;

    public Admin() {
    }

    public Admin(@NotBlank String login, String passwordHash) {
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(id, admin.id) &&
                Objects.equals(login, admin.login) &&
                Objects.equals(passwordHash, admin.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, passwordHash);
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
