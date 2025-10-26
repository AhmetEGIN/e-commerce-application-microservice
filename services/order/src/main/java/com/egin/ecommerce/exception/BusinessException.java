package com.egin.ecommerce.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
public class BusinessException extends RuntimeException {
    private final String msg;

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
