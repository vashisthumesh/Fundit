package com.fundit.model;

/**
 * Created by prince on 7/17/2017.
 */

public class VerifyResponse extends AppModel {

    VerifyResponseData data = new VerifyResponseData();

    public VerifyResponseData getData() {
        return data;
    }

    public class VerifyResponseData {
        User User = new User();
        Member Member = new Member();


        /*Fundspot Fundspot = new Fundspot();
        Organization Organization = new Organization();
*/

        public com.fundit.model.Member getMember() {
            return Member;
        }

        public com.fundit.model.User getUser() {
            return User;
        }

        /*public com.fundit.model.Fundspot getFundspot() {
            return Fundspot;
        }

        public com.fundit.model.Organization getOrganization() {
            return Organization;
        }
*/
    }
}
