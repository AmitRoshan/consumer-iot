package com.vodafone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vodafone.constant.Constant;
import com.vodafone.model.IotData;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.vodafone.constant.Constant.AIRPLANE_MODE_ENABLE;
import static com.vodafone.constant.Constant.LOCATION_IDENTIFIED;

@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IotResponseDTO {

    private String id;
    private String name;
    private String datetime;

    @JsonProperty("long")
    private String longitude;

    private String lat;
    private String status;
    private String battery;
    private String description;

    public static IotResponseDTO from(IotData iotData, String status) {
        log.info("creating IotResponseDTO from IotData");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateTime =  LocalDateTime.ofInstant(Instant.ofEpochMilli(iotData.getDateTime()),
                ZoneId.systemDefault());

        IotResponseDTOBuilder builder = IotResponseDTO.builder()
                .id(iotData.getProductId())
                .battery(iotData.getBatteryStatus())
                .name(iotData.getProductName())
                .datetime(dateTime.format(dateTimeFormatter));


        if(iotData.getAirplaneMode().isPresent() && iotData.getAirplaneMode().get().equals(Boolean.FALSE)) {
            log.info("airplane mode is off");

            builder = builder.longitude(iotData.getLongitude().toString())
                    .lat(iotData.getLatitude().toString())
                    .status(Constant.DeviceStatus.STATUS_ACTIVE.toString())
                    .description(LOCATION_IDENTIFIED);

        }
        else {
            log.info("airplane mode is on");
            builder = builder.status(Constant.DeviceStatus.STATUS_INACTIVE.toString())
                    .description(AIRPLANE_MODE_ENABLE);
        }
        if(status != null)
            builder = builder.status(status);

        return  builder.build();
    }
}
