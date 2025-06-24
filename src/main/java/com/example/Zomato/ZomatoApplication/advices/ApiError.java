package com.example.Zomato.ZomatoApplication.advices;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder(setterPrefix = "set")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private List<String> subErrors;
    private HttpStatus status;
    private String message;

}