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