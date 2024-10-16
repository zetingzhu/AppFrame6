package com.zzt.banvp.entiy;

import java.io.Serializable;

/**
 * @author: zeting
 * @date: 2023/2/28
 */
public class ImgObj implements Serializable {
    private Integer imgId ;
    private String body ;

    public ImgObj(Integer imgId, String body) {
        this.imgId = imgId;
        this.body = body;
    }

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
