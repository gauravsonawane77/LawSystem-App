package com.example.lawsystem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Button buttonSearch;
    EditText editTextSearchQuery;
    Spinner spinnerSearchCategory;
    private RequestQueue requestQueue;
    private RecyclerView recyclerViewLaws;
    private LawAdapter lawAdapter;
    private List<Law> lawList;

    private RecyclerView recyclerViewLawyers;
    private LawyerAdapter lawyerAdapter;
    private List<Lawyer> lawyerList;

    private RecyclerView recyclerViewCriminals;
    private CriminalAdapter criminalAdapter;
    private List<Criminal> criminalList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        editTextSearchQuery = findViewById(R.id.editTextSearchQuery);
        spinnerSearchCategory = findViewById(R.id.spinnerSearchCategory);
        buttonSearch = findViewById(R.id.buttonSearch);

        recyclerViewLaws = findViewById(R.id.recyclerViewLaws);
        recyclerViewLaws.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewLawyers = findViewById(R.id.recyclerViewLawyers);
        recyclerViewLawyers.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewCriminals = findViewById(R.id.recyclerViewCriminals);
        recyclerViewCriminals.setLayoutManager(new LinearLayoutManager(this));


        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spinnerSearchCategory.getSelectedItem().toString();
                String q = editTextSearchQuery.getText().toString();

                String url = constant.API_BASE_URL+"search.php?type=" + type.toLowerCase() + "&searchTerm=" + q;
                if (type.equals("Laws")) {
                    laws(url);
                } else if (type.equals("Criminals")) {
                    Criminal(url);
                } else if (type.equals("Lawyers")) {
                    lawyer(url);
                }

            }
        });
    }

    private void showLawyerDetailsDialog(int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_lawyer_details, null);

        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewExpertise = view.findViewById(R.id.textViewExpertise);
        TextView textViewCourt = view.findViewById(R.id.textViewCourt);
        TextView textViewContact = view.findViewById(R.id.textViewContact);
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);

        Lawyer lawyer = lawyerList.get(position);
        textViewName.setText(lawyer.getName());
        textViewExpertise.setText(lawyer.getExpertise());
        textViewCourt.setText(lawyer.getCourt());
        textViewContact.setText(lawyer.getContact());
        textViewDescription.setText(lawyer.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void laws(String url) {
        progressDialog.show();
        recyclerViewLaws.setVisibility(View.VISIBLE);
        recyclerViewLawyers.setVisibility(View.GONE);
        recyclerViewCriminals.setVisibility(View.GONE);

        // Populate law list (dummy data)
        lawList = new ArrayList<>();

        lawAdapter = new LawAdapter(this, lawList);
        recyclerViewLaws.setAdapter(lawAdapter);

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            if (response.getBoolean("status") == true) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    lawList.add(new Law(jsonObject.getString("LawText"),
                                            jsonObject.getString("SectionNumber"),
                                            jsonObject.getString("link")
                                    ));
                                }
                                lawAdapter.notifyDataSetChanged();
                                progressDialog.hide();
                            } else {
                                Toast.makeText(searchActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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
    }

    private void lawyer(String url) {
        progressDialog.show();
        recyclerViewLaws.setVisibility(View.GONE);
        recyclerViewLawyers.setVisibility(View.VISIBLE);
        recyclerViewCriminals.setVisibility(View.GONE);

        // Populate lawyer list (dummy data)
        lawyerList = new ArrayList<>();

        lawyerAdapter = new LawyerAdapter(this, lawyerList);
        recyclerViewLawyers.setAdapter(lawyerAdapter);
        // Click listener for RecyclerView item click
        lawyerAdapter.setOnItemClickListener(new LawyerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showLawyerDetailsDialog(position);
            }
        });

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            if (response.getBoolean("status") == true) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    lawyerList.add(new Lawyer(jsonObject.getString("LawyerName"),
                                            jsonObject.getString("Expertise"),
                                            jsonObject.getString("Court"),
                                            jsonObject.getString("Contact"),
                                            jsonObject.getString("Description"),
                                            jsonObject.getString("photo"),
                                            jsonObject.getString("LaywerID")
                                    ));
                                }
                                lawyerAdapter.notifyDataSetChanged();
                                progressDialog.hide();
                            } else {
                                Toast.makeText(searchActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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
    }

    private void Criminal(String url) {
        progressDialog.show();
        recyclerViewLaws.setVisibility(View.GONE);
        recyclerViewLawyers.setVisibility(View.GONE);
        recyclerViewCriminals.setVisibility(View.VISIBLE);

        // Populate criminal list (dummy data)
        criminalList = new ArrayList<>();
//        criminalList.add(new Criminal("John Doe", "Wanted for theft"," R.drawable.john_doe", "5000.0"));
//        criminalList.add(new Criminal("Jane Smith", "Wanted for fraud"," R.drawable.jane_smith", "10000.0"));
//        // Add more criminals as needed

        criminalAdapter = new CriminalAdapter(this, criminalList);
        recyclerViewCriminals.setAdapter(criminalAdapter);

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            if (response.getBoolean("status") == true) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    criminalList.add(new Criminal(jsonObject.getString("FullName"),
                                            jsonObject.getString("Description"),
                                            jsonObject.getString("photo"),
                                            jsonObject.getString("Reward")
                                    ));
                                }
                                criminalAdapter.notifyDataSetChanged();
                                progressDialog.hide();
                            } else {
                                Toast.makeText(searchActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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
    }
}