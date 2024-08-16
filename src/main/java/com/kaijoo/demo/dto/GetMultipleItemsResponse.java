package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetMultipleItemsResponse {
    public String errorMessage;
    public String url;

    // The data that can be any structure, so it is an object
    public List data;

    public GetMultipleItemsResponse(String errorMessage, String url, List data) {
        this.errorMessage = errorMessage;
        this.url = url;
        this.data = data;
    }
}
