package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class OcrResponseDto implements Serializable {
    private TextsDto texts;
    private double score;
    private String logId; // 对应 JSON 中的 log_id

    // 必须有无参构造函数（Jackson 反序列化需要）
    public OcrResponseDto() {}

    // Getter & Setter
    public TextsDto getTexts() { return texts; }
    public void setTexts(TextsDto texts) { this.texts = texts; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    @JsonProperty("log_id") // 映射 JSON 中的 log_id 字段
    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
}
