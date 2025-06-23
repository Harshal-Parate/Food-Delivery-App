package com.example.Zomato.ZomatoApplication.controllers;

import com.example.Zomato.ZomatoApplication.dtos.WalletDto;
import com.example.Zomato.ZomatoApplication.services.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/payment")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @PostMapping(path = "/order/{orderId}/customer/{customerId}")
    public ResponseEntity<String> processPayment(@PathVariable(value = "orderId") Long orderId,
                                                 @PathVariable(value = "customerId") Long customerId) {
        String status = paymentService.processPayment(orderId, customerId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping(path = "/user/{userId}/add-funds")
    public ResponseEntity<WalletDto> addFundsToWallet(@PathVariable(value = "userId") Long userId,
                                                      @RequestParam(defaultValue = "100") double fund) {
        WalletDto walletDto = paymentService.addFundsToWallet(fund, userId);
        return new ResponseEntity<>(walletDto, HttpStatus.OK);
    }

    @PostMapping(path = "/user/{userId}/withdraw-funds")
    public ResponseEntity<WalletDto> withdrawFundsFromWallet(@PathVariable(value = "userId") Long userId,
                                                             @RequestParam(defaultValue = "100") double fund) {
        WalletDto walletDto = paymentService.withdrawFundsFromWallet(fund, userId);
        return new ResponseEntity<>(walletDto, HttpStatus.OK);
    }

    @PostMapping(path = "/user/{userId}/wallet-details")
    public ResponseEntity<WalletDto> getWalletDetails(@PathVariable(value = "userId") Long userId) {
        WalletDto walletDetails = paymentService.getWalletDetails(userId);
        return new ResponseEntity<>(walletDetails, HttpStatus.OK);
    }
}
