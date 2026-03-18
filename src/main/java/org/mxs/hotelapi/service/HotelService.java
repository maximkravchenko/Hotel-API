package org.mxs.hotelapi.service;

import org.mxs.hotelapi.dto.request.HotelCreateDTO;
import org.mxs.hotelapi.dto.response.HotelDetailDTO;
import org.mxs.hotelapi.dto.response.HotelShortDTO;

import java.util.List;
import java.util.Map;

public interface HotelService {

    List<HotelShortDTO> getAllHotels();
    HotelDetailDTO getHotelById(Long id);
    List<HotelShortDTO> searchHotels(String name, String brand, String city, String country, List<String> amenities);
    HotelShortDTO createHotel(HotelCreateDTO request);
    HotelDetailDTO addAmenities(Long hotelId, List<String> amenities);
    Map<String, Long> getHistogram(String param);

}
