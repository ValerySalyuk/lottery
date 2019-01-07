package lv.helloit.lottery.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lv.helloit.lottery.lottery.Lottery;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Objects;

@Entity
@Table(name = "VS_LOT_USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "age")
    private Byte age;
    @Column(name = "code")
    private String code;
    @ManyToOne
    @JoinColumn(name = "lottery_id")
    @JsonBackReference
    private Lottery lottery;

    private Long lotteryId;

    public User() {
    }

    public User(String email, Byte age, String code, Long lotteryId, Lottery lottery) {
        this.email = email;
        this.age = age;
        this.code = code;
        this.lotteryId = lotteryId;
        this.lottery = lottery;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", code=" + code +
                ", lotteryId=" + lotteryId +
                ", lottery=" + lottery +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(age, user.age) &&
                Objects.equals(code, user.code) &&
                Objects.equals(lotteryId, user.lotteryId) &&
                Objects.equals(lottery, user.lottery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, age, code, lotteryId, lottery);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Lottery getLottery() {
        return lottery;
    }

    public void setLottery(Lottery lottery) {
        this.lottery = lottery;
    }

    public Long getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }
}
