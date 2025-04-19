package com.rajharit.rajharitsprings.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rajharit.rajharitsprings.entities.DurationUnit;

import lombok.Data;
import java.time.Instant;

@Data
public class ProcessingTimeDto {
    @JsonProperty("dishIdentifier")
    private int dishId;

    @JsonProperty("dishName")
    private String dishName;

    @JsonProperty("preparationDuration")
    private long preparationDuration;

    @JsonProperty("salesPoint")
    private String salesPoint;

    @JsonProperty("durationUnit")
    private String durationUnit = "SECONDS";
}
