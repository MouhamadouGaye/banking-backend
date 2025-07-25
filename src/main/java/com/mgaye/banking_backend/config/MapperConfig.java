package com.mgaye.banking_backend.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mgaye.banking_backend.dto.mapper.AddressMapper;
import com.mgaye.banking_backend.dto.mapper.NotificationPreferencesMapper;
import com.mgaye.banking_backend.dto.mapper.TransactionMapper;
import com.mgaye.banking_backend.dto.mapper.UserMapper;

@Configuration
public class MapperConfig {

    // @Bean
    // public UserMapper userMapper() {
    // return Mappers.getMapper(UserMapper.class);
    // }

    @Bean
    public TransactionMapper transactionMapper() {
        return Mappers.getMapper(TransactionMapper.class);
    }

    @Bean
    public AddressMapper addressMapper() {
        return Mappers.getMapper(AddressMapper.class);
    }
}