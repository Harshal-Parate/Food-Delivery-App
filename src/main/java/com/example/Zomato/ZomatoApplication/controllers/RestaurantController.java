package com.example.Zomato.ZomatoApplication.controllers;

import com.example.Zomato.ZomatoApplication.dtos.ItemsDto;
import com.example.Zomato.ZomatoApplication.dtos.RestaurantDto;
import com.example.Zomato.ZomatoApplication.services.RestaurantServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantServiceImpl restaurantService;

    @PostMapping(path = "/create")
    public ResponseEntity<RestaurantDto> addRestaurant(@RequestBody RestaurantDto restaurantDto) {
        RestaurantDto restaurant = restaurantService.createRestaurant(restaurantDto);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurant() {
        List<RestaurantDto> restaurant = restaurantService.getAllRestaurant();
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable(value = "id") Long id) {
        RestaurantDto restaurant = restaurantService.getRestaurant(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @PostMapping(path = "/{restaurantId}/items")
    public ResponseEntity<RestaurantDto> addItemsToRestaurant(@RequestBody List<ItemsDto> itemsDtoList,
                                                              @PathVariable(value = "restaurantId") Long restaurantId) {
        RestaurantDto restaurantDto = restaurantService.addItemsToRestaurant(restaurantId, itemsDtoList);
        return new ResponseEntity<>(restaurantDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{restaurantId}/items")
    public ResponseEntity<RestaurantDto> removeItemsFromRestaurant(@RequestBody List<ItemsDto> itemsDtoList,
                                                                   @PathVariable(value = "restaurantId") Long restaurantId) {
        RestaurantDto restaurantDto = restaurantService.removeItemsFromRestaurant(restaurantId, itemsDtoList);
        return new ResponseEntity<>(restaurantDto, HttpStatus.OK);
    }

    @GetMapping(path = "/rating/{restaurantId}")
    public ResponseEntity<RestaurantDto> getRatingOfRestaurant(@PathVariable(value = "restaurantId") Long restaurantId) {
        RestaurantDto restaurant = restaurantService.getRatingOfRestaurant(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping(path = "/menu/{restaurantId}")
    public ResponseEntity<List<ItemsDto>> getMenu(@PathVariable Long restaurantId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "4") int size,
                                                  @RequestParam(defaultValue = "price") String sortBy) {
        List<ItemsDto> itemsDtoList = restaurantService.getMenu(restaurantId, page, size, sortBy);
        return new ResponseEntity<>(itemsDtoList, HttpStatus.OK);
    }

}