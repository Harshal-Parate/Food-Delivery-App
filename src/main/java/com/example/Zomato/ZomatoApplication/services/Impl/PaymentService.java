package com.example.Zomato.ZomatoApplication.services.Impl;

import com.example.Zomato.ZomatoApplication.dtos.WalletDto;

public interface PaymentService {

    //Handles payments, wallets

    String processPayment(Long orderId);

    WalletDto addFundsToWallet(Double funds, Long userId);

    WalletDto withdrawFundsFromWallet(Double funds, Long userId);

    WalletDto getWalletDetails(Long userId);


}
