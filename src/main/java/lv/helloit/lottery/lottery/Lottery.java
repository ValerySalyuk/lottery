package lv.helloit.lottery.lottery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lv.helloit.lottery.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "VS_LOTTERIES")
public class Lottery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", unique = true)
    private String title;
    @Column(name = "open")
    private boolean open;
    @Column(name = "user_limit")
    private Integer limit;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "winner_code")
    private Integer winnerCode;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lottery")
    @JsonManagedReference
    @JsonIgnore
    private List<User> userList;

    public Lottery() {
    }

    public Lottery(String title, boolean open, Integer limit, Date startDate, Date endDate, Integer winnerCode) {
        this.title = title;
        this.open = open;
        this.limit = limit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.winnerCode = winnerCode;
    }

    @Override
    public String toString() {
        return "Lottery{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", open=" + open +
                ", limit=" + limit +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", winnerCode=" + winnerCode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lottery lottery = (Lottery) o;
        return open == lottery.open &&
                Objects.equals(id, lottery.id) &&
                Objects.equals(title, lottery.title) &&
                Objects.equals(limit, lottery.limit) &&
                Objects.equals(startDate, lottery.startDate) &&
                Objects.equals(endDate, lottery.endDate) &&
                Objects.equals(winnerCode, lottery.winnerCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, open, limit, startDate, endDate, winnerCode);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isOpen() {
        return open;
    }

    public Integer getLimit() {
        return limit;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getWinnerCode() {
        return winnerCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setWinnerCode(Integer winnerCode) {
        this.winnerCode = winnerCode;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
