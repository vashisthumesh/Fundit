package com.fundit.Bean;

/**
 * Created by prince on 4/14/2017.
 */

public class Bean_Notification_history {


    String id;
    String sent_user;
    String receive_user;
    String msg;
    String read_status;

    String role_id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getSent_user() {
        return sent_user;
    }

    public void setSent_user(String sent_user) {
        this.sent_user = sent_user;
    }
    public String getReceive_user() {
        return receive_user;
    }

    public void setReceive_user(String receive_user) {
        this.receive_user = receive_user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }
    public Bean_Notification_history() {
    }
    public Bean_Notification_history(String id, String sent_user, String receive_user, String msg,String read_status,String role_id) {

        this.id = id;
        this.sent_user = sent_user;
        this.receive_user = receive_user;
        this.msg = msg;
        this.read_status = read_status;
        this.role_id=role_id;
    }

}
