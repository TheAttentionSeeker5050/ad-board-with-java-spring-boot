package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemDeletedResponse {
    public String message;
    public String errorMessage;
    public String parentUrl;

    public ItemDeletedResponse(String message, String errorMessage, String parentUrl) {
        this.message = message;
        this.errorMessage = errorMessage;
        this.parentUrl = parentUrl;
    }
}
