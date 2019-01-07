function loadLotteries() {
    fetch("/stats", {
        method: "get",
        // headers: {
        //     'Authorization': 'Basic ' + btoa(localStorage.getItem("currentUsername") + ":" + localStorage.getItem("currentPassword"))
        // }
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
        <td>${lottery.id}</td>
        <td>${lottery.title}</td>
        <td>${lottery.open === true ? "Open" : "Closed"}</td>
        <td>${lottery.userList.length}</td>
        <td>${lottery.limit}</td>
        <td>${lottery.startDate.substring(0, 10)}</td>
        <td>${lottery.endDate === null ? "---" : lottery.endDate.substring(0, 10)}</td>
        <td>${lottery.winnerCode === null ? "---" : lottery.winnerCode}</td>
        <td>
            <button class="btn btn-primary" onclick="closeLottery(${lottery.id})" 
                    ${lottery.open === false ? "disabled" : ""}>Close</button>
            <button class="btn btn-primary" onclick="chooseWinner(${lottery.id})"
                    ${lottery.open === true ? "disabled" : ""} ${lottery.winnerCode === null ? "" : "disabled"}>Choose Winner</button>
        </td>
    `;
    document.getElementById("table-body").appendChild(tr);
}

function closeLottery(id) {
    fetch("/stop-registration/" + id, {
        method: "put",
        // headers: {
        //     'Authorization': 'Basic ' + btoa(localStorage.getItem("currentUsername") + ":" + localStorage.getItem("currentPassword"))
        // }
    })
        .then((resp) => resp.json())
        .then(response => {
            if (response.status === "OK") {
                window.location.reload();
            } else {
                alert("Unable to close lottery. Reason: " + response.reason);
            }
        });
}

function chooseWinner(id) {
    fetch("/choose-winner/" + id, {
        method: "put",
        // headers: {
        //     'Authorization': 'Basic ' + btoa(localStorage.getItem("currentUsername") + ":" + localStorage.getItem("currentPassword"))
        // }
    })
        .then((resp) => resp.json())
        .then(response => {
            if (response.status === "OK") {
                window.location.reload();
            } else {
                alert("Unable to choose winner. Reason: " + response.reason);
            }
        });
}

function openNewPage() {
    window.open("/admin/newLottery.html","_self");
}

function startNewLottery() {
    const title = document.getElementById("title").value;
    const limit = document.getElementById("limit").value;


    fetch("/start-registration", {
        method: "post",
        body: JSON.stringify({
            title: title,
            limit: limit
        }),
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
            // 'Authorization': 'Basic ' + btoa(/*"login_user:VWRYDQE2"*/localStorage.getItem("currentUsername") + ":" + localStorage.getItem("currentPassword"))
        }
    })
        .then((resp) => resp.json())
        .then(response => {
            if (response.status === "OK") {
                window.location.href = "adminTools.html";
            } else {
                alert("Unable to start new lottery. Reason: " + response.reason);
            }
        });
}