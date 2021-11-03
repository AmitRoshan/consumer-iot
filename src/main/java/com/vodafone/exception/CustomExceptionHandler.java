package com.vodafone.exception;

import com.vodafone.dto.IotResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

import static com.vodafone.constant.Constant.ID_NOT_FOUND_EXCEPTION;
import static com.vodafone.constant.Constant.TECHNICAL_EXCEPTION;

@ControllerAdvice
@RestController
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<IotResponseDTO> handleAllException(Exception exception){
        log.error("Exception, Message : {}", exception.getMessage());

        return new ResponseEntity<IotResponseDTO>(IotResponseDTO.builder().description(TECHNICAL_EXCEPTION).build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IotCustomException.class)
    public ResponseEntity<IotResponseDTO> handleIotCustomException(IotCustomException iotCustomException){
        log.error("IotCustomException, Message : {}", iotCustomException.getMessage());

        return new ResponseEntity<>(IotResponseDTO.builder().description(iotCustomException.getMessage()).build()
                , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeviceLocationNotFoundException.class)
    public ResponseEntity<IotResponseDTO> handleDeviceLocationNotFoundException(DeviceLocationNotFoundException deviceLocationNotFoundException){
        log.error("IotCustomException, Message : {}", deviceLocationNotFoundException.getMessage());

        return new ResponseEntity<>(IotResponseDTO.builder().description(deviceLocationNotFoundException.getMessage()).build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<IotResponseDTO> handleNoSuchElementException(NoSuchElementException noSuchElementException, WebRequest request){
        log.error("IotCustomException, Message : {}", noSuchElementException.getMessage());

        String errMsg = String.format(ID_NOT_FOUND_EXCEPTION, request.getParameter("productId"));
        return new ResponseEntity<>(IotResponseDTO.builder().description(errMsg).build(), HttpStatus.NOT_FOUND);
    }

}
