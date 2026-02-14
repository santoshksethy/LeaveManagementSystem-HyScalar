package com.hyscalar.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyscalar.config.JwtUtil;
import com.hyscalar.entity.Leave;
import com.hyscalar.entity.LeaveBalance;
import com.hyscalar.entity.User;
import com.hyscalar.repository.LeaveBalanceRepository;
import com.hyscalar.repository.LeaveRepository;
import com.hyscalar.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final LeaveRepository leaveRepository;
    private final UserRepository userRepository;
    private JwtUtil jwtUtil;
    private final LeaveBalanceRepository leaveBalanceRepository;

    @GetMapping("/my-leaves")
    public ResponseEntity<List<Leave>> getMyLeaves(Authentication authentication) {
        String email = authentication.getName(); // email from JWT
        User employee = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Leave> leaves = leaveRepository.findByEmployee(employee);
        return ResponseEntity.ok(leaves);
    }

//    @PostMapping("/apply-leave")
//    public ResponseEntity<Leave> applyLeave(@RequestBody Leave leave, Authentication authentication) {
//        String email = authentication.getName();
//        User employee = userRepository.findByEmailIgnoreCase(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        leave.setEmployee(employee);
//        leave.setStatus("PENDING");
//        Leave saved = leaveRepository.save(leave);
//        return ResponseEntity.ok(saved);
//    }
    
    @GetMapping("/leave-balance")
    public ResponseEntity<LeaveBalanceDTO> getLeaveBalance(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        User employee = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndMonthAndYear(employee.getId(), month, year)
                .orElse(new LeaveBalance()); // or return zeros

        return ResponseEntity.ok(new LeaveBalanceDTO(
                balance.getCasualRemaining(),
                balance.getSickRemaining(),
                balance.getEarnedRemaining()
        ));
    }

    // DTO
    public record LeaveBalanceDTO(int casual, int sick, int earned) {}

}


