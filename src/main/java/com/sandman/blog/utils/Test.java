package com.sandman.blog.utils;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void main(String[] args){
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtils.getUuidStr());
    }
}
