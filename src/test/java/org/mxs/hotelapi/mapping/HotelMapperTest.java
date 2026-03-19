package org.mxs.hotelapi.mapping;

import org.junit.jupiter.api.Test;
import org.mxs.hotelapi.dto.response.HotelDetailDTO;
import org.mxs.hotelapi.dto.response.HotelShortDTO;
import org.mxs.hotelapi.entity.Hotel;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class HotelMapperTest {

    private final HotelMapper mapper = new HotelMapper();

    @Test
    void toShortDto_shouldBuildAddressString() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setDescription("Desc");
        hotel.setPhone("+000");
        hotel.setHouseNumber(9);
        hotel.setStreet("Pobediteley Avenue");
        hotel.setCity("Minsk");
        hotel.setCountry("Belarus");
        hotel.setPostCode("220004");
        hotel.setAmenities(Collections.emptyList());

        HotelShortDTO dto = mapper.toShortDTO(hotel);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test Hotel");
        assertThat(dto.getAddress())
                .isEqualTo("9 Pobediteley Avenue, Minsk, 220004, Belarus");
    }

    @Test
    void toDetailDto_shouldMapNestedObjects() {
        Hotel hotel = new Hotel();
        hotel.setId(2L);
        hotel.setName("Detail Hotel");
        hotel.setDescription("Desc");
        hotel.setBrand("BrandX");
        hotel.setHouseNumber(1);
        hotel.setStreet("Test");
        hotel.setCity("City");
        hotel.setCountry("Country");
        hotel.setPostCode("000000");
        hotel.setPhone("+111");
        hotel.setEmail("mail@test.com");
        hotel.setCheckIn("14:00");
        hotel.setCheckOut("12:00");
        hotel.setAmenities(Collections.emptyList());

        HotelDetailDTO dto = mapper.toDetailDTO(hotel);

        assertThat(dto.getBrand()).isEqualTo("BrandX");
        assertThat(dto.getAddress().getCity()).isEqualTo("City");
        assertThat(dto.getContacts().getEmail()).isEqualTo("mail@test.com");
        assertThat(dto.getArrivalTime().getCheckIn()).isEqualTo("14:00");
    }
}