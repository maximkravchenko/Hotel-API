package org.mxs.hotelapi.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleHotelNotFound_shouldReturn404() {
        HotelNotFoundException ex = new HotelNotFoundException(99L);

        var response = handler.handleHotelNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("status")).isEqualTo(404);
        assertThat(body.get("message")).isEqualTo(ex.getMessage());
    }

    @Test
    void handleInvalidHistogramParam_shouldReturn400() {
        InvalidHistogramParamException ex = new InvalidHistogramParamException("test");

        var response = handler.handleInvalidHistogramParam(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("status")).isEqualTo(400);
        assertThat(body.get("message")).isEqualTo(ex.getMessage());
    }

    @Test
    void handleGeneric_shouldReturn500() {
        RuntimeException ex = new RuntimeException("boom");

        var response = handler.handleGeneric(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo(500);
        assertThat(response.getBody().get("message")).isEqualTo("boom");
    }

    @Test
    void handleValidation_shouldReturn400() {
        var bindingResult = mock(org.springframework.validation.BindingResult.class);
        var fieldError = new org.springframework.validation.FieldError("hotelCreateDTO", "name", "Name is required");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        var ex = mock(org.springframework.web.bind.MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        var response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo(400);
        assertThat(response.getBody().get("message")).isEqualTo("Validation failed");
    }
}
