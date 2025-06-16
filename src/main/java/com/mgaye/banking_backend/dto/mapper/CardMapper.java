package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mgaye.banking_backend.dto.response.CardResponse;
import com.mgaye.banking_backend.model.Card;
import com.mgaye.banking_backend.model.User;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "cardHolderName", expression = "java(getCardHolderName(card.getUser()))")
    @Mapping(target = "linkedAccountId", source = "linkedAccount.id")
    @Mapping(target = "expirationDate", source = "expirationDate")
    @Mapping(target = "cardType", source = "cardType")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "provider", source = "provider")
    CardResponse toCardResponse(Card card);

    default String getCardHolderName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}