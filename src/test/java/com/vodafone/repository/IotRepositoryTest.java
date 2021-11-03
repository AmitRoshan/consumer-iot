package com.vodafone.repository;

import com.vodafone.model.IotData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class IotRepositoryTest {

    @InjectMocks
    private IotRepository iotRepository;

    private List<IotData> checkMockIotListStatus() {
        try {
            Field field = IotRepository.class.getDeclaredField("persistIotData");
            field.setAccessible(true);
            return (List<IotData>) field.get(iotRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<IotData> createIoTList() {
        IotData iotCPT_1 = new IotData();
        iotCPT_1.setDateTime(1582605077000L);
        iotCPT_1.setEventId(10001);
        iotCPT_1.setProductId("WG11155638");
        iotCPT_1.setLatitude(new BigDecimal("51.5185"));
        iotCPT_1.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_1.setBattery(99);
        iotCPT_1.setLight(Optional.of(false));
        iotCPT_1.setAirplaneMode(Optional.of(false));
        IotData iotCPT_2 = new IotData();
        iotCPT_2.setDateTime(1582605137000L);
        iotCPT_2.setEventId(10002);
        iotCPT_2.setProductId("WG11155638");
        iotCPT_2.setLatitude(new BigDecimal("51.5185"));
        iotCPT_2.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_2.setBattery(99);
        iotCPT_2.setLight(Optional.of(false));
        iotCPT_2.setAirplaneMode(Optional.of(false));
        IotData iotCPT_3 = new IotData();
        iotCPT_3.setDateTime(1582605197000L);
        iotCPT_3.setEventId(10003);
        iotCPT_3.setProductId("WG11155638");
        iotCPT_3.setLatitude(new BigDecimal("51.5185"));
        iotCPT_3.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_3.setBattery(98);
        iotCPT_3.setLight(Optional.of(false));
        iotCPT_3.setAirplaneMode(Optional.of(false));

        IotData iotGT_1 = new IotData();
        iotGT_1.setDateTime(1582605257000L);
        iotGT_1.setEventId(10005);
        iotGT_1.setProductId("6900001001");
        iotGT_1.setLatitude(new BigDecimal("40.73061"));
        iotGT_1.setLongitude(new BigDecimal("-73.935242"));
        iotGT_1.setBattery(11);
        iotGT_1.setLight(Optional.empty());
        iotGT_1.setAirplaneMode(Optional.of(false));
        IotData iotGT_2 = new IotData();
        iotGT_2.setDateTime(1582605258000L);
        iotGT_2.setEventId(10006);
        iotGT_2.setProductId("6900001001");
        iotGT_2.setLatitude(new BigDecimal("40.73071"));
        iotGT_2.setLongitude(new BigDecimal("-73.935242"));
        iotGT_2.setBattery(10);
        iotGT_2.setLight(Optional.empty());
        iotGT_2.setAirplaneMode(Optional.of(false));
        IotData iotGT_3 = new IotData();
        iotGT_3.setDateTime(1582605259000L);
        iotGT_3.setEventId(10007);
        iotGT_3.setProductId("6900001001");
        iotGT_3.setLatitude(new BigDecimal("40.73081"));
        iotGT_3.setLongitude(new BigDecimal("-73.935242"));
        iotGT_3.setBattery(10);
        iotGT_3.setLight(Optional.empty());
        iotGT_3.setAirplaneMode(Optional.of(false));

        return Arrays.asList(iotCPT_1, iotCPT_2, iotCPT_3, iotGT_1, iotGT_2, iotGT_3);
    }


    @Test
    public void saveData() {

        List<IotData> mockIoTList = checkMockIotListStatus();
        List<IotData> iotListToStore = createIoTList();

        iotRepository.saveData(iotListToStore);

        Assertions.assertThat(mockIoTList).isNotNull();
        Assertions.assertThat(mockIoTList).hasSize(0);

        List<IotData> persistIotData = null;
        try {
            Field field = IotRepository.class.getDeclaredField("persistIotData");
            field.setAccessible(true);
            persistIotData =  (List<IotData>) field.get(iotRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertThat(persistIotData).isNotNull();
        Assertions.assertThat(persistIotData).hasSize(6);

    }

    @Test
    void findByProductIdAndDateTimeLessThanEqual() {

        iotRepository.saveData(createIoTList());

        IotData iotCPT_1 = new IotData();
        iotCPT_1.setDateTime(1582605077000L);
        iotCPT_1.setEventId(10001);
        iotCPT_1.setProductId("WG11155638");
        iotCPT_1.setLatitude(new BigDecimal("51.5185"));
        iotCPT_1.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_1.setBattery(99);
        iotCPT_1.setLight(Optional.of(false));
        iotCPT_1.setAirplaneMode(Optional.of(false));

        IotData iotCPT_2 = new IotData();
        iotCPT_2.setDateTime(1582605137000L);
        iotCPT_2.setEventId(10002);
        iotCPT_2.setProductId("WG11155638");
        iotCPT_2.setLatitude(new BigDecimal("51.5185"));
        iotCPT_2.setLongitude(new BigDecimal("-0.1736"));
        iotCPT_2.setBattery(99);
        iotCPT_2.setLight(Optional.of(false));
        iotCPT_2.setAirplaneMode(Optional.of(false));

        List<IotData> mockIoTListUpdated = iotRepository.
                findByProductIdAndDateTimeLessThanEqual("WG11155638", 1582605137000L);

        Assertions.assertThat(mockIoTListUpdated).isNotNull();
        Assertions.assertThat(mockIoTListUpdated).hasSize(2);
        Assertions.assertThat(mockIoTListUpdated.get(0)).isEqualTo(iotCPT_1);
        Assertions.assertThat(mockIoTListUpdated.get(1)).isEqualTo(iotCPT_2);
    }
}