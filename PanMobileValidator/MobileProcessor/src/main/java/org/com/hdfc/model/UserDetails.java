package org.com.hdfc.model;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApplicationScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    private Long id;

    private String firstName;

    private String lastName;

    private Integer mobileNumber;

    private String panNumber;

    private String address;

    private String gender;

    private Boolean panValidationStatus;

    private Boolean mobileValidationStatus;
}