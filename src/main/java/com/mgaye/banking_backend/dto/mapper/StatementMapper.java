package com.mgaye.banking_backend.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.mgaye.banking_backend.dto.StatementItem;
import com.mgaye.banking_backend.model.StatementItemEntity;

// @Mapper(componentModel = "spring")
// public interface StatementMapper {
//     StatementItem toDto(StatementItemEntity entity);

//     StatementItemEntity toEntity(StatementItem dto);

//     default List<StatementItem> toDtoList(List<StatementItemEntity> entities) {
//         return entities.stream().map(this::toDto).toList();
//     }
// }

@Mapper(componentModel = "spring")
public interface StatementMapper {
    StatementItem toDto(StatementItemEntity entity);

    default List<StatementItem> toDtoList(List<StatementItemEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }
}