package com.example.mohamed.chattapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.api.ApiClient;
import com.example.mohamed.chattapp.api.ApiInterface;
import com.example.mohamed.chattapp.model.LoginResponse;
import com.example.mohamed.chattapp.utils.Session;
import com.example.mohamed.chattapp.model.User;
import com.example.mohamed.chattapp.utils.InputValidation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvCreateAccount, tvForgotPassword;
    ApiInterface apiInterface;
    Call<LoginResponse> call;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawable(null);
        progressBar =findViewById(R.id.progress_bar_login);
        etEmail =findViewById(R.id.et_email);
        etPassword =findViewById(R.id.et_Pass);
        btnLogin =findViewById(R.id.btn_login);
        tvCreateAccount =findViewById(R.id.tv_create_account);
        tvForgotPassword =findViewById(R.id.tv_forgot_pass);

        ImageView image = findViewById(R.id.imgView_login);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.background);
        image.startAnimation(animation1);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user =new User();
                if (InputValidation.isValidEmail(etEmail.getText().toString().trim())){
                    user.setEmail(etEmail.getText().toString().trim());
                    if(InputValidation.isNotEmpty(etPassword.getText().toString())) {
                        user.setPassword(etPassword.getText().toString());
                        loginRequest(user);
                    }
                    else {
                        etPassword.setError("Password Required");
                    }
                }
                else {
                    etEmail.setError("Not Email");
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void loginRequest(final User user){
        progressBar.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getApiClient(this).create(ApiInterface.class);
        call =apiInterface.loginUser(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    Log.e("login_response",response.body().getMessage()+"");
                    if (response.body().getStatus() ==1){
                        Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "username: "+response.body().getLoginUser().getUsername(), Toast.LENGTH_LONG).show();
                        user.setId(response.body().getLoginUser().getId());
                        user.setUsername(response.body().getLoginUser().getUsername());
                        user.setEmail(response.body().getLoginUser().getEmail());
                        user.setIsUserAdmin(response.body().getLoginUser().getIsUserAdmin());
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Session.getInstance().loginUser(user);
                        finish();
                    }
                    else if (response.body().getStatus() ==2){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    Log.e("Error",response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FAILURE",t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call !=null){
            call.cancel();
        }
    }
}