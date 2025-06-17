package com.example.Zomato.ZomatoApplication.services;

import com.example.Zomato.ZomatoApplication.dtos.UserDto;
import com.example.Zomato.ZomatoApplication.entites.CustomerEntity;
import com.example.Zomato.ZomatoApplication.entites.UserEntity;
import com.example.Zomato.ZomatoApplication.entites.WalletEntity;
import com.example.Zomato.ZomatoApplication.enums.Roles;
import com.example.Zomato.ZomatoApplication.repositories.UserRepository;
import com.example.Zomato.ZomatoApplication.services.Impl.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public List<UserDto> getAllUsers() {
        List<UserEntity> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto onBoardCustomer(UserDto user) {
        UserDto userDto = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User with email: " + user.getEmail() + " already exists."));

        UserEntity newUserEntity = mapper.map(userDto, UserEntity.class);

        // default role will be customer

        CustomerEntity customer = CustomerEntity
                .builder()
                .setEmail(newUserEntity.getEmail())
                .setName(newUserEntity.getName())
                .build();

        WalletEntity wallet = WalletEntity
                .builder()
                .setBalance(0.0)
                .setCustomerEntity(customer)
                .build();

        UserEntity savedUser = userRepository.save(newUserEntity);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto assignRoleToUser(Long userId, Roles role) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setRole(role);
        }
        else {
            throw new RuntimeException("User with ID: " + user.get().getId() + " not found");
        }
        UserEntity savedUser = userRepository.save(user.get());
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public String login(String username, String password) {
        return null;
    }
}
