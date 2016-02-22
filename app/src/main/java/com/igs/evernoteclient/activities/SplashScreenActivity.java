package com.igs.evernoteclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.igs.evernoteclient.R;

public class SplashScreenActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(!EvernoteSession.getInstance().isLoggedIn()) {
            EvernoteSession.getInstance().authenticate(this);
        }else{
            onLogged();
        }
    }

    @Override
    public void onLoginFinished(boolean successful) {
        if(successful){
            onLogged();
        }else{
            Toast.makeText(this,getString(R.string.error_logging),Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //Start the new activity with the notes
    public void onLogged(){
        Intent newActivity = new Intent(this,MainActivity.class);
        startActivity(newActivity);
        finish();
    }
}
