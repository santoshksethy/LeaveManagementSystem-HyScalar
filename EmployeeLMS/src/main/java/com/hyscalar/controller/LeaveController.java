package com.hyscalar.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyscalar.config.JwtUtil;
import com.hyscalar.entity.Leave;
import com.hyscalar.entity.User;
import com.hyscalar.repository.UserRepository;
import com.hyscalar.service.LeaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

	private final LeaveService leaveService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;



    @PostMapping("/apply")
    public ResponseEntity<?> applyLeave(
            @RequestBody LeaveRequest leaveRequest,
            @RequestHeader("Authorization") String tokenHeader) {

        // Remove "Bearer "
        String token = tokenHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        User employee = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Leave leave = new Leave();
        leave.setFromDate(leaveRequest.getFromDate());
        leave.setToDate(leaveRequest.getToDate());
        leave.setReason(leaveRequest.getReason());
        leave.setLeaveType(leaveRequest.getLeaveType());
        leave.setStatus("PENDING");
        leave.setEmployee(employee);

        leaveService.applyLeave(leave, employee);




//        
//        User manager = employee.getManager();
//        if (manager != null && manager.getEmail() != null) {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo("works.sethy@gmail.com");
//            message.setSubject("Leave Application: " + employee.getName());
//            message.setText(
//                "Employee " + employee.getName() + " has applied for leave.\n" +
//                "Type: " + leave.getLeaveType() + "\n" +
//                "From: " + leave.getFromDate() + "\n" +
//                "To: " + leave.getToDate() + "\n" +
//                "Reason: " + leave.getReason() + "\n" +
//                "Please review the leave request."
//            );
//            mailSender.send(message);
//        }

        
        return ResponseEntity.ok("Leave applied successfully!");
    }


    // DTO class to accept form data (no email needed)
    public static class LeaveRequest {
        private String leaveType;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String reason;

        public String getLeaveType() { return leaveType; }
        public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

        public LocalDate getFromDate() { return fromDate; }
        public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

        public LocalDate getToDate() { return toDate; }
        public void setToDate(LocalDate toDate) { this.toDate = toDate; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}
