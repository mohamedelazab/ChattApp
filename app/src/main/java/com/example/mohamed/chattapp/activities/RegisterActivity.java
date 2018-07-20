
package com.example.mohamed.chattapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.api.ApiClient;
import com.example.mohamed.chattapp.api.ApiInterface;
import com.example.mohamed.chattapp.model.MainResponse;
import com.example.mohamed.chattapp.utils.Session;
import com.example.mohamed.chattapp.model.User;
import com.example.mohamed.chattapp.utils.InputValidation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    EditText etUsername;
    EditText etEmail;
    EditText etPassRegister;
    EditText etPassRegisterAgain;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setBackgroundDrawable(null);

        ImageView image = findViewById(R.id.imgView_register);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.background);
        image.startAnimation(animation1);

        etUsername =findViewById(R.id.et_username_register);
        etEmail =findViewById(R.id.et_email_register);
        etPassRegister =findViewById(R.id.et_Pass_register);
        etPassRegisterAgain =findViewById(R.id.et_PassAgain_register);
        btnRegister =findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username =etUsername.getText().toString().trim();
                String email =etEmail.getText().toString().trim();
                String pass=etPassRegister.getText().toString();
                String passConfirm=etPassRegisterAgain.getText().toString();
                if (isValidateInputs(username, email, pass, passConfirm)){
                    User user =new User();
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(pass);
                    registerRequest(user);
                }
            }
        });
    }

    private void registerRequest(final User user){
        apiInterface = ApiClient.getApiClient(this).create(ApiInterface.class);
        Call<MainResponse> call =apiInterface.registerUser(user);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() ==1){
                        Toast.makeText(RegisterActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        Session.getInstance().loginUser(user);
                        finish();
                    }
                    else if (response.body().getStatus() ==2){
                        Toast.makeText(RegisterActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FAILURE",t.getMessage());
            }
        });
    }

    private boolean isValidateInputs(String username, String email, String pass, String passConfirm){
        boolean isAllValid =true;
        switch (InputValidation.isUsernameValid(username)){
            case 1:
                isAllValid =false;
                etUsername.setError("spaces not allowed");
                break;
            case 2:
                isAllValid =false;
                etUsername.setError("required.!");
                break;
        }

        if (!InputValidation.isValidEmail(email)){
            isAllValid =false;
            etEmail.setError("Not Email");
        }
        if (!InputValidation.passwordMatch(pass,passConfirm)){
            isAllValid =false;
            etPassRegister.setError("Don't match");
            etPassRegisterAgain.setError("Don't match");
        }
        return isAllValid;
    }
}