package com.hyscalar.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyscalar.entity.Leave;
import com.hyscalar.entity.LeaveBalance;
import com.hyscalar.entity.User;
import com.hyscalar.repository.LeaveBalanceRepository;
import com.hyscalar.repository.LeaveRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    @Transactional
    public Leave applyLeave(Leave leave, User employee) {

        int month = leave.getFromDate().getMonthValue();
        int year = leave.getFromDate().getYear();

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndMonthAndYear(employee.getId(), month, year)
                .orElseThrow(() -> new RuntimeException("Leave balance not set for this month"));

        switch (leave.getLeaveType()) {

            case "SICK":
                if (balance.getSickRemaining() <= 0)
                    throw new RuntimeException("No SICK leave remaining");
                break;

            case "CASUAL":
                if (balance.getCasualRemaining() <= 0)
                    throw new RuntimeException("No CASUAL leave remaining");
                break;

            case "EARNED":
                if (balance.getEarnedRemaining() <= 0)
                    throw new RuntimeException("No EARNED leave remaining");
                break;
        }

        leave.setStatus("PENDING");
        leave.setEmployee(employee);

        return leaveRepository.save(leave);
    }

    @Transactional
    public Leave approveLeave(Long leaveId) {

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (!leave.getStatus().equals("PENDING")) {
            throw new RuntimeException("Only pending leaves can be approved");
        }

        int month = leave.getFromDate().getMonthValue();
        int year = leave.getFromDate().getYear();

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndMonthAndYear(
                        leave.getEmployee().getId(),
                        month,
                        year
                ).orElseThrow(() -> new RuntimeException("Leave balance not found"));

        switch (leave.getLeaveType()) {
            case "SICK":
                balance.setSickRemaining(balance.getSickRemaining() - 1);
                break;
            case "CASUAL":
                balance.setCasualRemaining(balance.getCasualRemaining() - 1);
                break;
            case "EARNED":
                balance.setEarnedRemaining(balance.getEarnedRemaining() - 1);
                break;
        }

        leave.setStatus("APPROVED");

        leaveBalanceRepository.save(balance);
        return leaveRepository.save(leave);
    }

    
    @Transactional
    public Leave rejectLeave(Long leaveId) {

        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (!leave.getStatus().equals("PENDING")) {
            throw new RuntimeException("Only pending leaves can be rejected");
        }

        leave.setStatus("REJECTED");

        return leaveRepository.save(leave);
    }

}
