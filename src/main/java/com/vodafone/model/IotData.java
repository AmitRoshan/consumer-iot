package com.vodafone.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.vodafone.constant.Constant;
import com.vodafone.utils.OnOffToOptionalBoolean;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Optional;

@Data
public class IotData {

    @CsvBindByName(column = "DateTime", required = true)
    private long dateTime;

    @CsvBindByName(column = "EventId", required = true)
    private long eventId;

    @CsvBindByName(column = "ProductId", required = true)
    private String productId;

    @CsvBindByName(column = "Latitude")
    private BigDecimal latitude;

    @CsvBindByName(column = "Longitude")
    private BigDecimal longitude;

    @CsvBindByName(column = "Battery")
    private double battery;

    @CsvCustomBindByName(column = "Light", converter = OnOffToOptionalBoolean.class)
    private Optional<Boolean> light;

    @CsvCustomBindByName(column = "AirplaneMode", converter = OnOffToOptionalBoolean.class)
    private Optional<Boolean> airplaneMode;

    public String getBatteryStatus() {

        String status = Constant.BatteryStatus.BATTERY_CRITICAL.getStatus();

        if(this.battery >= 0.98)
            status =  Constant.BatteryStatus.BATTERY_FULL.getStatus();
        else if(this.battery >= 0.60)
            status = Constant.BatteryStatus.BATTERY_HIGH.getStatus();
        else if(this.battery >= 0.40)
            status = Constant.BatteryStatus.BATTERY_MEDIUM.getStatus();
        else if(this.battery >=10)
            status =Constant.BatteryStatus.BATTERY_LOW.getStatus();

        return status;
    }


    public String getProductName() {
        String productName = Constant.ProductName.UNKNOWN_TRACKER.toString();

        if(this.productId.startsWith(Constant.ProductId.CPT_START_WITH.toString()))
            productName = Constant.ProductName.CYCLE_PLUS_TRACKER.toString();
        else if(this.productId.startsWith(Constant.ProductId.GT_START_WITH.toString()))
            productName = Constant.ProductName.GENERAL_TRACKER.toString();

        return productName;

    }
}
