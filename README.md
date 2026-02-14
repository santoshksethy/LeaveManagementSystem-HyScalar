Employee Leave Management System
Project Overview
The Employee Leave Management System is a full-stack web application developed to automate
leave request handling in a small organization.
The system enables employees to apply for leaves and managers to review, approve, or reject
them while maintaining accurate monthly leave balances.
Features
1. User Authentication
- Secure login using JWT
- Role-based access (EMPLOYEE / MANAGER)
- Protected REST APIs
2. Leave Request Submission
- Apply for Casual, Sick, or Earned Leave
- Date validation and leave balance validation
3. Leave Approval Workflow
- Managers can approve or reject leave requests
- Dynamic status updates
4. Leave Balance Management
- Monthly leave limits
- Automatic deduction on approval
- Leave restoration on rejection
- Remaining leave displayed on dashboard
5. Leave Calendar
- Monthly calendar view
- Approved leaves highlighted
Tech Stack
Frontend: HTML5, CSS3, JavaScript
Backend: Spring Boot, Spring Security, JWT, JPA/Hibernate
Database: MySQL
Database Tables
- users
- leaves
- leave_balance
System Architecture
1. Presentation Layer (HTML, CSS, JS)
2. Business Logic Layer (Spring Boot)
3. Data Access Layer (JPA + MySQL)
How to Run
1. Clone repository
2. Create MySQL database
3. Configure application.properties
4. Run using mvn spring-boot:run
Future Enhancements
- Email notification system
- Admin dashboard
- Yearly leave summary reports
- PDF export feature
Conclusion
This project demonstrates real-world workflow automation using full-stack technologies and secure
authentication mechanisms.
