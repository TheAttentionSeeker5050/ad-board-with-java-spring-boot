package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemCreatedOrUpdatedResponse {
    public String message;
    public String errorMessage;
    public String parentUrl;
    public String itemUrl;

    public ItemCreatedOrUpdatedResponse(String message, String errorMessage, String parentUrl, String itemUrl) {
        this.message = message;
        this.errorMessage = errorMessage;
        this.parentUrl = parentUrl;
        this.itemUrl = itemUrl;

    }
}
