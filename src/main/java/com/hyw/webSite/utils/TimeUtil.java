package com.hyw.webSite.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
public class TimeUtil {

    public static LocalDateTime intTime2LocalDateTime(long timeSecond) {
        return LocalDateTime.ofEpochSecond(timeSecond, 0, ZoneOffset.ofHours(+8));
    }
}
