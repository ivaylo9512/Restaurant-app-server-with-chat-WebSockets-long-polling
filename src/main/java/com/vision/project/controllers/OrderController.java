package com.vision.project.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vision.project.exceptions.NonExistingOrder;
import com.vision.project.models.*;
import com.vision.project.models.specs.DishSpec;
import com.vision.project.services.base.OrderService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/auth/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/findAll")
    public List<Order> findAllNotes(){
        return orderService.findAll();
    }

    @GetMapping(value = "/findAllNotReady")
    public List<Order> findNotReady(){
        return orderService.findAllNotReady();
    }

    @GetMapping(value = "/findById/{id}")
    public Order order(@PathVariable(name = "id") int id) throws IOException {
        return orderService.findById(id);
    }
    @GetMapping(value = "/getMostRecentDate")
    public LocalDateTime getMostRecentDate(){
        return orderService.getMostRecentDate();
    }
    @PostMapping(value = "/create")
    public Order create(@RequestBody Order order) throws ExpiredJwtException{
        return orderService.create(order);
    }

    @PostMapping(value = "/update")
    public Order update(@RequestBody DishSpec dish){
        return orderService.update(dish);
    }
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @PatchMapping("/getUpdates")
    DeferredResult<List<Order>> getUpdates(@RequestBody LocalDateTime dateTime){
        DeferredResult<List<Order>> deferredResult = new DeferredResult<>(1000L,"Time out.");
        UserRequest userRequest = new UserRequest(deferredResult, dateTime);
        deferredResult.onTimeout(() -> orderService.removeUserRequest(userRequest));
        orderService.findMoreRecent(userRequest);
        return deferredResult;
    }

    @ExceptionHandler
    ResponseEntity handleNonExistingOrder(NonExistingOrder e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
