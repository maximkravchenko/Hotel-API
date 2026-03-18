package org.mxs.hotelapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelDetailDTO {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private AddressDto address;
    private ContactsDto contacts;
    private ArrivalTimeDto arrivalTime;
    private List<String> amenities;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        private Integer houseNumber;
        private String street;
        private String city;
        private String country;
        private String postCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactsDto {
        private String phone;
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArrivalTimeDto {
        private String checkIn;
        private String checkOut;
    }
}
