package com.kaijoo.demo.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetSingleItemsResponse {
    public String errorMessage;
    public String url;
    public String parentUrl;

    // The data that can be any structure, so it is an object
    public Object data;

    public GetSingleItemsResponse(String errorMessage, String parentUrl, Object data) {
        this.errorMessage = errorMessage;
        this.parentUrl = parentUrl;
        this.data = data;
    }
}
