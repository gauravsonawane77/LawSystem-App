package com.example.lawsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class loginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin,buttonCreateAccount;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty()) {
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
                return;
            }

            progressDialog.show();
            String url = constant.API_BASE_URL+"login.php";

            // Create JSON object to send in the request body
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", editTextEmail.getText());
                jsonObject.put("password", editTextPassword.getText());
                // Add more key-value pairs as needed
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("kalki", "jsonObject: " + jsonObject);
            // Request a JSONObject response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getBoolean("status") == true){
                                    MyPreferences.saveUserInfo(loginActivity.this, response.getString("Name"), response.getString("email"),response.getString("id"),response.getString("role"));
                                    if(response.getString("role").equals("user")) {
                                        Toast.makeText(loginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(loginActivity.this, MainActivity.class));
                                        finish();
                                    }else {
                                        Toast.makeText(loginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(loginActivity.this, LawyerHomeActivity.class));
                                        finish();
                                    }
                                    progressDialog.hide();
                                }else{
                                    Toast.makeText(loginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                    progressDialog.hide();
                                }
                            } catch (JSONException e) {
                                progressDialog.hide();
                                throw new RuntimeException(e);
                            }
                            // Handle the response
//                            Log.d("kalki", "Response: " + response.toString());
                            progressDialog.hide();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            Log.e("kalki", "Error: " + error.toString());
                            progressDialog.hide();
                        }
                    });

            // Add the request to the RequestQueue.
            requestQueue.add(jsonObjectRequest);
        });

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, signupActivity.class));
            }
        });
    }
}