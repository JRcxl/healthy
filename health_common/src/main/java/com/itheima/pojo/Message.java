package com.itheima.pojo;

import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
public class Message implements Serializable {
    String role;

    /**
     * 对话内容。
     */
    String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
