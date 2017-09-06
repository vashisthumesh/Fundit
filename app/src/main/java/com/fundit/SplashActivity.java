package com.fundit;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fundit.a.AppPreference;
import com.fundit.a.C;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {


    private static final int TIMEOUT = 1500;

    AppPreference preference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preference = new AppPreference(getApplicationContext());


        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-c")
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (preference.isFirstTime() == false) {

                    Intent intent = new Intent(getApplicationContext(), WalkThroughActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {


                    if (preference.isLoggedIn()) {


                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        if (preference.getMemberData().isEmpty()) {
                            switch (preference.getUserRoleID()) {
                                case C.ORGANIZATION:
                                    intent = new Intent(getApplicationContext(), OrganizationProfileActivity.class);
                                    break;
                                case C.FUNDSPOT:
                                    intent = new Intent(getApplicationContext(), FundSpotProfile.class);
                                    break;
                                case C.GENERAL_MEMBER:
                                    intent = new Intent(getApplicationContext(), GeneralMemberProfileActivity.class);
                                    break;
                            }
                            intent.putExtra("firstTime", true);
                        }


                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                }

            }


        }, TIMEOUT);

    }
}
