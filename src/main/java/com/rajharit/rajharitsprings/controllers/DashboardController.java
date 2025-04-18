package com.rajharit.rajharitsprings.controllers;

import com.rajharit.rajharitsprings.dtos.BestSalesDto;
import com.rajharit.rajharitsprings.dtos.ProcessingTimeDto;
import com.rajharit.rajharitsprings.services.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/bestSales")
    public ResponseEntity<List<BestSalesDto>> getBestSales() {

        List<BestSalesDto> bestSales = dashboardService.getBestSales();
        return ResponseEntity.ok(bestSales);
    }

    /*@GetMapping("/dishes/{dishId}/processingTime")
    public ResponseEntity<ProcessingTimeDto> getDishProcessingTime(
            @PathVariable int dishId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "seconds") String timeUnit,
            @RequestParam(required = false, defaultValue = "average") String calculationType) {

        ProcessingTimeDto processingTime = dashboardService.getDishProcessingTime(
                dishId, startDate, endDate, timeUnit, calculationType);

        return ResponseEntity.ok(processingTime);
    }*/

    @PostMapping("/bestSales")
    public ResponseEntity<Void> calculateBestSales() {
        dashboardService.calculateAndSaveBestSalesData();
        return ResponseEntity.ok().build();
    }

    /*@PostMapping("/dishes/{dishId}/processingTime")
    public ResponseEntity<Void> calculateProcessingTime(
            @PathVariable int dishId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "seconds") String timeUnit,
            @RequestParam(required = false, defaultValue = "average") String calculationType) {

        dashboardService.calculateAndSaveProcessingTimeData(
                dishId, startDate, endDate, timeUnit, calculationType);

        return ResponseEntity.ok().build();
    }*/

    @GetMapping("/preCalculated/bestSales")
    public ResponseEntity<List<BestSalesDto>> getPreCalculatedBestSales() {

        List<BestSalesDto> bestSales = dashboardService.getPreCalculatedBestSales();
        return ResponseEntity.ok(bestSales);
    }

    /*@GetMapping("/dishes/{dishId}/processingTime")
    public ResponseEntity<ProcessingTimeDto> getPreCalculatedProcessingTime(
            @PathVariable int dishId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "seconds") String timeUnit,
            @RequestParam(required = false, defaultValue = "average") String calculationType) {

        ProcessingTimeDto processingTime = dashboardService.getPreCalculatedProcessingTime(
                dishId, startDate, endDate, timeUnit, calculationType);

        return ResponseEntity.ok(processingTime);
    }*/

    @GetMapping("/dbCalculated/bestSales")
    public ResponseEntity<List<BestSalesDto>> getBestSalesFromDB() {

        List<BestSalesDto> bestSales = dashboardService.getBestSalesFromDB();
        return ResponseEntity.ok(bestSales);
    }

    /*@GetMapping("/dbCalculated/dishes/{dishId}/processingTime")
    public ResponseEntity<ProcessingTimeDto> getProcessingTimeFromDB(
            @PathVariable int dishId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "seconds") String timeUnit,
            @RequestParam(required = false, defaultValue = "average") String calculationType) {

        ProcessingTimeDto processingTime = dashboardService.getProcessingTimeFromDB(
                dishId, startDate, endDate, timeUnit, calculationType);

        return ResponseEntity.ok(processingTime);
    }*/
}