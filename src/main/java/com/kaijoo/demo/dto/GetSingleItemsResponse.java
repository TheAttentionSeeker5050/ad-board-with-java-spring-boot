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

    public GetSingleItemsResponse(String errorMessage, String url, String parentUrl, Object data) {
        this.errorMessage = errorMessage;
        this.url = url;
        this.parentUrl = parentUrl;
        this.data = data;
    }
}
