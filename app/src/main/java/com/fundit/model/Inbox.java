package com.fundit.model;

/**
 * Created by NWSPL-17 on 29-Aug-17.
 */

public class Inbox {

    String id = "";
    String sender_id = "";
    String receiver_id = "";
    String subject = "";
    String msg = "";
    String status = "";
    String created = "";


    public String getId() {
        return id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getSubject() {
        return subject;
    }

    public String getMsg() {
        return msg;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated() {
        return created;
    }
}
