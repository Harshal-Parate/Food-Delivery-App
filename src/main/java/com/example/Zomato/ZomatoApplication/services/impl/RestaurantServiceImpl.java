package com.example.Zomato.ZomatoApplication.services.impl;

import com.example.Zomato.ZomatoApplication.dtos.ItemsDto;
import com.example.Zomato.ZomatoApplication.dtos.RestaurantDto;
import com.example.Zomato.ZomatoApplication.entites.ItemsEntity;
import com.example.Zomato.ZomatoApplication.entites.RestaurantEntity;
import com.example.Zomato.ZomatoApplication.repositories.ItemRepository;
import com.example.Zomato.ZomatoApplication.repositories.RestaurantRepository;
import com.example.Zomato.ZomatoApplication.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper mapper;
    private final ItemRepository itemRepository;

    @Override
    public RestaurantDto createRestaurant(RestaurantDto restaurantDto) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findByName(restaurantDto.getName());
        if (restaurantEntity.isPresent()) {
            throw new RuntimeException("Restaurant by the ID: " + restaurantEntity.get().getName() + " already present.");
        }
        RestaurantEntity savedRestaurant = restaurantRepository.save(mapper.map(restaurantDto, RestaurantEntity.class));
        return mapper.map(savedRestaurant, RestaurantDto.class);
    }

    @Override
    public RestaurantDto getRestaurant(Long restaurantId) {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(restaurantId);
        if (restaurantEntity.isPresent()) {
            return mapper.map(restaurantEntity.get(), RestaurantDto.class);
        } else {
            throw new RuntimeException("Restaurant by the ID: " + restaurantId + " is not present.");
        }
    }

    @Override
    public List<RestaurantDto> getAllRestaurant() {
        return restaurantRepository.findAll()
                .stream()
                .map(restaurantEntity -> mapper.map(restaurantEntity, RestaurantDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantDto addItemsToRestaurant(Long restaurantId, List<ItemsDto> items) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant with ID " + restaurantId + " not found."));

        List<ItemsEntity> existingItems = restaurant.getMenuItems();

        for (int i = 0; i < items.size(); i++) {
            ItemsDto itemDto = items.get(i);
            ItemsEntity newItem = mapper.map(itemDto, ItemsEntity.class);

            boolean found = false;

            for (int j = 0; j < existingItems.size(); j++) {
                ItemsEntity oldItem = existingItems.get(j);

                if (Objects.equals(oldItem.getName(), newItem.getName())) {
                    oldItem.setQuantity(oldItem.getQuantity() + newItem.getQuantity());
                    found = true;
                    break;
                }
            }

            if (!found) {
                newItem.setRestaurant(restaurant);
                existingItems.add(newItem);
                itemRepository.save(newItem);
            }
        }

        restaurant.setMenuItems(existingItems);
        RestaurantEntity saved = restaurantRepository.save(restaurant);
        return mapper.map(saved, RestaurantDto.class);
    }

    @Override
    public RestaurantDto removeItemsFromRestaurant(Long restaurantId, List<ItemsDto> items) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant with ID " + restaurantId + " not found."));

        List<ItemsEntity> existingItems = restaurant.getMenuItems();

        for (int i = 0; i < items.size(); i++) {
            ItemsDto itemDto = items.get(i);
            String removedName = itemDto.getName();
            int removeQty = itemDto.getQuantity();

            boolean found = false;

            Iterator<ItemsEntity> iterator = existingItems.iterator();
            while (iterator.hasNext()) {
                ItemsEntity oldItem = iterator.next();

                if (oldItem.getName().equalsIgnoreCase(removedName)) {
                    found = true;
                    if (removeQty >= oldItem.getQuantity()) {
                        iterator.remove();
                        oldItem.setRestaurant(null);
                        itemRepository.save(oldItem);
                    } else {
                        oldItem.setQuantity(oldItem.getQuantity() - removeQty);
                    }
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Item with name: " + removedName + " not found in the menu.");
            }
        }

        restaurant.setMenuItems(existingItems);
        RestaurantEntity saved = restaurantRepository.save(restaurant);
        return mapper.map(saved, RestaurantDto.class);
    }

    @Override
    public RestaurantDto getRatingOfRestaurant(Long restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant with ID " + restaurantId + " not found."));
        return new RestaurantDto(restaurant.getRating());
    }

    @Override
    public List<ItemsDto> getMenu(Long restaurantId, int page, int size, String sortBy) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant with ID " + restaurantId + " not found."));

        List<ItemsEntity> menuItems = restaurant.getMenuItems();

        //custom comparator
        Comparator<ItemsEntity> comparator = switch (sortBy.toLowerCase()) {
            case "name" -> Comparator.comparing(ItemsEntity::getName);
            case "quantity" -> Comparator.comparingInt(ItemsEntity::getQuantity);
            default -> Comparator.comparingDouble(ItemsEntity::getPrice);
        };
        menuItems.sort(comparator);

        // manually making pages
        int start = page * size;
        int end = Math.min(start + size, menuItems.size());

        if (start > menuItems.size()) {
            return new ArrayList<>(); // return empty list
        }

        List<ItemsEntity> paginatedItems = menuItems.subList(start, end);

        return paginatedItems.stream()
                .map(item -> mapper.map(item, ItemsDto.class))
                .collect(Collectors.toList());
    }

}