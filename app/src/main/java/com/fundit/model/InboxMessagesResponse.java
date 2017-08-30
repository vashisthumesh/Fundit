package com.fundit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 29-Aug-17.
 */

public class InboxMessagesResponse extends AppModel {

    List<MessageResponseData> data = new ArrayList<>();

    public List<MessageResponseData> getResponseDataList() {
        return data;
    }

    public class MessageResponseData {


        Inbox Inbox = new Inbox();
        InboxReceiveUser ReceiverUser = new InboxReceiveUser();
        InboxSendUser SenderUser = new InboxSendUser();


        public com.fundit.model.Inbox getInbox() {
            return Inbox;
        }

        public InboxReceiveUser getReceiverUser() {
            return ReceiverUser;
        }

        public InboxSendUser getSenderUser() {
            return SenderUser;
        }
    }
}
