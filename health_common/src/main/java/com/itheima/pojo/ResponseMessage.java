package com.itheima.pojo;

import java.io.Serializable;

public class ResponseMessage implements Serializable {
    String id;

    //回包类型。 chat.completion:多轮对话返回
    String object;

    //时间戳
    int created;

    //表示当前子句的序号。只有在流式接口模式下才会返回该字段
    int sentence_id;

    //表示当前子句是否是最后一句。只有在流式接口模式下会返回该字段。
    boolean is_end;

    //对话返回结果。
    String result;

    /**
     * 表示用户输入是否存在安全，是否关闭当前会话，清理历史回话信息。
     * true：是，表示用户输入存在安全风险，建议关闭当前会话，清理历史会话信息。
     * false：否，表示用户输入无安全风险。
     */
    boolean need_clear_history;

    //token统计信息，token数 = 汉字数+单词数*1.3 （仅为估算逻辑）。
    Usage usage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getSentence_id() {
        return sentence_id;
    }

    public void setSentence_id(int sentence_id) {
        this.sentence_id = sentence_id;
    }

    public boolean isIs_end() {
        return is_end;
    }

    public void setIs_end(boolean is_end) {
        this.is_end = is_end;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isNeed_clear_history() {
        return need_clear_history;
    }

    public void setNeed_clear_history(boolean need_clear_history) {
        this.need_clear_history = need_clear_history;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}
