package com.example.lightchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    Boolean loginModeActive  = false;
    EditText usernameditText ;
    EditText passwordEditText ;
    Button loginSignupButton;
    TextView loginToggleTexView;

    public void redirectIfLoggedIn()
    {
        if (ParseUser.getCurrentUser() != null)
        {
            Intent intent = new Intent(getApplicationContext() , UserListActivity.class);
            startActivity(intent);

        }
    }
    public void toggleLoginMode(View view)
    {


       if (loginModeActive)
       {
           loginModeActive = false;
           loginSignupButton.setText("Sign Up");
           loginToggleTexView.setText("or Login");

       }
       else
       {
           loginModeActive = true;
           loginSignupButton.setText("Login");
           loginToggleTexView.setText("or Sign Up");
       }
    }
    public void loginSignup(View view)
    {
        if (loginModeActive)
        { //we login the user
        ParseUser.logInInBackground(usernameditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
             if (e==null)
             {
                 Log.i("Info" , "user logged in");
                 redirectIfLoggedIn();
             }
             else{
                 //to give the proper message in toast we make some checks
                 String message = e.getMessage();
                 //chck if the message contains "java"
                 if(message.toLowerCase().contains("java"))
                 {
                     message = e.getMessage().substring(e.getMessage().indexOf(" "));
                 }
                 // otherwise we will just toast e.getMessage() .......no need to split

                 Toast.makeText(MainActivity.this,message , Toast.LENGTH_SHORT).show();
             }
            }
        });


        }else{
            //we sign the user up
            ParseUser user = new ParseUser();
            user.setUsername(usernameditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                    {
                        Log.i("Info" , "user signed up");
                        redirectIfLoggedIn();

                    }
                    else{
                        //to give the proper message in toast we make some checks
                        String message = e.getMessage();
                        //chck if the message contains "java"
                        if(message.toLowerCase().contains("java"))
                        {
                            message = e.getMessage().substring(e.getMessage().indexOf(" "));
                        }
                        // otherwise we will just toast e.getMessage() .......no need to split

                        Toast.makeText(MainActivity.this,message , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginSignupButton = findViewById(R.id.loginSignupButton);
        loginToggleTexView = findViewById(R.id.loginToggleTexView);

        redirectIfLoggedIn();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }
}
