package com.hyw.webSite.web.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true )
public class WebElementDto {
    private String type; // div/labelInput/button/
    private String pageId;
    private String parentElement;
    private Object data;
}
