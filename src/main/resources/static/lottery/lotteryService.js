function loadLotteries() {
    fetch("/lotteries", {
        method: "get",
    })
        .then(resp => resp.json())
        .then(lotteries => {
            for (const lottery of lotteries) {
            addLottery(lottery);
        }
    });
}

function addLottery(lottery) {
    const tr = document.createElement("tr");
    tr.innerHTML = `
        <td>${lottery.title}</td>
        <td>${lottery.open === true ? "Open" : "Closed"}</td>
        <td>${lottery.startDate.substring(0, 10)}</td>
        <td>${lottery.endDate === null ? "---" : lottery.endDate.substring(0, 10)}</td>
        <td>
            <button class="btn btn-primary" onclick="registerUser(${lottery.id})"
                    ${lottery.open === false ? "disabled" : ""}>Register</button>
            <button class="btn btn-primary" onclick="requestStatus(${lottery.id})"
                    ${lottery.open === true ? "disabled" : ""}>Request status</button>
        </td>
    `;
    document.getElementById("table-body").appendChild(tr);
}

function registerUser(id) {
    window.open("/user/register.html?lotteryId=" + id,"_self");
}

function requestStatus(id) {
    window.open("/user/requestStatus.html?lotteryId=" + id,"_self");
}