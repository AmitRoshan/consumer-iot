package com.vodafone.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.*;

public class ConvertOptionalStringToOptionalLongTest {

    @Test
    public void convert() {

        OptionalLong optionalLongVal = ConvertOptionalStringToOptionalLong.convert(Optional.of("845484548488"));

        Assertions.assertThat(optionalLongVal).isNotNull();
        Assertions.assertThat(optionalLongVal).isEqualTo(OptionalLong.of(845484548488L));
    }

    @Test
    public void covertEmptyOptionalStringToEmptyOptionalLong() {

        OptionalLong optionalLongVal = ConvertOptionalStringToOptionalLong.convert(Optional.empty());

        Assertions.assertThat(optionalLongVal).isNotNull();
        Assertions.assertThat(optionalLongVal).isEqualTo(OptionalLong.empty());
    }

    @Test
    public void convertForNumberFormatException(){
        Assertions.assertThatThrownBy(()->ConvertOptionalStringToOptionalLong.convert(Optional.of("A")))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    public void convertForNullPointerException(){
        Assertions.assertThatThrownBy(()->ConvertOptionalStringToOptionalLong.convert(null))
                .isInstanceOf(NullPointerException.class);
    }
}
