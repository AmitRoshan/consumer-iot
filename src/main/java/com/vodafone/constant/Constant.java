package com.vodafone.constant;

public class Constant {

    public static final String DATA_REFRESHED = "data refreshed";
    public static final String LOCATION_IDENTIFIED = "SUCCESS: Location identified.";
    public static final String AIRPLANE_MODE_ENABLE = "SUCCESS: Location not available: Please turn off airplane mode";


    //Error Message
    public static final String NO_DATA_FILE_FOUND_EXCEPTION = "ERROR: no data file found";
    public static final String TECHNICAL_EXCEPTION = "ERROR: A technical exception occurred";
    public static final String DEVICE_NOT_LOCATED_EXCEPTION = "ERROR: Device could not be located";
    public static final String ID_NOT_FOUND_EXCEPTION = "ERROR: Id %s not found";


    public enum SwitchModes {

        ON("ON"),
        OFF("OFF"),
        NA("N/A");

        private final String value;

        SwitchModes(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum BatteryStatus {
        BATTERY_FULL("Full"),
        BATTERY_HIGH("High"),
        BATTERY_MEDIUM("Medium"),
        BATTERY_LOW("Low"),
        BATTERY_CRITICAL("Critical");

        private final String status;

        BatteryStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    public enum ProductName {

        CYCLE_PLUS_TRACKER("CyclePlusTracker"),
        GENERAL_TRACKER("GeneralTracker"),
        UNKNOWN_TRACKER("Unknown");

        private final String value;

        ProductName(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum ProductId {

        CPT_START_WITH("WG"),
        GT_START_WITH("69");

        private final String value;

        ProductId(String value){
            this.value =  value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum DeviceStatus {

        STATUS_ACTIVE("Active"),
        STATUS_INACTIVE("Inactive"),
        STATUS_NA("N/A");

        private final String value;

        DeviceStatus(String value){
            this.value = value;
        }


        @Override
        public String toString() {
            return value;
        }
    }
}
