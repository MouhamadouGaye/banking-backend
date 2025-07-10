package com.mgaye.banking_backend.util;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.TicketSearchCriteria;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.SupportTicket;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketSpecificationBuilder {
    private final UserRepository userRepository;

    public Specification<SupportTicket> build(TicketSearchCriteria criteria, String userId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Base predicate: user's own tickets or all tickets for support agents
            if (!hasSupportRole(userId)) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }

            if (criteria.status() != null) {
                predicates.add(cb.equal(cb.lower(root.get("status")), criteria.status().toLowerCase()));
            }

            if (criteria.category() != null) {
                predicates.add(cb.equal(cb.lower(root.get("category")), criteria.category().toLowerCase()));
            }

            if (criteria.priority() != null) {
                predicates.add(cb.equal(cb.lower(root.get("priority")), criteria.priority().toLowerCase()));
            }

            if (criteria.createdAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"),
                        criteria.createdAfter().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }

            if (criteria.createdBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"),
                        criteria.createdBefore().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }

            if (criteria.assignedTo() != null) {
                User assignee = userRepository.findByEmail(criteria.assignedTo())
                        .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
                predicates.add(cb.equal(root.get("assignedTo").get("id"), assignee.getId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private boolean hasSupportRole(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("SUPPORT_AGENT") || role.getName().equals("ADMIN"));
    }
}