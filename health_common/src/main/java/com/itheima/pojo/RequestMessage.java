package com.itheima.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RequestMessage implements Serializable {


    String model="ernie-4.0-turbo-8k";
    List<Message> messages = new ArrayList<>();

    /**
     * 范围(0~1.0]
     * 较高的数值会使输出更加随机
     *
     */
    float temperature = Float.parseFloat("0.95");

    /**
     * 影响文本的多样性，取值越大生成的文本多样性越强
     * 建议该参数与temperature只设置一个。建议top_p和temperature不要同时更改
     */
    float top_p = Float.parseFloat("0.8");

    /**
     * 通过对已生成的token增加惩罚，减少重复生成的现象
     * 值越大，惩罚越大
     * 取值范围[1,2]
     */
    float penalty_score = Float.parseFloat("1.0");

    /**
     * 是否以流式接口形式返回数据
     */
    boolean stream = false;

    /**
     * 模型人设
     */
    String system = null;

    /**
     * 表示用户唯一标识符，用于监测和检测滥用行为。防止接口恶意调用。
     */
    String user_id = "";

    public void addMessage(Message message){
        this.messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getTop_p() {
        return top_p;
    }

    public void setTop_p(float top_p) {
        this.top_p = top_p;
    }

    public float getPenalty_score() {
        return penalty_score;
    }

    public void setPenalty_score(float penalty_score) {
        this.penalty_score = penalty_score;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
