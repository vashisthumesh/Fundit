package com.fundit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida6 on 25-01-2018.
 */

public class NotificationSettingsModel extends AppModel {

    List<NotificationData> data = new ArrayList<>();

    public List<NotificationData> getData() {
        return data;
    }

    public class NotificationData {


        String id = "";
        String role_id = "";
        String user_id = "";
        String notification_type_id = "";
        String status = "";
        String name = "";

        public String getId() {
            return id;
        }

        public String getRole_id() {
            return role_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getNotification_type_id() {
            return notification_type_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
