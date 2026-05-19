package com.re.cinemabookingapp.service.impl;

import com.re.cinemabookingapp.repository.UserRepository;
import com.re.cinemabookingapp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;


}
