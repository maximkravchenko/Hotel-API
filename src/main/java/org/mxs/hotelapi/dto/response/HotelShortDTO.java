package org.mxs.hotelapi.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelShortDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
