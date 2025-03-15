package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TextsDto implements Serializable {
    @JsonProperty("text_1") // 映射 JSON 中的 text_1 字段
    private String text1;

    @JsonProperty("text_2") // 映射 JSON 中的 text_2 字段
    private String text2;

    // 必须有无参构造函数
    public TextsDto() {}

    // Getter & Setter
    public String getText1() { return text1; }
    public void setText1(String text1) { this.text1 = text1; }

    public String getText2() { return text2; }
    public void setText2(String text2) { this.text2 = text2; }


}
