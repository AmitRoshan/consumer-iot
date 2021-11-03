package com.vodafone.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.OptionalLong;

@Slf4j
public class ConvertOptionalStringToOptionalLong {

    public static OptionalLong convert(Optional<String> input) {
        log.info("input Optional<String> : {} ", input.orElse(null));

        OptionalLong output = input.map(s -> OptionalLong.of(Long.parseLong(s))).orElse(OptionalLong.empty());

        log.info("output OptionalLong : {}", output);
        return  output;
    }
}
