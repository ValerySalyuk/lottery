function saveCredentials() {

    localStorage.setItem("currentUsername", document.getElementById("username").value);
    localStorage.setItem("currentPassword", document.getElementById("password").value);

    window.location.href = "/admin/admintools.html";

}

function openRegisterForm() {
    window.location.href = "register.html";
}

function registerAdmin() {
    const login = document.getElementById("login").value;
    const password = document.getElementById("password").value;
    const confirm = document.getElementById("confirm").value;

    if (password === confirm) {
        fetch("/register-admin", {
            method: "post",
            body: JSON.stringify({
                login: login,
                password: password
            }),
            headers: {
                "Content-Type": "application/json;charset=UTF-8",
                // 'Authorization': 'Basic ' + btoa(localStorage.getItem("currentUsername") + ":" + localStorage.getItem("currentPassword"))
            }
        })
            .then((resp) => resp.json())
            .then(response => {
                if (response.status === "OK") {
                    localStorage.setItem("currentUsername", login);
                    localStorage.setItem("currentPassword", password);

                    window.location.href = "/admin/admintools.html";
                } else {
                    alert("Unable to register. Reason: " + response.reason);
                }
            });
    } else {
        alert("Passwords don't match")
    }
}