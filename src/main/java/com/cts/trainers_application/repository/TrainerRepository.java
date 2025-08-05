package com.cts.trainers_application.repository;


import com.cts.trainers_application.entity.ApplicationStatus;
import com.cts.trainers_application.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    // Custom query methods using JPA Streams
    Optional<Trainer> findByEmail(String email);

    List<Trainer> findByApplicationStatus(ApplicationStatus status);

    List<Trainer> findByYearsOfExperienceGreaterThanEqual(Integer years);

    List<Trainer> findByCityIgnoreCase(String city);

    List<Trainer> findByStateIgnoreCase(String state);

    List<Trainer> findByCountryIgnoreCase(String country);

    List<Trainer> findByAvailableForTravel(Boolean available);

    @Query("SELECT t FROM Trainer t WHERE t.firstName LIKE %:name% OR t.lastName LIKE %:name%")
    List<Trainer> findByNameContaining(@Param("name") String name);

    @Query("SELECT t FROM Trainer t WHERE t.salaryExpectation BETWEEN :minSalary AND :maxSalary")
    List<Trainer> findBySalaryExpectationBetween(@Param("minSalary") Double minSalary, @Param("maxSalary") Double maxSalary);

    @Query("SELECT t FROM Trainer t WHERE t.dateOfBirth BETWEEN :startDate AND :endDate")
    List<Trainer> findByDateOfBirthBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT t FROM Trainer t JOIN t.specializations s WHERE s IN :specializations")
    List<Trainer> findBySpecializationsIn(@Param("specializations") List<String> specializations);

    @Query("SELECT COUNT(t) FROM Trainer t WHERE t.applicationStatus = :status")
    Long countByApplicationStatus(@Param("status") ApplicationStatus status);

    @Query("SELECT t FROM Trainer t WHERE t.createdAt >= :date")
    List<Trainer> findRecentApplications(@Param("date") LocalDate date);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT t.city FROM Trainer t ORDER BY t.city")
    List<String> findAllDistinctCities();

    @Query("SELECT DISTINCT t.state FROM Trainer t ORDER BY t.state")
    List<String> findAllDistinctStates();
}
