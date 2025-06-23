package com.example.Zomato.ZomatoApplication.services;

import com.example.Zomato.ZomatoApplication.dtos.WalletDto;
import com.example.Zomato.ZomatoApplication.entites.CustomerEntity;
import com.example.Zomato.ZomatoApplication.entites.CartEntity;
import com.example.Zomato.ZomatoApplication.entites.OrderEntity;
import com.example.Zomato.ZomatoApplication.entites.WalletEntity;
import com.example.Zomato.ZomatoApplication.enums.PaymentStatus;
import com.example.Zomato.ZomatoApplication.repositories.CustomerRepository;
import com.example.Zomato.ZomatoApplication.repositories.OrderRepository;
import com.example.Zomato.ZomatoApplication.repositories.UserRepository;
import com.example.Zomato.ZomatoApplication.repositories.WalletRepository;
import com.example.Zomato.ZomatoApplication.services.Impl.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public String processPayment(Long orderId, Long customerId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Invalid Order ID: " + orderId + "."));

        CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Invalid Customer ID: " + customerId + "."));

        WalletEntity wallet = customerEntity.getWallet();
        if (wallet == null) {
            throw new RuntimeException("Customer does not have a wallet.");
        }

        if (orderEntity.getPaymentStatus() == PaymentStatus.COMPLETED) {
            return "Payment has already been completed for this order.";
        }

        double orderAmount = orderEntity.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        if (wallet.getBalance() < orderAmount) {
            orderEntity.setPaymentStatus(PaymentStatus.FAILED);
            orderRepository.save(orderEntity);
            return "Payment failed. Insufficient wallet balance.";
        }

        wallet.setBalance(wallet.getBalance() - orderAmount);
        orderEntity.setPaymentStatus(PaymentStatus.COMPLETED);

        CartEntity cart = customerEntity.getCart();
        if (cart != null) {
            cart.setItems(new ArrayList<>());
            cart.setTotal(0);
        }

        walletRepository.save(wallet);
        customerRepository.save(customerEntity);
        orderRepository.save(orderEntity);

        return "Payment successful. " + orderAmount + " deducted ,from wallet.";
    }


    @Override
    public WalletDto addFundsToWallet(Double funds, Long userId) {
        CustomerEntity customerEntity = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid User ID: " + userId + "."));

        WalletEntity wallet = customerEntity.getWallet();
        wallet.setBalance(funds);
        WalletEntity savedWallet = walletRepository.save(wallet);
        return mapper.map(savedWallet, WalletDto.class);
    }

    @Override
    public WalletDto withdrawFundsFromWallet(Double funds, Long userId) {
        CustomerEntity customerEntity = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid User ID: " + userId + "."));

        WalletEntity wallet = customerEntity.getWallet();
        double balance = wallet.getBalance();
        if (balance <= funds) {
            wallet.setBalance(0.0);
            walletRepository.save(wallet);
        }
        wallet.setBalance(balance - funds);
        WalletEntity savedWallet = walletRepository.save(wallet);
        return mapper.map(savedWallet, WalletDto.class);
    }

    @Override
    public WalletDto getWalletDetails(Long userId) {
        CustomerEntity customerEntity = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid User ID: " + userId + "."));
        return mapper.map(customerEntity.getWallet(), WalletDto.class);
    }
}
