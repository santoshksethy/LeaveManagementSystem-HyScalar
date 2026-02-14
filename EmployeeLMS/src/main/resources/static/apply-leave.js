document.addEventListener("DOMContentLoaded", function() {

    const leaveForm = document.getElementById("leaveForm");

    leaveForm.addEventListener("submit", async function(e) {
        e.preventDefault();

        // Grab values safely
        const leaveType = document.getElementById("leaveType").value;
        const fromDate = document.getElementById("fromDate").value;
        const toDate = document.getElementById("toDate").value;
        const reason = document.getElementById("reason").value;

        const token = localStorage.getItem("token");

        if (!token) {
            alert("You are not logged in!");
            window.location.href = "/index.html";
            return;
        }

        try {
            const res = await fetch("http://localhost:8081/api/leaves/apply", {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ leaveType, fromDate, toDate, reason })
            });

            const text = await res.text();

            if (!res.ok) {
                throw new Error(text || "Failed to apply leave");
            }

            alert(text); // Success message
            leaveForm.reset();

        } catch (err) {
            console.error("Error applying leave:", err);
            alert("Error applying leave: " + err.message);
        }
    });
});
