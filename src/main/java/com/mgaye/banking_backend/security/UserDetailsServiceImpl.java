// package com.mgaye.banking_backend.security;

// import java.nio.file.attribute.UserPrincipal;
// import java.util.Collection;
// import java.util.Set;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import
// org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.mgaye.banking_backend.model.Role;
// import com.mgaye.banking_backend.model.User;
// import com.mgaye.banking_backend.repository.UserRepository;

// import jakarta.transaction.Transactional;
// import lombok.RequiredArgsConstructor;

// // security/UserDetailsServiceImpl.java
// @Service
// @RequiredArgsConstructor
// public class UserDetailsServiceImpl implements UserDetailsService {
// private final UserRepository userRepository;

// @Override
// @Transactional
// public UserDetails loadUserByUsername(String email) throws
// UsernameNotFoundException {
// User user = userRepository.findByEmail(email)
// .orElseThrow(() -> new UsernameNotFoundException("User not found with email:"
// + email));

// return UserPrincipal.builder()
// .id(user.getId())
// .email(user.getEmail())
// .password(user.getPassword())
// .authorities(getAuthorities(user.getRoles()))
// .build();
// }

// private Collection<? extends GrantedAuthority> getAuthorities(Set<Role>
// roles) {
// return roles.stream()
// .flatMap(role -> role.getPermissions().stream())
// .map(permission -> new SimpleGrantedAuthority(permission.name()))
// .collect(Collectors.toSet());
// }
// }