package com.hyscalar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyscalar.entity.LeaveBalance;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByEmployeeIdAndMonthAndYear(
            Long employeeId,
            int month,
            int year
    );
}
