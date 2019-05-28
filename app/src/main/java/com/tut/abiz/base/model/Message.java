package com.tut.abiz.base.model;


/**
 * Created by abiz on 5/25/2019.
 */

public class Message {

    public static final String MSGIDS = "msgIds";
    public static int RECEIPT = 2;
    public static int SENT = 3;
    private long id;
    private String body;
    private Integer type;
    private String registerDate;
    private Boolean delivered;
    private Long msgId;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }
}

