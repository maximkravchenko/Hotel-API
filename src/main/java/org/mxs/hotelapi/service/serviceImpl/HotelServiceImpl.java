package org.mxs.hotelapi.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.mxs.hotelapi.dto.request.HotelCreateDTO;
import org.mxs.hotelapi.dto.response.HotelDetailDTO;
import org.mxs.hotelapi.dto.response.HotelShortDTO;
import org.mxs.hotelapi.entity.Amenity;
import org.mxs.hotelapi.entity.Hotel;
import org.mxs.hotelapi.exception.HotelNotFoundException;
import org.mxs.hotelapi.exception.InvalidHistogramParamException;
import org.mxs.hotelapi.mapping.HotelMapper;
import org.mxs.hotelapi.repository.AmenityRepository;
import org.mxs.hotelapi.repository.HotelRepository;
import org.mxs.hotelapi.service.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<HotelShortDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toShortDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HotelDetailDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        return hotelMapper.toDetailDTO(hotel);
    }

    @Override
    public List<HotelShortDTO> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        return hotelRepository.findAll().stream()
                .filter(h -> name == null || h.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(h -> brand == null || brand.equalsIgnoreCase(h.getBrand()))
                .filter(h -> city == null || city.equalsIgnoreCase(h.getCity()))
                .filter(h -> country == null || country.equalsIgnoreCase(h.getCountry()))
                .filter(h -> amenities == null || amenities.isEmpty() ||
                        h.getAmenities().stream()
                                .map(a -> a.getName().toLowerCase())
                                .collect(Collectors.toList())
                                .containsAll(amenities.stream()
                                        .map(String::toLowerCase)
                                        .collect(Collectors.toList())))
                .map(hotelMapper::toShortDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HotelShortDTO createHotel(HotelCreateDTO request) {
        Hotel hotel = hotelMapper.toEntity(request);
        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toShortDTO(saved);
    }

    @Override
    public HotelDetailDTO addAmenities(Long hotelId, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        List<Amenity> newAmenities = amenities.stream()
                .map(name -> {
                    Amenity amenity = new Amenity();
                    amenity.setName(name);
                    amenity.setHotel(hotel);
                    return amenity;
                })
                .collect(Collectors.toList());

        hotel.getAmenities().addAll(newAmenities);
        hotelRepository.save(hotel);
        return hotelMapper.toDetailDTO(hotel);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        List<Hotel> hotels = hotelRepository.findAll();
        return switch (param.toLowerCase()) {
            case "brand" -> hotels.stream()
                    .filter(h -> h.getBrand() != null)
                    .collect(Collectors.groupingBy(Hotel::getBrand, Collectors.counting()));
            case "city" -> hotels.stream()
                    .filter(h -> h.getCity() != null)
                    .collect(Collectors.groupingBy(Hotel::getCity, Collectors.counting()));
            case "country" -> hotels.stream()
                    .filter(h -> h.getCountry() != null)
                    .collect(Collectors.groupingBy(Hotel::getCountry, Collectors.counting()));
            case "amenities" -> hotels.stream()
                    .flatMap(h -> h.getAmenities().stream())
                    .collect(Collectors.groupingBy(Amenity::getName, Collectors.counting()));
            default -> throw new InvalidHistogramParamException(param);
        };
    }
}
