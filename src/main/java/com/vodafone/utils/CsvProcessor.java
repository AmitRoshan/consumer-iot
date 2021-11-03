package com.vodafone.utils;

import com.opencsv.bean.CsvToBeanBuilder;
import com.vodafone.model.IotData;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Slf4j
public class CsvProcessor {

    public static List<IotData> processIotDataFile(String filepath) throws FileNotFoundException {
        log.info(" processing the data from {} ", filepath);

        return new CsvToBeanBuilder<IotData>(new FileReader(filepath))
                .withSkipLines(1)
                .withSeparator(';')
                .withType(IotData.class)
                .build()
                .parse();
    }
}
