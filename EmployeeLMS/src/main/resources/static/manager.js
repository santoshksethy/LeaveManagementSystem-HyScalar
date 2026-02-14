// Get token from localStorage
const token = localStorage.getItem("token");

// Redirect if no token
if (!token) {
    console.warn("JWT missing. Redirecting to login page.");
    window.location.href = "/index.html";
}

// Wait for DOM to load
document.addEventListener("DOMContentLoaded", () => {
    loadPendingLeaves();
});

// Fetch and display pending leaves
async function loadPendingLeaves() {
    try {
        const res = await fetch("http://localhost:8081/api/manager/pending-leaves", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        });

        console.log("Status:", res.status);

        if (!res.ok) throw new Error("Failed to fetch pending leaves. Status: " + res.status);

        const data = await res.json();
        console.log("Pending Leaves:", data);

        const tableBody = document.querySelector("#leaveTable tbody");
        if (!tableBody) throw new Error("Table body not found in HTML");

        tableBody.innerHTML = "";

        // Loop through leaves
        data.forEach(leave => {
            tableBody.innerHTML += `
                <tr>
                    <td>${leave.employee?.name || leave.employeeName || ""}</td>
                    <td>${leave.leaveType || leave.type || ""}</td>
                    <td>${leave.fromDate || leave.from_date || ""}</td>
                    <td>${leave.toDate || leave.to_date || ""}</td>
                    <td>${leave.reason || ""}</td>
                    <td>
                        <button class="approve" onclick="approve(${leave.id})">Approve</button>
                        <button class="reject" onclick="reject(${leave.id})">Reject</button>
                    </td>
                </tr>
            `;
        });

    } catch (err) {
        console.error("Error fetching pending leaves:", err);
        alert("Error fetching pending leaves. See console.");
    }
}

// Approve leave
async function approve(id) {
    try {
        const res = await fetch(`http://localhost:8081/api/manager/approve/${id}`, {
            method: "PUT",
            headers: { "Authorization": "Bearer " + token }
        });

        if (!res.ok) throw new Error("Failed to approve leave");

        alert("Leave approved!");
        loadPendingLeaves();
    } catch (err) {
        console.error(err);
        alert("Failed to approve leave. See console.");
    }
}

// Reject leave
async function reject(id) {
    try {
        const res = await fetch(`http://localhost:8081/api/manager/reject/${id}`, {
            method: "PUT",
            headers: { "Authorization": "Bearer " + token }
        });

        if (!res.ok) throw new Error("Failed to reject leave");

        alert("Leave rejected!");
        loadPendingLeaves();
    } catch (err) {
        console.error(err);
        alert("Failed to reject leave. See console.");
    }
}

// Logout
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    window.location.href = "/index.html";
}

// Profile placeholder
function viewProfile() {
    alert("Profile page coming soon");
}

// Load all leaves
async function loadAllLeaves() {
    try {
        const res = await fetch("http://localhost:8081/api/manager/all-leaves", {
            headers: { "Authorization": "Bearer " + token }
        });

        if (!res.ok) throw new Error("Failed to fetch all leaves.");

        const data = await res.json();
        
        // 1. Change the Title
        document.getElementById("tableTitle").innerText = "All Leave History";

        // 2. Change the Button to a "Back" button
        document.getElementById("buttonContainer").innerHTML = 
            `<button onclick="backToPending()" style="background:#7f8c8d; color:white;">← Back to Pending</button>`;

        const tableBody = document.querySelector("#leaveTable tbody");
        tableBody.innerHTML = "";

        data.forEach(leave => {
            tableBody.innerHTML += `
                <tr>
                    <td>${leave.employee?.name || leave.employeeName || ""}</td>
                    <td>${leave.leaveType || ""}</td>
                    <td>${leave.fromDate || ""}</td>
                    <td>${leave.toDate || ""}</td>
                    <td>${leave.reason || ""}</td>
                    <td><strong>${leave.status || "N/A"}</strong></td>
                </tr>
            `;
        });
    } catch (err) {
        console.error(err);
    }
}
function backToPending() {
    // 1. Reset Title
    document.getElementById("tableTitle").innerText = "Pending Leave Requests";

    // 2. Restore the "View All" button
    document.getElementById("buttonContainer").innerHTML = 
        `<button onclick="loadAllLeaves()">View All Leaves</button>`;

    // 3. Reload the pending leaves
    loadPendingLeaves();
}