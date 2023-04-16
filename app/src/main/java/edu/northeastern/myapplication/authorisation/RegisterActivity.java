package edu.northeastern.myapplication.authorisation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import edu.northeastern.myapplication.MainActivity;
import edu.northeastern.myapplication.R;
import edu.northeastern.myapplication.dao.UserDao;
import edu.northeastern.myapplication.entity.User;
import edu.northeastern.myapplication.utils.Utils;

public class RegisterActivity extends AppCompatActivity {
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private EditText registerUsername, registerEmail, registerPassword, registerConfirmPassword;
    private Button registerBtn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseMessaging firebaseMessaging;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private String cityName;
    // The current phone's registration token oon FirebaseMessaging Service.
    private String userToken;
    private String profileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getUserLocationPermission();
        firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    userToken = task.getResult();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to get registration token", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerUsername = findViewById(R.id.registerUsername);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = registerUsername.getText().toString().trim();
                String email = registerEmail.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();
                String confirmPassword = registerConfirmPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    registerUsername.setError("Username can not be empty.");
                    registerUsername.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    registerEmail.setError("Email can not be empty.");
                    registerEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    registerPassword.setError("Password can not be empty.");
                    registerPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    registerPassword.setError("Length of password must be more than 6.");
                    registerPassword.requestFocus();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    registerConfirmPassword.setError("Password are not matching. Please try again.");
                    registerConfirmPassword.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(RegisterActivity.this, "This email has registered before. Please login instead.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    User user = new User(mAuth.getUid(), username, email, cityName, new ArrayList<>(), userToken, profileImageUrl);
                    UserDao userDao = new UserDao();
                    userDao.create(mAuth.getUid(), user);
                    Toast.makeText(RegisterActivity.this, "You have registered successfully.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                });
            }
        });
    }

    /**
     * Gets the user's location.
     */
    private void getUserLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCityName();
        }

    }

    /**
     * Called when the request permission activity finishes.
     *
     * @param requestCode  The request code
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *                     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCityName();
                    Toast.makeText(RegisterActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "No permission granted.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    /**
     * Gets the city that the user is currently in.
     */
    private void getCityName() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        CancellationToken cancellationToken = cancellationTokenSource.getToken();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(RegisterActivity.this, "Failed to get the location.", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken).addOnSuccessListener(location -> {
            if (location == null) {
                Toast.makeText(RegisterActivity.this, "Failed to get the location.", Toast.LENGTH_SHORT).show();
                return;
            }
            cityName = Utils.getCityName(RegisterActivity.this, location);
        });
    }
}
