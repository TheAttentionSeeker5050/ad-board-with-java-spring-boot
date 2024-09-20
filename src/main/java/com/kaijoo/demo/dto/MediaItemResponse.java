package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MediaItemResponse {
    public int id = 0;
    public String itemType = "";
    public String link = "";
    public String iconLink = "";
    public String title = "";
    public String alt = "";

    public MediaItemResponse(int id, String itemType, String link, String iconLink, String title, String alt) {
        this.id = id;
        this.itemType = itemType;
        this.link = link;
        this.iconLink = iconLink;
        this.title = title;
        this.alt = alt;
    }
}
