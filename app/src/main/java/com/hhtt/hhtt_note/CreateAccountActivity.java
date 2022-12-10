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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText , passEditText , confirmPassEditText;
    Button createbtn;
    ProgressBar progressbar;
    TextView loginbtnTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.Email_edit_text);
        passEditText = findViewById(R.id.Password_edit_text);
        confirmPassEditText = findViewById(R.id.ConfirmPassword_edit_text);
        progressbar = findViewById(R.id.progress_bar);
        loginbtnTextview = findViewById(R.id.login_text_btn);
        createbtn = findViewById(R.id.Create_Acc_btn);

        createbtn.setOnClickListener(v-> createAccount());
        loginbtnTextview.setOnClickListener(v-> startActivity(new Intent(CreateAccountActivity.this,LoginActivity.class)));
    }

    void createAccount(){
        String email = emailEditText.getText().toString();
        String pass = passEditText.getText().toString();
        String confirmpass = confirmPassEditText.getText().toString();

        boolean isValidated = validateData(email,pass,confirmpass);

        if(!isValidated)
            return;
        createAccountInFirebase(email,pass);
    }

    void createAccountInFirebase(String email, String pass){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            Utility.showToast(CreateAccountActivity.this,"Tạo tài khoản thành công, vui lòng kiểm tra Email để xác thực");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else{
                            Utility.showToast(CreateAccountActivity.this,task.getException().getLocalizedMessage());
                        }
                    }
                }
                );
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressbar.setVisibility(View.VISIBLE);
            createbtn.setVisibility(View.GONE);
        }else{
            progressbar.setVisibility(View.GONE);
            createbtn.setVisibility(View.VISIBLE);
        }

    }

    boolean validateData(String email, String pass , String confirmpass){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email không hợp lệ");
            return false;
        }
        if(pass.length()<6){
            passEditText.setError("Mật khẩu không hợp lệ");
        }
        if(!pass.equals(confirmpass)){
            confirmPassEditText.setError("Mật khẩu phải trùng nhau");
        }
        return true;
    }
}