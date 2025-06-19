package com.example.Zomato.ZomatoApplication.services;

import com.example.Zomato.ZomatoApplication.dtos.UserDto;
import com.example.Zomato.ZomatoApplication.entites.CustomerEntity;
import com.example.Zomato.ZomatoApplication.entites.UserEntity;
import com.example.Zomato.ZomatoApplication.entites.WalletEntity;
import com.example.Zomato.ZomatoApplication.enums.Roles;
import com.example.Zomato.ZomatoApplication.repositories.CustomerRepository;
import com.example.Zomato.ZomatoApplication.repositories.UserRepository;
import com.example.Zomato.ZomatoApplication.repositories.WalletRepository;
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
    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;
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
    public UserDto onBoardCustomer(UserDto userDto) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with email: " + userDto.getEmail() + " already exists.");
        }

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setRole(Roles.CUSTOMER);

        // Create customer
        CustomerEntity customer = new CustomerEntity();
        customer.setName(userDto.getName());
        customer.setEmail(userDto.getEmail());

        // Create wallet
        WalletEntity wallet = new WalletEntity();
        wallet.setBalance(0.0);
        wallet.setCustomer(customer);

        customer.setWallet(wallet); // bidirectional

        walletRepository.save(wallet);
        customerRepository.save(customer);
        UserEntity saved = userRepository.save(userEntity);

        return mapper.map(saved, UserDto.class);
    }


    @Override
    public UserDto assignRoleToUser(Long userId, Roles role) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setRole(role);
        } else {
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
