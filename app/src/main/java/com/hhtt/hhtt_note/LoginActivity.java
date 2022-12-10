package com.hhtt.hhtt_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText , passEditText ;
    Button loginBtn;
    ProgressBar progressbar;
    TextView createAccountbtnTextview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.Email_edit_text);
        passEditText = findViewById(R.id.Password_edit_text);
        progressbar = findViewById(R.id.progress_bar);
        createAccountbtnTextview = findViewById(R.id.create_acc_text_btn);
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener((v)-> loginUser());
        createAccountbtnTextview.setOnClickListener((v)-> startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)));
    }

    void loginUser(){
        String email = emailEditText.getText().toString();
        String pass = passEditText.getText().toString();

        boolean isValidated = validateData(email,pass);

        if(!isValidated)
            return;
        loginAccountInFirebase(email,pass);
    }

    void loginAccountInFirebase(String email,String pass){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()) {
                    //login is success
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        //go to mainactivity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else {
                        Utility.showToast(LoginActivity.this, "Email này chưa được xác thực");

                    }
                }else{
                    //login is failed
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }


    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressbar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressbar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }

    }
    boolean validateData(String email, String pass ){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email không hợp lệ");
            return false;
        }
        if(pass.length()<6){
            passEditText.setError("Mật khẩu không hợp lệ");
        }

        return true;
    }
}