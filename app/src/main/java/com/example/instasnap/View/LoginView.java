package com.example.instasnap.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import com.example.instasnap.Utils.FirebaseHandler;
import com.example.instasnap.View.User.UserView;
import com.example.instasnap.ViewModel.LoginViewModel;
import com.example.instasnap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginView extends AppCompatActivity {

    private EditText _editTextEmail;
    private EditText _editTextPassword;
    private Button _loginButton;
    private FirebaseAuth _mAuth;
    private ProgressBar _loginProgressBar;
    private TextView _registerHereTextView;

    private LoginViewModel loginViewModel;
    private CheckBox _saveCredentialsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        initialize();

        setRegisterNowButtonListener();

        setLoginButtonListener();

        checkForSavedCredentials();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = _mAuth.getCurrentUser();
        if(currentUser != null){
            Intent homepageIntent = new Intent(getApplicationContext(), UserView.class);
            startActivity(homepageIntent);
            finish();
        }
    }

    private void initialize() {
        // Initializing registration objects
        _mAuth = FirebaseAuth.getInstance();
        FirebaseHandler firebaseHandler = new FirebaseHandler(getApplicationContext());
       // firebaseHandler.loadDataToFirebase();
       // firebaseHandler.readDataFromFirebase();
        _editTextEmail = findViewById(R.id.login_email_text_input);
        _editTextPassword = findViewById(R.id.login_password_text_input);
        _loginButton = findViewById(R.id.login_button);
        _loginProgressBar = findViewById(R.id.login_progressBar);
        _registerHereTextView = findViewById(R.id.register_now);
        _saveCredentialsCheckBox = findViewById(R.id.save_credentials_checkbox);
    }

    private void setLoginButtonListener() {
        // Setting listener for login button
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _loginProgressBar.setVisibility(View.VISIBLE);
                String email = String.valueOf(_editTextEmail.getText());
                String password = String.valueOf(_editTextPassword.getText());

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginView.this, "Email or password is empty", Toast.LENGTH_SHORT).show();
                    _loginProgressBar.setVisibility(View.GONE);
                    return;
                }

                _mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                _loginProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginView.this, "Login Succeed",
                                            Toast.LENGTH_SHORT).show();
                                    Intent homepageIntent = new Intent(getApplicationContext(), UserView.class);
                                    startActivity(homepageIntent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginView.this, "Login failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                // Save the Email and Password of the user with SharedPreference
                if (_saveCredentialsCheckBox.isChecked())
                    saveLoginCredentials();
                 else
                    clearSavedLoginCredentials();

            }
        });

    }

    private void setRegisterNowButtonListener() {
        // Setting listener for register now button
        _registerHereTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterView.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }

    private void checkForSavedCredentials() {

        // Check if there are any saved login credentials and restore them if the checkbox was previously checked
        loginViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LoginViewModel.class);

        // Restore saved credentials if the checkbox was previously checked
        if (loginViewModel.isLoginCredentialsSaved()) {
            String savedEmail = loginViewModel.getEmail();
            String savedPassword = loginViewModel.getPassword();
            _editTextEmail.setText(savedEmail);
            _editTextPassword.setText(savedPassword);
            _saveCredentialsCheckBox.setChecked(true);
        }

        _saveCredentialsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    clearSavedLoginCredentials();
            }
        });
    }

    private void saveLoginCredentials() {
        String email = _editTextEmail.getText().toString().trim();
        String password = _editTextPassword.getText().toString().trim();
        loginViewModel.saveLoginCredentials(email, password);
    }

    private void clearSavedLoginCredentials() {
        loginViewModel.clearLoginCredentials();
    }
}