package com.cts.trainers_application.service;

import com.cts.trainers_application.entity.ApplicationStatus;
import com.cts.trainers_application.entity.Trainer;
import com.cts.trainers_application.exception.InvalidTrainerDataException;
import com.cts.trainers_application.exception.TrainerAlreadyExistsException;
import com.cts.trainers_application.exception.TrainerNotFoundException;
import com.cts.trainers_application.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrainerService {

    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    // CREATE
    public Trainer createTrainer(Trainer trainer) {
        validateTrainerData(trainer);

        if (trainerRepository.existsByEmail(trainer.getEmail())) {
            throw new TrainerAlreadyExistsException(trainer.getEmail());
        }

        trainer.setApplicationStatus(ApplicationStatus.PENDING);
        return trainerRepository.save(trainer);
    }

    // READ
    @Transactional(readOnly = true)
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Trainer> getAllTrainers(Pageable pageable) {
        return trainerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Trainer getTrainerByEmail(String email) {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with email: " + email));
    }

    // UPDATE
    public Trainer updateTrainer(Long id, Trainer updatedTrainer) {
        Trainer existingTrainer = getTrainerById(id);

        // Check if email is being changed and if new email already exists
        if (!existingTrainer.getEmail().equals(updatedTrainer.getEmail())) {
            if (trainerRepository.existsByEmail(updatedTrainer.getEmail())) {
                throw new TrainerAlreadyExistsException(updatedTrainer.getEmail());
            }
        }

        validateTrainerData(updatedTrainer);

        // Update fields
        existingTrainer.setFirstName(updatedTrainer.getFirstName());
        existingTrainer.setLastName(updatedTrainer.getLastName());
        existingTrainer.setEmail(updatedTrainer.getEmail());
        existingTrainer.setPhoneNumber(updatedTrainer.getPhoneNumber());
        existingTrainer.setDateOfBirth(updatedTrainer.getDateOfBirth());
        existingTrainer.setAddress(updatedTrainer.getAddress());
        existingTrainer.setCity(updatedTrainer.getCity());
        existingTrainer.setState(updatedTrainer.getState());
        existingTrainer.setCountry(updatedTrainer.getCountry());
        existingTrainer.setPostalCode(updatedTrainer.getPostalCode());
        existingTrainer.setHighestQualification(updatedTrainer.getHighestQualification());
        existingTrainer.setYearsOfExperience(updatedTrainer.getYearsOfExperience());
        existingTrainer.setSpecializations(updatedTrainer.getSpecializations());
        existingTrainer.setCertifications(updatedTrainer.getCertifications());
        existingTrainer.setPreviousCompany(updatedTrainer.getPreviousCompany());
        existingTrainer.setAdditionalNotes(updatedTrainer.getAdditionalNotes());
        existingTrainer.setSalaryExpectation(updatedTrainer.getSalaryExpectation());
        existingTrainer.setAvailableForTravel(updatedTrainer.getAvailableForTravel());

        return trainerRepository.save(existingTrainer);
    }

    public Trainer updateApplicationStatus(Long id, ApplicationStatus status) {
        Trainer trainer = getTrainerById(id);
        trainer.setApplicationStatus(status);
        return trainerRepository.save(trainer);
    }

    // DELETE
    public void deleteTrainer(Long id) {
        if (!trainerRepository.existsById(id)) {
            throw new TrainerNotFoundException(id);
        }
        trainerRepository.deleteById(id);
    }

    // SEARCH AND FILTER METHODS USING JPA STREAMS
    @Transactional(readOnly = true)
    public List<Trainer> getTrainersByStatus(ApplicationStatus status) {
        return trainerRepository.findByApplicationStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getExperiencedTrainers(Integer minYears) {
        return trainerRepository.findByYearsOfExperienceGreaterThanEqual(minYears);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getTrainersByCity(String city) {
        return trainerRepository.findByCityIgnoreCase(city);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getTrainersBySpecializations(List<String> specializations) {
        return trainerRepository.findBySpecializationsIn(specializations);
    }

    @Transactional(readOnly = true)
    public List<Trainer> searchTrainersByName(String name) {
        return trainerRepository.findByNameContaining(name);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getTrainersBySalaryRange(Double minSalary, Double maxSalary) {
        return trainerRepository.findBySalaryExpectationBetween(minSalary, maxSalary);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getTrainersAvailableForTravel() {
        return trainerRepository.findByAvailableForTravel(true);
    }

    // ANALYTICS METHODS USING JPA STREAMS
    @Transactional(readOnly = true)
    public Map<ApplicationStatus, Long> getApplicationStatusStatistics() {
        return trainerRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Trainer::getApplicationStatus,
                        Collectors.counting()
                ));
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getCityStatistics() {
        return trainerRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Trainer::getCity,
                        Collectors.counting()
                ));
    }

    @Transactional(readOnly = true)
    public Map<Integer, Long> getExperienceStatistics() {
        return trainerRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        trainer -> trainer.getYearsOfExperience() / 5 * 5, // Group by 5-year ranges
                        Collectors.counting()
                ));
    }

    @Transactional(readOnly = true)
    public List<Trainer> getTopExperiencedTrainers(int limit) {
        return trainerRepository.findAll().stream()
                .sorted((t1, t2) -> Integer.compare(t2.getYearsOfExperience(), t1.getYearsOfExperience()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Trainer> getRecentApplications(int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return trainerRepository.findRecentApplications(cutoffDate);
    }

    @Transactional(readOnly = true)
    public Double getAverageSalaryExpectation() {
        return trainerRepository.findAll().stream()
                .filter(trainer -> trainer.getSalaryExpectation() != null)
                .mapToDouble(Trainer::getSalaryExpectation)
                .average()
                .orElse(0.0);
    }

    @Transactional(readOnly = true)
    public List<String> getAllCities() {
        return trainerRepository.findAllDistinctCities();
    }

    @Transactional(readOnly = true)
    public List<String> getAllStates() {
        return trainerRepository.findAllDistinctStates();
    }

    // VALIDATION
    private void validateTrainerData(Trainer trainer) {
        if (trainer == null) {
            throw new InvalidTrainerDataException("Trainer data cannot be null");
        }

        if (trainer.getDateOfBirth() != null && trainer.getDateOfBirth().isAfter(LocalDate.now().minusYears(18))) {
            throw new InvalidTrainerDataException("Trainer must be at least 18 years old");
        }

        if (trainer.getYearsOfExperience() != null && trainer.getYearsOfExperience() < 0) {
            throw new InvalidTrainerDataException("Years of experience cannot be negative");
        }

        if (trainer.getSalaryExpectation() != null && trainer.getSalaryExpectation() < 0) {
            throw new InvalidTrainerDataException("Salary expectation cannot be negative");
        }
    }
}