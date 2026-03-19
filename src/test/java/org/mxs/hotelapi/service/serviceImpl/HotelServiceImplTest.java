package org.mxs.hotelapi.service.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mxs.hotelapi.dto.response.HotelShortDTO;
import org.mxs.hotelapi.entity.Amenity;
import org.mxs.hotelapi.entity.Hotel;
import org.mxs.hotelapi.mapping.HotelMapper;
import org.mxs.hotelapi.repository.AmenityRepository;
import org.mxs.hotelapi.repository.HotelRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class HotelServiceImplTest {

    private HotelRepository hotelRepository;
    private AmenityRepository amenityRepository;
    private HotelMapper hotelMapper;
    private HotelServiceImpl service;

    @BeforeEach
    void setUp() {
        hotelRepository = mock(HotelRepository.class);
        amenityRepository = mock(AmenityRepository.class);
        hotelMapper = new HotelMapper();
        service = new HotelServiceImpl(hotelRepository, amenityRepository, hotelMapper);
    }

    @Test
    void getHotelById_shouldThrowWhenNotFound() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getHotelById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Hotel not found with id: 99");
    }

    @Test
    void addAmenities_shouldThrowWhenHotelNotFound() {
        when(hotelRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addAmenities(42L, List.of("Free WiFi")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Hotel not found with id: 42");
    }

    @Test
    void getHistogram_shouldReturnCountsByBrand() {
        Hotel h1 = new Hotel();
        h1.setBrand("Hilton");
        Hotel h2 = new Hotel();
        h2.setBrand("Hilton");
        Hotel h3 = new Hotel();
        h3.setBrand("Marriott");

        when(hotelRepository.findAll()).thenReturn(List.of(h1, h2, h3));

        Map<String, Long> histogram = service.getHistogram("brand");

        assertThat(histogram.get("Hilton")).isEqualTo(2L);
        assertThat(histogram.get("Marriott")).isEqualTo(1L);
    }

    @Test
    void getHistogram_shouldReturnCountsByCountry() {
        Hotel h1 = new Hotel();
        h1.setCountry("Belarus");
        Hotel h2 = new Hotel();
        h2.setCountry("Belarus");
        Hotel h3 = new Hotel();
        h3.setCountry("Poland");

        when(hotelRepository.findAll()).thenReturn(List.of(h1, h2, h3));

        Map<String, Long> histogram = service.getHistogram("country");

        assertThat(histogram.get("Belarus")).isEqualTo(2L);
        assertThat(histogram.get("Poland")).isEqualTo(1L);
    }

    @Test
    void getHistogram_shouldReturnCountsByAmenities() {
        Hotel h1 = new Hotel();
        h1.setAmenities(List.of(amenity("WiFi"), amenity("Pool")));
        Hotel h2 = new Hotel();
        h2.setAmenities(List.of(amenity("WiFi")));

        when(hotelRepository.findAll()).thenReturn(List.of(h1, h2));

        Map<String, Long> histogram = service.getHistogram("amenities");

        assertThat(histogram.get("WiFi")).isEqualTo(2L);
        assertThat(histogram.get("Pool")).isEqualTo(1L);
    }

    @Test
    void searchHotels_shouldReturnAllWhenNoFilters() {
        Hotel h1 = new Hotel();
        h1.setId(1L);
        h1.setName("Hotel A");
        Hotel h2 = new Hotel();
        h2.setId(2L);
        h2.setName("Hotel B");

        when(hotelRepository.findAll()).thenReturn(List.of(h1, h2));

        List<HotelShortDTO> result = service.searchHotels(null, null, null, null, null);

        assertThat(result).hasSize(2);
    }


    private Amenity amenity(String name) {
        Amenity a = new Amenity();
        a.setName(name);
        return a;
    }
}