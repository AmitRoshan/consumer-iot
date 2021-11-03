package com.vodafone.utils;

import com.vodafone.model.IotData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvProcessorTest {

    @Test
    public void processIotDataFile() throws FileNotFoundException {

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
        iotData2.setDateTime(1582612875000L);
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


        MockedStatic<CsvProcessor> csvProcessorMockedStatic = Mockito.mockStatic(CsvProcessor.class);
        csvProcessorMockedStatic.when(() -> CsvProcessor.processIotDataFile(Mockito.anyString())).thenReturn( mockIotDataList);


        List<IotData> iotDataList = CsvProcessor.processIotDataFile("");
        Assertions.assertThat(iotDataList).isNotNull().hasSize(2);
        Assertions.assertThat(iotDataList.get(0)).isEqualTo(iotData1);
    }

    @Test
    public void processIotDataFileInvalidPath() throws FileNotFoundException {

        Assertions.assertThatThrownBy(()->CsvProcessor.processIotDataFile(""))
                .isInstanceOf(FileNotFoundException.class).hasMessage(" (The system cannot find the path specified)");
    }
}