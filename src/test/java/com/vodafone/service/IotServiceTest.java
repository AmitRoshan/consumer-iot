package com.vodafone.service;

import com.vodafone.constant.Constant;
import com.vodafone.dto.IotRequestDTO;
import com.vodafone.dto.IotResponseDTO;
import com.vodafone.exception.DeviceLocationNotFoundException;
import com.vodafone.exception.IotCustomException;
import com.vodafone.model.IotData;
import com.vodafone.repository.IotRepository;
import com.vodafone.utils.CsvProcessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class IotServiceTest {

    @InjectMocks
    private IotService iotService;

    @Mock
    private IotRepository iotRepository;

    private static MockedStatic<CsvProcessor> csvProcessorMockedStatic = null;
    private static IotRequestDTO iotRequestDTO;

    @BeforeAll
    public static void setUp() {
        csvProcessorMockedStatic = Mockito.mockStatic(CsvProcessor.class);

        iotRequestDTO = new IotRequestDTO();
        iotRequestDTO.setFilepath("");

    }

    @AfterAll
    public static void close() {
        csvProcessorMockedStatic.close();
    }

    @Test
    public void loadData() {

        csvProcessorMockedStatic.when(() -> CsvProcessor.processIotDataFile(Mockito.anyString()))
                .thenReturn(Mockito.anyList());
        IotResponseDTO iotResponseDTO = iotService.loadData(iotRequestDTO);

        Assertions.assertThat(iotResponseDTO).isNotNull();
        Assertions.assertThat((iotResponseDTO.getDescription())).isEqualTo(Constant.DATA_REFRESHED);
    }

    @Test
    public void loadDataForFileNotFoundException() {

        csvProcessorMockedStatic.when(() -> CsvProcessor.processIotDataFile(Mockito.anyString()))
                .thenThrow(FileNotFoundException.class);

        Assertions.assertThatThrownBy(() -> iotService.loadData(iotRequestDTO)).isInstanceOf(IotCustomException.class)
                .hasMessage(Constant.NO_DATA_FILE_FOUND_EXCEPTION);

    }

    @Test
    public void reportDevice() {

        IotData iotData1 = new IotData();
        iotData1.setDateTime(1582605077000L);
        iotData1.setEventId(10001L);
        iotData1.setProductId("WG11155638");
        iotData1.setLatitude(new BigDecimal("51.5185"));
        iotData1.setLongitude(new BigDecimal("-0.1736"));
        iotData1.setBattery(99);
        iotData1.setLight(Optional.of(false));
        iotData1.setAirplaneMode(Optional.of(false));

        IotData iotData2 = new IotData();
        iotData2.setDateTime(1582605077000L);
        iotData2.setEventId(10014L);
        iotData2.setProductId("6900233111");
        iotData2.setLatitude(null);
        iotData2.setLongitude(null);
        iotData2.setBattery(10);
        iotData2.setLight(Optional.empty());
        iotData2.setAirplaneMode(Optional.of(false));

        List<IotData> mockIotDataList = new ArrayList<IotData>();
        mockIotDataList.add(iotData1);
        mockIotDataList.add(iotData2);

        Mockito.when(iotRepository.findByProductIdAndDateTimeLessThanEqual(Mockito.anyString(), Mockito.any(Long.class)))
                .thenReturn(mockIotDataList);

        IotResponseDTO iotResponseDTO = iotService.reportDevice("WG11155638", Optional.of("1582605077000"));

        Assertions.assertThat(iotResponseDTO).isNotNull();
        Assertions.assertThat(iotResponseDTO.getId()).isEqualTo("WG11155638");
        Assertions.assertThat(iotResponseDTO.getDatetime()).isEqualTo("25/02/2020 10:01:17");
        Assertions.assertThat(iotResponseDTO.getLat()).isEqualTo("51.5185");
        Assertions.assertThat(iotResponseDTO.getLongitude()).isEqualTo("-0.1736");
        Assertions.assertThat(iotResponseDTO.getStatus()).isEqualTo("N/A");
        Assertions.assertThat(iotResponseDTO.getBattery()).isEqualTo("Full");
        Assertions.assertThat(iotResponseDTO.getDescription()).isEqualTo(Constant.LOCATION_IDENTIFIED);

    }

    @Test
    public void reportDeviceFORNoSuchElementException() {
        Mockito.when(iotRepository.findByProductIdAndDateTimeLessThanEqual(Mockito.anyString(), Mockito.any(Long.class)))
                .thenReturn(Collections.emptyList());

        Assertions.assertThatThrownBy(() -> iotService.reportDevice("0", Optional.of("1582605077000")))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void reportDeviceForDeviceLocationNotFoundException() {

        IotData iotData1 = new IotData();
        iotData1.setDateTime(1582605077000L);
        iotData1.setEventId(10005L);
        iotData1.setProductId("6900233111");
        iotData1.setBattery(99);
        iotData1.setLight(Optional.of(false));
        iotData1.setAirplaneMode(Optional.of(false));

        Mockito.when(iotRepository.findByProductIdAndDateTimeLessThanEqual(Mockito.anyString(), Mockito.any(Long.class)))
                .thenReturn(Collections.singletonList(iotData1));

        Assertions.assertThatThrownBy(() -> iotService.reportDevice("0", Optional.of("1582605077000")))
                .isInstanceOf(DeviceLocationNotFoundException.class);

    }

    @Test
    public void reportDeviceForNoGpsData(){

        IotData iotData1 = new IotData();
        iotData1.setDateTime(1582605077000L);
        iotData1.setEventId(10005L);
        iotData1.setProductId("6900233111");
        iotData1.setBattery(99);
        iotData1.setLight(Optional.of(false));
        iotData1.setAirplaneMode(Optional.of(true));

        Mockito.when(iotRepository.findByProductIdAndDateTimeLessThanEqual(Mockito.anyString(), Mockito.any(Long.class)))
                .thenReturn(Collections.singletonList(iotData1));


        IotResponseDTO iotResponseDTO = iotService
                .reportDevice("6900233111", Optional.of("1582605077000"));

        Assertions.assertThat(iotResponseDTO).isNotNull();
        Assertions.assertThat(iotResponseDTO.getDatetime()).isEqualTo("25/02/2020 10:01:17");
        Assertions.assertThat(iotResponseDTO.getStatus()).isEqualTo("Inactive");
        Assertions.assertThat(iotResponseDTO.getBattery()).isEqualTo("Full");
        Assertions.assertThat(iotResponseDTO.getDescription()).isEqualTo(Constant.AIRPLANE_MODE_ENABLE);

    }

    @Test
    public void reportDeviceCyclePlusDeviceWithStatusActive() throws Exception {
        // Given
        IotData iot_1 = new IotData();
        iot_1.setDateTime(1582605197000L);
        iot_1.setEventId(10003);
        iot_1.setProductId("WG11155638");
        iot_1.setLatitude(new BigDecimal("51.5185"));
        iot_1.setLongitude(new BigDecimal("-0.1736"));
        iot_1.setBattery(98);
        iot_1.setLight(Optional.of(false));
        iot_1.setAirplaneMode(Optional.of(false));
        IotData iot_2 = new IotData();
        iot_2.setDateTime(1582605257000L);
        iot_2.setEventId(10004);
        iot_2.setProductId("WG11155638");
        iot_2.setLatitude(new BigDecimal("51.5185"));
        iot_2.setLongitude(new BigDecimal("-0.1736"));
        iot_2.setBattery(98);
        iot_2.setLight(Optional.of(false));
        iot_2.setAirplaneMode(Optional.of(false));
        IotData iot_3 = new IotData();
        iot_3.setDateTime(1582605497000L);
        iot_3.setEventId(10011);
        iot_3.setProductId("WG11155638");
        iot_3.setLatitude(new BigDecimal("51.5185"));
        iot_3.setLongitude(new BigDecimal("-0.17538"));
        iot_3.setBattery(95);
        iot_3.setLight(Optional.of(false));
        iot_3.setAirplaneMode(Optional.of(false));

        Mockito.when(iotRepository.findByProductIdAndDateTimeLessThanEqual(Mockito.anyString(), Mockito.any(Long.class)))
                .thenReturn(Arrays.asList(iot_1, iot_2, iot_3));

        IotResponseDTO iotResponseDTO = iotService
                .reportDevice("WG11155638", Optional.of("1582605257000"));

        Assertions.assertThat(iotResponseDTO).isNotNull();
        Assertions.assertThat(iotResponseDTO.getId()).isEqualTo("WG11155638");
        Assertions.assertThat(iotResponseDTO.getName()).isEqualTo("CyclePlusTracker");
        Assertions.assertThat(iotResponseDTO.getDatetime()).isEqualTo("25/02/2020 10:08:17");
        Assertions.assertThat(iotResponseDTO.getLongitude()).isEqualTo("-0.17538");
        Assertions.assertThat(iotResponseDTO.getLat()).isEqualTo("51.5185");
        Assertions.assertThat(iotResponseDTO.getStatus()).isEqualTo("Active");
        Assertions.assertThat(iotResponseDTO.getBattery()).isEqualTo("Full");
        Assertions.assertThat(iotResponseDTO.getDescription()).isEqualTo(Constant.LOCATION_IDENTIFIED);
    }

    @Test
    public void reportDeviceCyclePlusDeviceWithStatusInActive() throws Exception {

        IotData iot_1 = new IotData();
        iot_1.setDateTime(1582605077000L);
        iot_1.setEventId(10001);
        iot_1.setProductId("WG11155638");
        iot_1.setLatitude(new BigDecimal("51.5185"));
        iot_1.setLongitude(new BigDecimal("-0.1736"));
        iot_1.setBattery(99);
        iot_1.setLight(Optional.of(false));
        iot_1.setAirplaneMode(Optional.of(false));
        IotData iot_2 = new IotData();
        iot_2.setDateTime(1582605137000L);
        iot_2.setEventId(10002);
        iot_2.setProductId("WG11155638");
        iot_2.setLatitude(new BigDecimal("51.5185"));
        iot_2.setLongitude(new BigDecimal("-0.1736"));
        iot_2.setBattery(99);
        iot_2.setLight(Optional.of(false));
        iot_2.setAirplaneMode(Optional.of(false));
        IotData iot_3 = new IotData();
        iot_3.setDateTime(1582605197000L);
        iot_3.setEventId(10003);
        iot_3.setProductId("WG11155638");
        iot_3.setLatitude(new BigDecimal("51.5185"));
        iot_3.setLongitude(new BigDecimal("-0.1736"));
        iot_3.setBattery(98);
        iot_3.setLight(Optional.of(false));
        iot_3.setAirplaneMode(Optional.of(false));
        IotData iot_4 = new IotData();
        iot_4.setDateTime(1582605257000L);
        iot_4.setEventId(10004);
        iot_4.setProductId("WG11155638");
        iot_4.setLatitude(new BigDecimal("51.5185"));
        iot_4.setLongitude(new BigDecimal("-0.1736"));
        iot_4.setBattery(98);
        iot_4.setLight(Optional.of(false));
        iot_4.setAirplaneMode(Optional.of(false));

        Mockito.when(iotRepository.findByProductIdAndDateTimeLessThanEqual(Mockito.anyString(), Mockito.any(Long.class)))
                .thenReturn(Arrays.asList(iot_1, iot_2, iot_3, iot_4));

        IotResponseDTO iotResponseDTO = iotService
                .reportDevice("WG11155638", Optional.of("1582605257000"));

        Assertions.assertThat(iotResponseDTO).isNotNull();
        Assertions.assertThat(iotResponseDTO.getId()).isEqualTo("WG11155638");
        Assertions.assertThat(iotResponseDTO.getName()).isEqualTo("CyclePlusTracker");
        Assertions.assertThat(iotResponseDTO.getDatetime()).isEqualTo("25/02/2020 10:04:17");
        Assertions.assertThat(iotResponseDTO.getLongitude()).isEqualTo("-0.1736");
        Assertions.assertThat(iotResponseDTO.getLat()).isEqualTo("51.5185");
        Assertions.assertThat(iotResponseDTO.getStatus()).isEqualTo("Inactive");
        Assertions.assertThat(iotResponseDTO.getBattery()).isEqualTo("Full");
        Assertions.assertThat(iotResponseDTO.getDescription()).isEqualTo(Constant.LOCATION_IDENTIFIED);

    }
}