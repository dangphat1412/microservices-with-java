package com.secure.accounts.service.impl;

import com.secure.accounts.dto.AccountsDto;
import com.secure.accounts.dto.CardsDto;
import com.secure.accounts.dto.CustomerDetailsDto;
import com.secure.accounts.dto.LoansDto;
import com.secure.accounts.entity.Accounts;
import com.secure.accounts.entity.Customer;
import com.secure.accounts.exception.ResourceNotFoundException;
import com.secure.accounts.mapper.AccountsMapper;
import com.secure.accounts.mapper.CustomerMapper;
import com.secure.accounts.repository.AccountsRepository;
import com.secure.accounts.repository.CustomerRepository;
import com.secure.accounts.service.ICustomersService;
import com.secure.accounts.service.client.CardsFeignClient;
import com.secure.accounts.service.client.LoansFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomersServiceImpl implements ICustomersService {
    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    /**
     * Fetch customer details by mobile number
     *
     * @param mobileNumber the mobile number of the customer
     * @return CustomerDetailsDto containing customer information
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoansDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardsDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
