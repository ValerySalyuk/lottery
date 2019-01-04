package lv.helloit.lottery.lottery;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "VS_LOTTERY")
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

    public Lottery() {
    }

    public Lottery(String title, boolean open, Integer limit, Date startDate, Date endDate) {
        this.title = title;
        this.open = open;
        this.limit = limit;
        this.startDate = startDate;
        this.endDate = endDate;
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
                Objects.equals(endDate, lottery.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, open, limit, startDate, endDate);
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
}
