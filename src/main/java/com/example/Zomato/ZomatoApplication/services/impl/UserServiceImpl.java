package com.example.Zomato.ZomatoApplication.services.impl;

import com.example.Zomato.ZomatoApplication.dtos.UserDto;
import com.example.Zomato.ZomatoApplication.entites.CustomerEntity;
import com.example.Zomato.ZomatoApplication.entites.UserEntity;
import com.example.Zomato.ZomatoApplication.entites.WalletEntity;
import com.example.Zomato.ZomatoApplication.enums.Roles;
import com.example.Zomato.ZomatoApplication.repositories.CustomerRepository;
import com.example.Zomato.ZomatoApplication.repositories.UserRepository;
import com.example.Zomato.ZomatoApplication.repositories.WalletRepository;
import com.example.Zomato.ZomatoApplication.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionService sessionService;
    private final UserServiceImpl userService;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("User with email: " + username + " not found"));
    }

    public UserDto loadUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with ID: " + id + " not found"));
        return mapper.map(userEntity, UserDto.class);
    }

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

    public UserDto authenticateUser(UserDto userDto) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        sessionService.generateNewSession(user, refreshToken);

        return new UserDto(user.getId(), accessToken, refreshToken);
    }

    public UserDto refreshToken(String refreshToken) {
        Claims claimsFromToken = jwtService.getClaimsFromToken(refreshToken);
        Long userId = jwtService.getUserId(claimsFromToken);

        UserDto userDTO = userService.loadUserById(userId);

        sessionService.validateSession(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(mapper.map(userDTO, UserEntity.class));
        return new UserDto(userId, newAccessToken, refreshToken);
    }

    public UserDto createUser(UserDto userDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(userDto.getEmail());

        if (userEntityOptional.isPresent()) {
            boolean equals = Objects.equals(userEntityOptional.get().getEmail(), userDto.getEmail());
            if (equals) {
                throw new BadCredentialsException("User with " + userDto.getEmail() + " is already registered");
            }
        }
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity saveUser = userRepository.save(userEntity);
        return mapper.map(saveUser, UserDto.class);
    }
}
