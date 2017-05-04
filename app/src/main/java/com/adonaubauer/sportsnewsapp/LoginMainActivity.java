package com.adonaubauer.sportsnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Austin on 5/4/2017.
 */

public class LoginMainActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText edittextUsername;
    EditText edittextPassword;
    Button   buttonSignin;
    Button   buttonSignup;

    CheckBox rememberLoginInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        wireUpWidgets();

        userSignin();

        userSignUp();

    }

    public void wireUpWidgets() {

        setupToolbar();

        edittextUsername = (EditText) findViewById(R.id.edittextSigninUsername);

        edittextPassword = (EditText) findViewById(R.id.edittextSigninPassword);

        buttonSignin     = (Button)   findViewById(R.id.btnSignin);

        buttonSignup     = (Button)   findViewById(R.id.btnSignup);

        rememberLoginInfo = (CheckBox) findViewById(R.id.checkboxRememberLoginInfo);

    }

    public void setupToolbar() {

        toolbar          = (Toolbar)  findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

    }

    public void userSignin() {

        buttonSignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO Get edittextUsername if not null and check if user is in database
                //TODO Set error is username text field is empty
                //TODO Set error if password text field is empty
                //TODO Get edittextPassword if not null and check if user password is in database
                //TODO If user is signup and username and password are correct, send them to main activity
                //TODO If user is not signedup and username and password are incorrect, Toast/Snackbar/Dialog box message that the user should signup first before trying to sign in

                if (edittextUsername.getText().toString().isEmpty()) {

                    edittextUsername.setError("PLEASE ENTER YOUR USERNAME");

                }

                if (edittextPassword.getText().toString().isEmpty()) {

                    edittextPassword.setError("PLEASE ENTER YOUR PASSWORD");

                }

                User user;

                MyRegistrationDatabaseHandler myRegistrationDatabaseHandler = new MyRegistrationDatabaseHandler(getApplicationContext(), null, null, 1);

                user = myRegistrationDatabaseHandler.findUser(edittextUsername.getText().toString(), edittextPassword.getText().toString());

                if (user != null) {

                    Intent startSportsNewsAppMainActivity = new Intent(getApplicationContext(), MainActivity.class);

                    if (rememberLoginInfo.isChecked()) {

                        startSportsNewsAppMainActivity.putExtra("username", user.getUsername());

                        startSportsNewsAppMainActivity.putExtra("password", user.getPassword());

                        startActivity(startSportsNewsAppMainActivity);

                    } else {

                        startActivity(startSportsNewsAppMainActivity);

                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Sign in was unsuccessfull, try re-entering user info or click signup button to get started", Toast.LENGTH_LONG).show();

                }

            }

        });

    }

    public void userSignUp() {

        buttonSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO Send user to SignupActivity

                //Toast.makeText(getApplicationContext(), "THANK YOU FOR SIGNING UP, BUT IT IS CURRENTLY UNAVAILABLE", Toast.LENGTH_LONG).show();

                Intent signupActivityIntent = new Intent(getApplicationContext(), SignupActivity.class);

                startActivity(signupActivityIntent);

            }

        });

    }

}
