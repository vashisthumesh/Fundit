package com.fundit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida new on 21-Jul-17.
 */

public class FundspotListResponse extends AppModel {
    List<VerifyResponse.VerifyResponseData> data=new ArrayList<>();

    public List<VerifyResponse.VerifyResponseData> getData() {
        return data;
    }
}
