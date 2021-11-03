package com.vodafone.utils;

import com.opencsv.bean.AbstractBeanField;
import com.vodafone.constant.Constant;

import java.util.Optional;

public class OnOffToOptionalBoolean extends AbstractBeanField {

    @Override
    protected Optional<Boolean> convert(String s) {

         return s.equalsIgnoreCase(Constant.SwitchModes.ON.getValue()) ? Optional.of(Boolean.TRUE) :
                 s.equalsIgnoreCase(Constant.SwitchModes.OFF.getValue()) ? Optional.of(Boolean.FALSE) : Optional.empty();
    }
}
