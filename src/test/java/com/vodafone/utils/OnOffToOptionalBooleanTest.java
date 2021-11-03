package com.vodafone.utils;

import com.vodafone.constant.Constant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OnOffToOptionalBooleanTest {

    private static OnOffToOptionalBoolean onOffToOptionalBoolean;

    @BeforeAll
    public  static void setUp(){
        onOffToOptionalBoolean = new OnOffToOptionalBoolean();
    }

    @Test
    public void convertOnSwitchModes() {
        Optional<Boolean> optionalBoolean = onOffToOptionalBoolean.convert(Constant.SwitchModes.ON.getValue());

        Assertions.assertThat(optionalBoolean.isPresent()).isTrue();
        Assertions.assertThat(optionalBoolean.get()).isEqualTo(Boolean.TRUE);
    }

    @Test void convertOffSwitchMode() {
        Optional<Boolean> optionalBoolean = onOffToOptionalBoolean.convert(Constant.SwitchModes.OFF.getValue());

        Assertions.assertThat(optionalBoolean.isPresent()).isTrue();
        Assertions.assertThat(optionalBoolean.get()).isEqualTo(Boolean.FALSE);
    }

    @Test void convertNASwitchMode() {
        Optional<Boolean> optionalBoolean = onOffToOptionalBoolean.convert(Constant.SwitchModes.NA.getValue());

        Assertions.assertThat(optionalBoolean.isPresent()).isFalse();

    }

    @Test void convertForNullPointerException() {

        Assertions.assertThatThrownBy(()->onOffToOptionalBoolean.convert(null))
                .isInstanceOf(NullPointerException.class);

    }
}