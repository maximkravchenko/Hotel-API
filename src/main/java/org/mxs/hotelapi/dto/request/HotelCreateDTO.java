package org.mxs.hotelapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotelCreateDTO {

    @NotBlank
    private String name;
    private String description;

    @NotBlank
    private String brand;

    @Valid
    @NotNull
    private AddressRequest address;

    @Valid
    @NotNull
    private ContactsRequest contacts;

    private ArrivalTimeRequest arrivalTime;

    @Getter @Setter @NoArgsConstructor
    public static class AddressRequest {
        @NotNull
        private Integer houseNumber;
        @NotBlank
        private String street;
        @NotBlank
        private String city;
        @NotBlank
        private String country;
        @NotBlank
        private String postCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ContactsRequest {
        @NotBlank
        private String phone;
        @NotBlank
        @Email
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ArrivalTimeRequest {
        private String checkIn;
        private String checkOut; // optional по ТЗ
    }

}
