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
import com.example.Zomato.ZomatoApplication.services.helper.UserLookupService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionService sessionService;
    private final UserLookupService userLookupService;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto onBoardCustomer(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with email: " + userDto.getEmail() + " already exists.");
        }

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setRole(Roles.CUSTOMER);

        CustomerEntity customer = new CustomerEntity();
        customer.setName(userDto.getName());
        customer.setEmail(userDto.getEmail());

        WalletEntity wallet = new WalletEntity();
        wallet.setBalance(0.0);
        wallet.setCustomer(customer);

        customer.setWallet(wallet);

        walletRepository.save(wallet);
        customerRepository.save(customer);
        UserEntity saved = userRepository.save(userEntity);

        return mapper.map(saved, UserDto.class);
    }

    @Override
    public UserDto assignRoleToUser(Long userId, String role) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(Roles.valueOf(role.toUpperCase()));
        return mapper.map(userRepository.save(user), UserDto.class);
    }

    public UserDto authenticateUser(UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        UserEntity user = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        sessionService.generateNewSession(user, refreshToken);

        return new UserDto(user.getId(), accessToken, refreshToken);
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BadCredentialsException("User with email " + userDto.getEmail() + " already exists");
        }

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
    public UserDto refreshToken(String refreshToken) {
        Claims claimsFromToken = jwtService.getClaimsFromToken(refreshToken);
        Long userId = jwtService.getUserId(claimsFromToken);

        UserDto userDTO = userLookupService.loadUserById(userId);

        sessionService.validateSession(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(mapper.map(userDTO, UserEntity.class));
        return new UserDto(userId, userDTO.getEmail(), newAccessToken, refreshToken);
    }
}
