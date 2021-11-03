package com.vodafone.repository;

import com.vodafone.model.IotData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class IotRepository {

    private List<IotData> persistIotData =  new ArrayList<>();

    public void saveData(List<IotData> iotData) {
        log.info("persisting the iot data...");

        persistIotData = iotData;
    }

    public List<IotData> findByProductIdAndDateTimeLessThanEqual(String productId, Long dateTime) {
        log.info("Filtering iot data based on productId and dateTime");

        return persistIotData.stream().filter(iotData -> iotData.getProductId().equals(productId))
                .filter(iotData -> iotData.getDateTime() <= dateTime).collect(Collectors.toList());
    }
}
