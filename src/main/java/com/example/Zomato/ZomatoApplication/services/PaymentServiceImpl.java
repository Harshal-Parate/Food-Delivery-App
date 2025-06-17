package com.example.Zomato.ZomatoApplication.services;

import com.example.Zomato.ZomatoApplication.dtos.WalletDto;
import com.example.Zomato.ZomatoApplication.services.Impl.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public String processPayment(Long orderId) {
        return null;
    }

    @Override
    public WalletDto addFundsToWallet(Double funds, Long userId) {
        return null;
    }

    @Override
    public WalletDto withdrawFundsFromWallet(Double funds, Long userId) {
        return null;
    }

    @Override
    public WalletDto getWalletDetails(Long userId) {
        return null;
    }
}
