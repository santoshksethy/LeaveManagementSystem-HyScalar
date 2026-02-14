document.getElementById("loginForm").addEventListener("submit", async function(e) {
    e.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const errorDiv = document.getElementById("error");
    errorDiv.innerText = "";

    try {
        const response = await fetch("/auth/authenticate", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username: email, password: password })
        });

        if (!response.ok) {
            const msg = await response.text();
            errorDiv.innerText = msg; // Show error message in browser
            return;
        }

        const data = await response.json();

        // Store JWT and role
        localStorage.setItem("token", data.token);
        localStorage.setItem("role", data.role);

        // Redirect based on role
        if (data.role === "MANAGER") {
            window.location.href = "/manager.html";
        } else {
            window.location.href = "/employee.html";
        }

    } catch (err) {
        console.error(err);
        errorDiv.innerText = "Server not reachable!";
    }
});
