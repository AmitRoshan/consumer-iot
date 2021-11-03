package com.vodafone.controller;

import com.vodafone.dto.IotRequestDTO;
import com.vodafone.dto.IotResponseDTO;
import com.vodafone.service.IotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/iot")
@AllArgsConstructor
@Slf4j
public class IotController {

    private final IotService iotService;

    @PostMapping("/event/v1/")
    public IotResponseDTO loadData(@RequestBody IotRequestDTO iotRequestDTO){

        return iotService.loadData(iotRequestDTO);
    }

    @GetMapping("/event/v1")
    public IotResponseDTO reportDevice(@RequestParam String productId, @RequestParam Optional<String> tstmp){

        return iotService.reportDevice(productId, tstmp);
    }
}
