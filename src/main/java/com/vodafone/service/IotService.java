package com.vodafone.service;

import com.vodafone.constant.Constant;
import com.vodafone.dto.IotRequestDTO;
import com.vodafone.dto.IotResponseDTO;
import com.vodafone.exception.DeviceLocationNotFoundException;
import com.vodafone.exception.IotCustomException;
import com.vodafone.model.IotData;
import com.vodafone.repository.IotRepository;
import com.vodafone.utils.ConvertOptionalStringToOptionalLong;
import com.vodafone.utils.CsvProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.vodafone.constant.Constant.DEVICE_NOT_LOCATED_EXCEPTION;
import static com.vodafone.constant.Constant.NO_DATA_FILE_FOUND_EXCEPTION;

@Service
@Slf4j
@AllArgsConstructor
public class IotService {

    private final IotRepository iotRepository;

    /**
     * @param iotRequestDTO
     * @return IotResponseDTO
     */
    public IotResponseDTO loadData(IotRequestDTO iotRequestDTO) {
        try {
            iotRepository.saveData(CsvProcessor.processIotDataFile(iotRequestDTO.getFilepath()));
        } catch (FileNotFoundException e) {
            throw new IotCustomException(NO_DATA_FILE_FOUND_EXCEPTION);
        }
        log.info("IOT data updated ");

        return IotResponseDTO.builder().description(Constant.DATA_REFRESHED).build();
    }

    /**
     *
     * @param productId
     * @param tstmp
     * @return IotResponseDTO
     */
    public IotResponseDTO reportDevice(String productId, Optional<String> tstmp) {
        log.info("service to report device data");

        OptionalLong timeStamp = ConvertOptionalStringToOptionalLong.convert(tstmp);
        Long dateTime = timeStamp.orElse(Instant.now().toEpochMilli());

        List<IotData> iotList = iotRepository.findByProductIdAndDateTimeLessThanEqual(productId, dateTime);
        IotData iotData = filterByDateTime(iotList, dateTime);

        isDeviceLocatable(iotData);

        String status = null;
        if (iotData.getProductName().equals(Constant.ProductName.CYCLE_PLUS_TRACKER.toString())) {
            log.info("product name :: {} ", iotData.getProductName());
            status = getCyclePlusTrackerStatus(iotList);
        }

        return IotResponseDTO.from(iotData, status);


    }

    /**
     *
     * @param iotList
     * @return String
     */
    private String getCyclePlusTrackerStatus(List<IotData> iotList) {
        List<IotData> sortedIotData = iotList.stream().sorted(Comparator.comparingLong(IotData::getDateTime).reversed())
                .limit(3).collect(Collectors.toList());

        if (sortedIotData.size() == 3 && sortedIotData.stream()
                .allMatch(iotData -> iotData.getLongitude() != null && iotData.getLatitude() != null)) {

            return isAllCoordinateSame(sortedIotData) ?
                    Constant.DeviceStatus.STATUS_INACTIVE.toString() : Constant.DeviceStatus.STATUS_ACTIVE.toString();
        }
        else {
            return Constant.DeviceStatus.STATUS_NA.toString();
        }
    }

    private boolean isAllCoordinateSame(List<IotData> iotDataList) {
        return iotDataList.stream().allMatch(iotData -> Objects.equals(iotData.getLatitude(), iotDataList.get(0).getLatitude())
                && Objects.equals(iotData.getLongitude(), iotDataList.get(0).getLongitude()));
    }

    private IotData filterByDateTime(List<IotData> iotList, Long dateTime) {

        return iotList.stream().min(Comparator.comparingLong(iotData -> dateTime - iotData.getDateTime()))
                .orElseThrow(NoSuchElementException::new);
    }

    private void isDeviceLocatable(IotData iotData) {

        if (iotData.getAirplaneMode().isPresent() && !iotData.getAirplaneMode().get()
                && iotData.getLatitude() == null && iotData.getLongitude() == null)
            throw new DeviceLocationNotFoundException(DEVICE_NOT_LOCATED_EXCEPTION);

    }
}
