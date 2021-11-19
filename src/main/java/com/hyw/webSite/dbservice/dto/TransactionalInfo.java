package com.hyw.webSite.dbservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TransactionalInfo{
    private String transactionId;
    private Method method;
    private LocalDateTime begTime;
    private List<Connection> connectionList = new ArrayList<>();
    private Class<? extends Throwable>[] throwableList;
}
