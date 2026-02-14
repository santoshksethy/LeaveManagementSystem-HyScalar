package com.hyscalar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyscalar.entity.Leave;
import com.hyscalar.repository.LeaveRepository;
import com.hyscalar.service.LeaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final LeaveService leaveService;
    private final LeaveRepository leaveRepository;
    
   // private final JavaMailSender mailSender;


    // 1️⃣ Get all pending leaves
    @GetMapping("/pending-leaves")
    public ResponseEntity<List<Leave>> getPendingLeaves() {
        List<Leave> pendingLeaves = leaveRepository.findByStatus("PENDING");
        return ResponseEntity.ok(pendingLeaves);
    }


    // 3️⃣ Reject leave
    @PutMapping("/reject/{id}")
    public ResponseEntity<String> rejectLeave(@PathVariable Long id) {

        leaveService.rejectLeave(id);

        return ResponseEntity.ok("Leave Rejected Successfully");
    }
    
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approveLeave(@PathVariable Long id) {

        leaveService.approveLeave(id);

        return ResponseEntity.ok("Leave Approved Successfully");
    }



    // 4️⃣ Get all leaves (history)
    @GetMapping("/all-leaves")
    public ResponseEntity<List<Leave>> getAllLeaves() {
        return ResponseEntity.ok(leaveRepository.findAll());
    }
}
