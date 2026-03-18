package org.mxs.hotelapi.mapping;

import org.mxs.hotelapi.dto.request.HotelCreateDTO;
import org.mxs.hotelapi.dto.response.HotelDetailDTO;
import org.mxs.hotelapi.dto.response.HotelShortDTO;
import org.mxs.hotelapi.entity.Amenity;
import org.mxs.hotelapi.entity.Hotel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HotelMapper {


    public HotelShortDTO toShortDTO(Hotel hotel) {
        HotelShortDTO dto = new HotelShortDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setDescription(hotel.getDescription());
        dto.setPhone(hotel.getPhone());
        dto.setAddress(buildAddress(hotel));

        return dto;
    }

    public HotelDetailDTO toDetailDTO(Hotel hotel) {
        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setDescription(hotel.getDescription());
        dto.setBrand(hotel.getBrand());

        dto.setAddress(new HotelDetailDTO.AddressDto(
                hotel.getHouseNumber(),
                hotel.getStreet(),
                hotel.getCity(),
                hotel.getCountry(),
                hotel.getPostCode()
        ));

        dto.setContacts(new HotelDetailDTO.ContactsDto(
                hotel.getPhone(),
                hotel.getEmail()
        ));

        dto.setArrivalTime(new HotelDetailDTO.ArrivalTimeDto(
                hotel.getCheckIn(),
                hotel.getCheckOut()
        ));

        dto.setAmenities(
                hotel.getAmenities().stream()
                        .map(Amenity::getName)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    public Hotel toEntity(HotelCreateDTO request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setBrand(request.getBrand());
        hotel.setHouseNumber(request.getAddress().getHouseNumber());
        hotel.setStreet(request.getAddress().getStreet());
        hotel.setCity(request.getAddress().getCity());
        hotel.setCountry(request.getAddress().getCountry());
        hotel.setPostCode(request.getAddress().getPostCode());
        hotel.setPhone(request.getContacts().getPhone());
        hotel.setEmail(request.getContacts().getEmail());
        if (request.getArrivalTime() != null) {
            hotel.setCheckIn(request.getArrivalTime().getCheckIn());
            hotel.setCheckOut(request.getArrivalTime().getCheckOut());
        }
        return hotel;
    }


    private String buildAddress(Hotel hotel) {
        return hotel.getHouseNumber() + " " +
                hotel.getStreet() + ", " +
                hotel.getCity() + ", " +
                hotel.getPostCode() + ", " +
                hotel.getCountry();
    }

}
