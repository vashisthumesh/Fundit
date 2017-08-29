package com.fundit.helper;

import android.content.Context;

import com.alihafizji.library.CreditCardEditText;
import com.fundit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 26-Aug-17.
 */

public class CreditCardPattern implements CreditCardEditText.CreditCartEditTextInterface {

    private Context mContext;

    public CreditCardPattern(Context context) {
        mContext = context;
    }
    @Override
    public List<CreditCardEditText.CreditCard> mapOfRegexStringAndImageResourceForCreditCardEditText(CreditCardEditText creditCardEditText) {
        List<CreditCardEditText.CreditCard> listOfPatterns = new ArrayList<CreditCardEditText.CreditCard>();






        CreditCardEditText.CreditCard Electron = new CreditCardEditText.CreditCard("^(4026|417500|4405|4508|4844|4913|4917)\\\\d+$", mContext.getResources().getDrawable(R.mipmap.visaelectron), "Electron");

        CreditCardEditText.CreditCard Maestro = new CreditCardEditText.CreditCard("^(?:5[0678]\\\\d\\\\d|6304|6390|67\\\\d\\\\d)\\\\d{8,15}$", mContext.getResources().getDrawable(R.mipmap.maestro), "Maestro");

        CreditCardEditText.CreditCard DinersClub = new CreditCardEditText.CreditCard("^3(?:0[0-5]|[68][0-9])[0-9]{11}$", mContext.getResources().getDrawable(R.mipmap.dinnersclub), "Diners Club");

        CreditCardEditText.CreditCard JCB = new CreditCardEditText.CreditCard("^(?:2131|1800|35[0-9]{3})[0-9]{3,}$", mContext.getResources().getDrawable(R.mipmap.jcb), "JCB");

        CreditCardEditText.CreditCard Discover = new CreditCardEditText.CreditCard("^6(?:011|5[0-9]{2})[0-9]{3,}$", mContext.getResources().getDrawable(R.mipmap.discover), "Discover");

        CreditCardEditText.CreditCard UnionPay = new CreditCardEditText.CreditCard("^62[0-5]\\\\d{13,16}$", mContext.getResources().getDrawable(R.mipmap.unionpay), "JCUnionPayB");

        CreditCardEditText.CreditCard Dankort = new CreditCardEditText.CreditCard("^(5019)\\\\d+$", mContext.getResources().getDrawable(R.mipmap.dankort), "Dankort");

        CreditCardEditText.CreditCard RuPay = new CreditCardEditText.CreditCard("^6[0-9]+$", mContext.getResources().getDrawable(R.mipmap.rupay), "RuPay");


        CreditCardEditText.CreditCard AmericanExpress = new CreditCardEditText.CreditCard("^3[47][0-9]{5,}$", mContext.getResources().getDrawable(R.mipmap.american_express), "AmericanExpress");

        CreditCardEditText.CreditCard visa = new CreditCardEditText.CreditCard("^4[0-9]{12}(?:[0-9]{3})?$", mContext.getResources().getDrawable(R.drawable.visa), "visa");

        CreditCardEditText.CreditCard MasterCard = new CreditCardEditText.CreditCard("^5[1-5][0-9]{14}$", mContext.getResources().getDrawable(R.drawable.mastercard), "MasterCard");


        listOfPatterns.add(Electron);
        listOfPatterns.add(Maestro);
        listOfPatterns.add(DinersClub);
        listOfPatterns.add(JCB);
        listOfPatterns.add(Discover);
        listOfPatterns.add(UnionPay);
        listOfPatterns.add(Dankort);
        listOfPatterns.add(RuPay);
        listOfPatterns.add(AmericanExpress);
        listOfPatterns.add(visa);
        listOfPatterns.add(MasterCard);
        return listOfPatterns;
    }
}
