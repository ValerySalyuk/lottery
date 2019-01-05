package lv.helloit.lottery.response;

public class Response {

    private String status;
    private Long id;
    private Integer winnerCode;
    private String reason;

    public Response() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWinnerCode() {
        return winnerCode;
    }

    public void setWinnerCode(Integer winnerCode) {
        this.winnerCode = winnerCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
