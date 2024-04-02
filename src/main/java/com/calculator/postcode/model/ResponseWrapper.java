package com.calculator.postcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T> {

    private boolean status;
    private String errorCode;
    private String errorMessage;
    private T data;

    public ResponseWrapper<T> success(T data) {
        this.status = true;
        this.data = data;
        return this;
    }

    public ResponseWrapper<T> fail(String errorCode, String errorMessage) {
        this.status = false;
        this.data = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        return this;
    }

}
