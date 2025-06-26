package com.example.Zomato.ZomatoApplication.controllers;

import com.example.Zomato.ZomatoApplication.dtos.DeliveryDto;
import com.example.Zomato.ZomatoApplication.dtos.OrderDto;
import com.example.Zomato.ZomatoApplication.services.impl.DeliveryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/delivery")
public class DeliveryController {

    private final DeliveryServiceImpl deliveryService;

    @GetMapping(path = "/track/{orderId}")
    public ResponseEntity<DeliveryDto> trackDelivery(@PathVariable(value = "orderId") Long orderId) {
        //TODO: To be added
        DeliveryDto deliveryDto = deliveryService.trackDelivery(orderId);
        return new ResponseEntity<>(deliveryDto, HttpStatus.OK);
    }

    @GetMapping(path = "/{driverId}")
    public ResponseEntity<List<DeliveryDto>> getAllDeliveriesOfDrivers(@PathVariable(value = "driverId") Long driverId) {
        List<DeliveryDto> allDeliveriesByDriver = deliveryService.getAllDeliveriesByDriver(driverId);
        return new ResponseEntity<>(allDeliveriesByDriver, HttpStatus.OK);
    }

    @GetMapping(path = "/{deliveryId}")
    public ResponseEntity<DeliveryDto> getDeliveryInfo(@PathVariable(value = "deliveryId") Long deliveryId) {
        DeliveryDto deliveryInfo = deliveryService.getDeliveryInfo(deliveryId);
        return new ResponseEntity<>(deliveryInfo, HttpStatus.OK);
    }

    @PutMapping(path = "/{orderId}/driver/{driverId}")
    public ResponseEntity<OrderDto> assignDriverToOrder(@PathVariable(value = "orderId") Long orderId,
                                                        @PathVariable(value = "driverId") Long driverId) {
        OrderDto orderDto = deliveryService.assignDriverToOrder(driverId, orderId);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PutMapping("/delivery/{id}/status")
    public ResponseEntity<DeliveryDto> updateDeliveryStatus(@PathVariable("id") Long deliveryId,
                                                            @RequestParam("status") String status) {
        DeliveryDto dto = deliveryService.updateDeliveryStatus(deliveryId, status);
        return ResponseEntity.ok(dto);
    }


}
