package com.cts.trainers_application.controller;

import com.cts.trainers_application.entity.ApplicationStatus;
import com.cts.trainers_application.entity.Trainer;
import com.cts.trainers_application.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trainers")
@CrossOrigin(origins = "*")
public class TrainerController {

    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    // CREATE - POST /api/trainers
    @PostMapping
    public ResponseEntity<ApiResponse<Trainer>> createTrainer(@Valid @RequestBody Trainer trainer) {
        Trainer createdTrainer = trainerService.createTrainer(trainer);
        ApiResponse<Trainer> response = new ApiResponse<>(
                true,
                "Trainer application submitted successfully",
                createdTrainer
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ - GET /api/trainers
    @GetMapping
    public ResponseEntity<ApiResponse<List<Trainer>>> getAllTrainers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Trainer> trainersPage = trainerService.getAllTrainers(pageable);

        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Trainers retrieved successfully",
                trainersPage.getContent(),
                Map.of(
                        "totalElements", trainersPage.getTotalElements(),
                        "totalPages", trainersPage.getTotalPages(),
                        "currentPage", trainersPage.getNumber(),
                        "size", trainersPage.getSize()
                )
        );

        return ResponseEntity.ok(response);
    }

    // READ - GET /api/trainers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Trainer>> getTrainerById(@PathVariable Long id) {
        Trainer trainer = trainerService.getTrainerById(id);
        ApiResponse<Trainer> response = new ApiResponse<>(
                true,
                "Trainer retrieved successfully",
                trainer
        );
        return ResponseEntity.ok(response);
    }

    // READ - GET /api/trainers/email/{email}
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<Trainer>> getTrainerByEmail(@PathVariable String email) {
        Trainer trainer = trainerService.getTrainerByEmail(email);
        ApiResponse<Trainer> response = new ApiResponse<>(
                true,
                "Trainer retrieved successfully",
                trainer
        );
        return ResponseEntity.ok(response);
    }

    // UPDATE - PUT /api/trainers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Trainer>> updateTrainer(
            @PathVariable Long id,
            @Valid @RequestBody Trainer trainer) {
        Trainer updatedTrainer = trainerService.updateTrainer(id, trainer);
        ApiResponse<Trainer> response = new ApiResponse<>(
                true,
                "Trainer updated successfully",
                updatedTrainer
        );
        return ResponseEntity.ok(response);
    }

    // UPDATE - PATCH /api/trainers/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Trainer>> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {
        Trainer updatedTrainer = trainerService.updateApplicationStatus(id, status);
        ApiResponse<Trainer> response = new ApiResponse<>(
                true,
                "Application status updated successfully",
                updatedTrainer
        );
        return ResponseEntity.ok(response);
    }

    // DELETE - DELETE /api/trainers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Trainer deleted successfully",
                "Trainer with ID " + id + " has been deleted"
        );
        return ResponseEntity.ok(response);
    }

    // SEARCH AND FILTER ENDPOINTS

    // GET /api/trainers/search/by-status
    @GetMapping("/search/by-status")
    public ResponseEntity<ApiResponse<List<Trainer>>> getTrainersByStatus(
            @RequestParam ApplicationStatus status) {
        List<Trainer> trainers = trainerService.getTrainersByStatus(status);
        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Trainers filtered by status successfully",
                trainers
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/search/experienced
    @GetMapping("/search/experienced")
    public ResponseEntity<ApiResponse<List<Trainer>>> getExperiencedTrainers(
            @RequestParam(defaultValue = "5") Integer minYears) {
        List<Trainer> trainers = trainerService.getExperiencedTrainers(minYears);
        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Experienced trainers retrieved successfully",
                trainers
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/search/by-city
    @GetMapping("/search/by-city")
    public ResponseEntity<ApiResponse<List<Trainer>>> getTrainersByCity(
            @RequestParam String city) {
        List<Trainer> trainers = trainerService.getTrainersByCity(city);
        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Trainers filtered by city successfully",
                trainers
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/search/by-name
    @GetMapping("/search/by-name")
    public ResponseEntity<ApiResponse<List<Trainer>>> searchTrainersByName(
            @RequestParam String name) {
        List<Trainer> trainers = trainerService.searchTrainersByName(name);
        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Trainers searched by name successfully",
                trainers
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/search/by-specializations
    @GetMapping("/search/by-specializations")
    public ResponseEntity<ApiResponse<List<Trainer>>> getTrainersBySpecializations(
            @RequestParam List<String> specializations) {
        List<Trainer> trainers = trainerService.getTrainersBySpecializations(specializations);
        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Trainers filtered by specializations successfully",
                trainers
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/search/by-salary-range
    @GetMapping("/search/by-salary-range")
    public ResponseEntity<ApiResponse<List<Trainer>>> getTrainersBySalaryRange(
            @RequestParam Double minSalary,
            @RequestParam Double maxSalary) {
        List<Trainer> trainers = trainerService.getTrainersBySalaryRange(minSalary, maxSalary);
        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Trainers filtered by salary range successfully",
                trainers
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/search/available-for-travel
    @GetMapping("/search/available-for-travel")
    public ResponseEntity<ApiResponse<List<Trainer>>> getTrainersAvailableForTravel() {
        List<Trainer> trainers = trainerService.getTrainersAvailableForTravel();
        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Trainers available for travel retrieved successfully",
                trainers
        );
        return ResponseEntity.ok(response);
    }

    // ANALYTICS ENDPOINTS

    // GET /api/trainers/analytics/status-statistics
    @GetMapping("/analytics/status-statistics")
    public ResponseEntity<ApiResponse<Map<ApplicationStatus, Long>>> getApplicationStatusStatistics() {
        Map<ApplicationStatus, Long> statistics = trainerService.getApplicationStatusStatistics();
        ApiResponse<Map<ApplicationStatus, Long>> response = new ApiResponse<>(
                true,
                "Application status statistics retrieved successfully",
                statistics
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/analytics/city-statistics
    @GetMapping("/analytics/city-statistics")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getCityStatistics() {
        Map<String, Long> statistics = trainerService.getCityStatistics();
        ApiResponse<Map<String, Long>> response = new ApiResponse<>(
                true,
                "City statistics retrieved successfully",
                statistics
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/analytics/experience-statistics
    @GetMapping("/analytics/experience-statistics")
    public ResponseEntity<ApiResponse<Map<Integer, Long>>> getExperienceStatistics() {
        Map<Integer, Long> statistics = trainerService.getExperienceStatistics();
        ApiResponse<Map<Integer, Long>> response = new ApiResponse<>(
                true,
                "Experience statistics retrieved successfully",
                statistics
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/analytics/top-experienced
    @GetMapping("/analytics/top-experienced")
    public ResponseEntity<ApiResponse<List<Trainer>>> getTopExperiencedTrainers(
            @RequestParam(defaultValue = "10") int limit) {
        List<Trainer> trainers = trainerService.getTopExperiencedTrainers(limit);
        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Top experienced trainers retrieved successfully",
                trainers
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/analytics/recent-applications
    @GetMapping("/analytics/recent-applications")
    public ResponseEntity<ApiResponse<List<Trainer>>> getRecentApplications(
            @RequestParam(defaultValue = "30") int days) {
        List<Trainer> trainers = trainerService.getRecentApplications(days);
        ApiResponse<List<Trainer>> response = new ApiResponse<>(
                true,
                "Recent applications retrieved successfully",
                trainers
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/analytics/average-salary
    @GetMapping("/analytics/average-salary")
    public ResponseEntity<ApiResponse<Double>> getAverageSalaryExpectation() {
        Double averageSalary = trainerService.getAverageSalaryExpectation();
        ApiResponse<Double> response = new ApiResponse<>(
                true,
                "Average salary expectation retrieved successfully",
                averageSalary
        );
        return ResponseEntity.ok(response);
    }

    // UTILITY ENDPOINTS

    // GET /api/trainers/cities
    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<List<String>>> getAllCities() {
        List<String> cities = trainerService.getAllCities();
        ApiResponse<List<String>> response = new ApiResponse<>(
                true,
                "All cities retrieved successfully",
                cities
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/trainers/states
    @GetMapping("/states")
    public ResponseEntity<ApiResponse<List<String>>> getAllStates() {
        List<String> states = trainerService.getAllStates();
        ApiResponse<List<String>> response = new ApiResponse<>(
                true,
                "All states retrieved successfully",
                states
        );
        return ResponseEntity.ok(response);
    }

    // API Response Wrapper Class
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        private Map<String, Object> metadata;

        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public ApiResponse(boolean success, String message, T data, Map<String, Object> metadata) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.metadata = metadata;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }
    @GetMapping("/greet")
    public String greet() {
        return "Hello, World!";
    }
}