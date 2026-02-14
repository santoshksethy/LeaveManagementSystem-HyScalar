const calendar = document.getElementById("calendar");
const monthYear = document.getElementById("monthYear");

let currentDate = new Date();
let leaves = []; // Will contain all leaves (approved & pending)

document.addEventListener("DOMContentLoaded", async function() {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token || role !== "EMPLOYEE") {
        window.location.href = "/index.html";
        return;
    }

    const username = parseJwt(token).name;
    document.getElementById("welcomeText").innerText = "Welcome, " + username;

    await fetchLeaves();
    renderCalendar();
});


document.addEventListener("DOMContentLoaded", async function() {
    await fetchLeaveBalance();
});

async function fetchLeaves() {
    try {
        const response = await fetch("http://localhost:8081/api/employee/my-leaves", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token"),
                "Content-Type": "application/json"
            }
        });

        const text = await response.text(); // read raw response
        try {
            leaves = JSON.parse(text); // try parsing JSON
        } catch (jsonErr) {
            console.error("Failed to parse JSON. Raw response:", text);
            throw jsonErr;
        }

        console.log("Leaves from DB:", leaves);

    } catch(err) {
       // console.error("Error fetching leaves:", err);
        alert("Failed to fetch leaves. Check console.");
    }
}

function renderCalendar() {
    const calendarDiv = document.getElementById("calendar");
    calendarDiv.innerHTML = "";

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();

    monthYear.innerText = `${currentDate.toLocaleString('default', { month: 'long' })} ${year}`;

    // Empty cells before the 1st day
    for (let i = 0; i < firstDay; i++) {
        const emptyDiv = document.createElement("div");
        emptyDiv.className = "day";
        calendarDiv.appendChild(emptyDiv);
    }

    // Calendar days
    for (let day = 1; day <= lastDate; day++) {
        const current = new Date(year, month, day);
        const dayOfWeek = current.getDay(); // 6 = Sun, 7 = Sat
        const dateString = `${year}-${String(month+1).padStart(2,'0')}-${String(day).padStart(2,'0')}`;

        let className = "day";

        // Check leave status
        let leaveStatus = null;
        leaves.forEach(leave => {
            const from = new Date(leave.fromDate || leave.from_date);
            const to = new Date(leave.toDate || leave.to_date);
            if(current >= from && current <= to) leaveStatus = leave.status;
        });

        if (leaveStatus === "APPROVED") className += " approved-leave";
        else if (leaveStatus === "PENDING") className += " pending-leave";
        else if (dayOfWeek === 5 || dayOfWeek === 6) className += " weekend";

        const dayDiv = document.createElement("div");
        dayDiv.className = className;
        dayDiv.innerText = day;
        calendarDiv.appendChild(dayDiv);
    }
}


function prevMonth() {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar();
}

function nextMonth() {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar();
}

function goToApplyLeave() {
    window.location.href = "/apply-leave.html";
}

function logout() {
    localStorage.clear();
    window.location.href = "/index.html";
}

function parseJwt(token) {
    return JSON.parse(atob(token.split('.')[1]));
}
