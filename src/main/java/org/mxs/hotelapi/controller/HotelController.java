package org.mxs.hotelapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mxs.hotelapi.dto.request.HotelCreateDTO;
import org.mxs.hotelapi.dto.response.HotelDetailDTO;
import org.mxs.hotelapi.dto.response.HotelShortDTO;
import org.mxs.hotelapi.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Tag(name = "Hotel API", description = "Endpoints for working with hotels")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels", description = "Returns a list of hotels in short format")
    @ApiResponse(responseCode = "200", description = "Hotels returned successfully")
    public ResponseEntity<List<HotelShortDTO>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @Operation(summary = "Get hotel by id", description = "Returns detailed info for a single hotel")
    @ApiResponse(responseCode = "200", description = "Hotel found")
    @ApiResponse(responseCode = "404", description = "Hotel not found")
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelDetailDTO> getHotelById(
            @Parameter(description = "Hotel id", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels", description = "Search hotels by name, brand, city, country or amenities")
    @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    public ResponseEntity<List<HotelShortDTO>> searchHotels(
            @Parameter(description = "Hotel name", example = "GP Hotel")
            @RequestParam(required = false) String name,
            @Parameter(description = "Hotel brand", example = "GP Solutions")
            @RequestParam(required = false) String brand,
            @Parameter(description = "City", example = "Minsk")
            @RequestParam(required = false) String city,
            @Parameter(description = "Country", example = "Belarus")
            @RequestParam(required = false) String country,
            @Parameter(description = "Amenities list", example = "Free WiFi, Free parking")
            @RequestParam(required = false) List<String> amenities) {

        return ResponseEntity.ok(hotelService
                .searchHotels(name, brand, city, country, amenities));
    }

    @PostMapping("/hotels")
    @Operation(summary = "Create hotel", description = "Creates a new hotel and returns short info about it")
    @ApiResponse(responseCode = "201", description = "Hotel created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    public ResponseEntity<HotelShortDTO> createHotel(
            @Valid @RequestBody
            HotelCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.createHotel(request));
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities", description = "Adds amenities to hotel by id")
    @ApiResponse(responseCode = "200", description = "Amenities added successfully")
    @ApiResponse(responseCode = "404", description = "Hotel not found")
    public ResponseEntity<HotelDetailDTO> addAmenities(
            @Parameter(description = "Hotel id", example = "1")
            @PathVariable Long id,
            @RequestBody List<String> amenities) {
        return ResponseEntity.ok(hotelService.addAmenities(id, amenities));
    }


    @GetMapping("/histogram/{param}")
    @Operation(summary = "Get histogram", description = "Returns grouped hotel counts by brand, city, country or amenities")
    @ApiResponse(responseCode = "200", description = "Histogram returned successfully")
    @ApiResponse(responseCode = "400", description = "Invalid histogram parameter")
    public ResponseEntity<Map<String, Long>> getHistogram(
            @Parameter(description = "Histogram parameter", example = "city")
            @PathVariable String param) {
        return ResponseEntity.ok(hotelService.getHistogram(param));
    }
}
