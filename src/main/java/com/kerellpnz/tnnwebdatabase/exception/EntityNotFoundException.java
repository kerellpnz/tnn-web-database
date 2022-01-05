package com.kerellpnz.tnnwebdatabase.exception;

import java.io.Serializable;

public class EntityNotFoundException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;

    public EntityNotFoundException() {
        this("Запрашиваемый объект отсутствует в базе данных!");
    }

    public EntityNotFoundException(String message) {
        this.message = System.currentTimeMillis() + ": " + message;
    }

    public String getMessage() {
        return message;
    }
}
