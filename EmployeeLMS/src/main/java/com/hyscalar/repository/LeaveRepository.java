package com.hyscalar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hyscalar.entity.Leave;
import com.hyscalar.entity.User;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    List<Leave> findByEmployeeEmail(String email); // matches User.email
    
    List<Leave> findByEmployee(User employee);

	List<Leave> findByStatus(String status);


	@Query("SELECT COUNT(l) FROM Leave l WHERE l.employee.id = :empId " +
		       "AND l.leaveType = :leaveType " +
		       "AND MONTH(l.fromDate) = :month " +
		       "AND YEAR(l.fromDate) = :year")
		long countLeavesByMonth(Long empId, String leaveType, int month, int year);


}
