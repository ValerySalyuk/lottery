function registerUser() {
    const lotteryId = new URL(window.location.href).searchParams.get("lotteryId");
    const email = document.getElementById("email").value;
    const age = document.getElementById("age").value;
    const code = document.getElementById("code").value;

    fetch("/register", {
        method: "post",
        body: JSON.stringify({
            lotteryId: lotteryId,
            email: email,
            age: age,
            code: code
        }),
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
            // 'Authorization': 'Basic ' + btoa(/*"login_user:VWRYDQE2"*/localStorage.getItem("currentUsername") + ":" + localStorage.getItem("currentPassword"))
        }
    })
        .then((resp) => resp.json())
        .then(response => {
            if (response.status === "OK") {
                window.location.href = "../index.html";
            } else {
                alert("Unable to register. Reason: " + response.reason);
            }
        });
}

function checkStatus() {
    const lotteryId = new URL(window.location.href).searchParams.get("lotteryId");
    const email = document.getElementById("email").value;
    const code = document.getElementById("code").value;


    fetch("/status?id=" + lotteryId + "&email=" + email + "&code=" + code, {
        method: "get",
    })
        .then((resp) => resp.json())
        .then(response => {
            switch (response.status) {
                case "WIN":
                    alert("Your code won! Contact lottery owner.");
                    break;
                case "LOOSE":
                    alert("Unfortunately your code didn't win.");
                    break;
                case "PENDING":
                    alert("Winner selection is in progress. Please be patient.");
                    break;
                case "ERROR":
                    alert("An error occurred. Reason: " + response.reason);
                    break;
            }
        });
}

function goBack() {
    window.location.href = "../index.html";
}