package com.secure.accounts.service;

import com.secure.accounts.dto.CustomerDetailsDto;

public interface ICustomersService {
    /**
     * Fetch customer details by mobile number
     *
     * @param mobileNumber the mobile number of the customer
     * @return CustomerDetailsDto containing customer information
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
