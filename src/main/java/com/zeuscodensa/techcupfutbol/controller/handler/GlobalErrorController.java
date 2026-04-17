package com.zeuscodensa.techcupfutbol.controller.handler;

import com.zeuscodensa.techcupfutbol.controller.dto.ApiErrorDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalErrorController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiErrorDTO> handleError(HttpServletRequest request) {
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (statusObj != null) {
            try {
                statusCode = Integer.parseInt(statusObj.toString());
            } catch (NumberFormatException ignored) {
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
        }

        HttpStatus status = HttpStatus.resolve(statusCode);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String message = switch (status) {
            case BAD_REQUEST -> "Bad request";
            case UNAUTHORIZED -> "Unauthorized";
            case NOT_FOUND -> "Resource not found";
            default -> status.getReasonPhrase();
        };

        return ResponseEntity.status(status).body(new ApiErrorDTO(status.value(), message));
    }
}
