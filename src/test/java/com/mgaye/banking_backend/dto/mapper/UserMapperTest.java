package com.mgaye.banking_backend.dto.mapper;

import com.mgaye.banking_backend.dto.UserDto;
import com.mgaye.banking_backend.dto.request.AddressRequest;
import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.response.UserResponse;
import com.mgaye.banking_backend.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

public class UserMapperTest {

    // private final UserMapper mapper = new UbserMapperImpl();

    // @Autowired
    // private UserMapper userMapper;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToDto() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setKycStatus(User.KycStatus.VERIFIED);

        UserDto dto = userMapper.toDto(user);

        assertEquals("VERIFIED", dto.kycStatus());
        assertEquals("test@example.com", dto.email());
    }

    @Test
    void testFromRegisterRequestWithRecord() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "Password@123",
                "+1234567890",
                LocalDate.of(1990, 1, 1),
                new AddressRequest("123 Main St", "Springfield", "IL", "62704", "US"));

        User user = userMapper.fromRegisterRequest(request);

        assertEquals("John", user.getFirstName());
        assertEquals(User.KycStatus.PENDING, user.getKycStatus());
    }

    @Test
    void testToUserResponse() {
        User user = new User();
        user.setPhone("+1555-1234");
        user.setEmail("user@bank.com");

        UserResponse response = userMapper.toUserResponse(user);

        assertEquals("+1555-1234", response.getPhoneNumber());
        assertEquals("user@bank.com", response.getEmail());
    }
}
