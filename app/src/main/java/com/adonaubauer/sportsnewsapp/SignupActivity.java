package com.adonaubauer.sportsnewsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Austin on 5/4/2017.
 */

public class SignupActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText edittextUsername;
    EditText edittextPassword;
    EditText edittextConfirmPassword;

    Button buttonSubmit;
    Button buttonCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        wireUpWidgets();

        userSubmit();

        userCancel();

    }

    public void wireUpWidgets() {

        setupToolbar();

        edittextUsername = (EditText) findViewById(R.id.edittextSignupUsername);

        edittextPassword = (EditText) findViewById(R.id.edittextSignupPassword);

        edittextConfirmPassword = (EditText) findViewById(R.id.edittextSignupConfirmPassword);

        buttonSubmit = (Button) findViewById(R.id.btnSubmitRegistration);

        buttonCancel = (Button) findViewById(R.id.btnCancelSignup);

    }

    public void setupToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

    }

    public void userSubmit() {

        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO Get edittextUsername if not null
                //TODO Get edittextPassword if not null
                //TODO Get edittextPassword if it matches edittextConfirmPassword
                //TODO add user to database
                //TODO send user to main screen

                if (edittextUsername.getText().toString().isEmpty()) {

                    edittextUsername.setError("PLEASE ENTER A USERNAME");

                } else if (edittextPassword.getText().toString().isEmpty()) {

                    edittextPassword.setError("PLEASE ENTER A PASSWORD");

                } else if (edittextPassword.getText().toString().equals(edittextConfirmPassword.getText().toString())) {

                    edittextConfirmPassword.setError("CONFIRM PASSWORD DOESN'T MATCH PASSWORD");

                } else {

                    newUser(v);

                    Intent userSignupSuccessIntent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(userSignupSuccessIntent);

                }

            }

        });

    }

    public void userCancel() {

        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO Create dialog box and ask user if they are sure they want to cancel signup
                //TODO if user click yes they are sure, send the user back to main login screen
                //TODO if user click no they are not sure, then close dialog box

                //Toast.makeText(getApplicationContext(), "CANCEL SIGNUP", Toast.LENGTH_LONG).show();

                final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                builder.setMessage("CANCEL SIGNUP");

                builder.setPositiveButton("YES CANCEL SIGNUP", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        onBackPressed();

                    }

                });

                builder.setNegativeButton("NO CLOSE DIALOG BOX", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }

                });

            }

        });

    }

    public void newUser(View view) {

        MyRegistrationDatabaseHandler myRegistrationDatabaseHandler = new MyRegistrationDatabaseHandler(getApplicationContext(), null, null, 1);

        int id = (int) (Math.random() * 100);

        String username = edittextUsername.getText().toString();

        String password = edittextPassword.getText().toString();

        User user = new User(id, username, password);

        myRegistrationDatabaseHandler.addUser(user);

    }

}
