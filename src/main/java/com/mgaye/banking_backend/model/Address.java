package com.mgaye.banking_backend.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    // // Add manual builder method
    // public static AddressBuilder builder() {
    // return new AddressBuilder();
    // }

    // // Builder class
    // public static class AddressBuilder {
    // private String street;
    // private String city;
    // private String state;
    // private String zipCode;
    // private String country;

    // public AddressBuilder street(String street) {
    // this.street = street;
    // return this;
    // }

    // public AddressBuilder city(String city) {
    // this.city = city;
    // return this;
    // }

    // public AddressBuilder state(String state) {
    // this.state = state;
    // return this;
    // }

    // public AddressBuilder zipCode(String zipCode) {
    // this.zipCode = zipCode;
    // return this;
    // }

    // public AddressBuilder country(String country) {
    // this.country = country;
    // return this;
    // }

    // public Address build() {
    // return new Address(street, city, state, zipCode, country);
    // }
    // }
}