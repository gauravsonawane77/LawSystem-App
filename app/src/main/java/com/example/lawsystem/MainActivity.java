package com.example.lawsystem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView criminalbtn,lawyerbtn,lawbtn,textViewName,textViewEmail;
    ImageView imageViewSearch;
    ProgressDialog progressDialog;
    TextView textViewlogout;
    private RecyclerView recyclerViewLaws;
    private LawAdapter lawAdapter;
    private List<Law> lawList;

    private RecyclerView recyclerViewLawyers;
    private LawyerAdapter lawyerAdapter;
    private List<Lawyer> lawyerList;

    private RecyclerView recyclerViewCriminals;
    private CriminalAdapter criminalAdapter;
    private List<Criminal> criminalList;

    private RequestQueue requestQueue;

    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        fabAdd = findViewById(R.id.fabAdd);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewName = findViewById(R.id.textViewName);
        String[] userInfo = MyPreferences.getUserInfo(MainActivity.this);
        String name = userInfo[0];
        String email = userInfo[1];
        textViewEmail.setText(email);
        textViewName.setText(name);

        textViewlogout = findViewById(R.id.textViewlogout);
        textViewlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPreferences.saveUserInfo(MainActivity.this, null, null,null,null);
                startActivity(new Intent(MainActivity.this, loginActivity.class));
                finish();
            }
        });
        // Instantiate the RequestQueue.
        requestQueue = Volley.newRequestQueue(this);

        imageViewSearch = findViewById(R.id.imageViewSearch);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, searchActivity.class));
            }
        });

        recyclerViewLaws = findViewById(R.id.recyclerViewLaws);
        recyclerViewLaws.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewLawyers = findViewById(R.id.recyclerViewLawyers);
        recyclerViewLawyers.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewCriminals = findViewById(R.id.recyclerViewCriminals);
        recyclerViewCriminals.setLayoutManager(new LinearLayoutManager(this));

        lawbtn = findViewById(R.id.lawbtn);
        lawyerbtn = findViewById(R.id.lawyerbtn);
        criminalbtn = findViewById(R.id.criminalbtn);

        lawbtn.setBackgroundResource(R.drawable.textbackground);
        lawyerbtn.setBackgroundResource(0);
        criminalbtn.setBackgroundResource(0);
        laws();

        lawbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                lawbtn.setBackgroundResource(R.drawable.textbackground);
                lawyerbtn.setBackgroundResource(0);
                criminalbtn.setBackgroundResource(0);
                laws();
            }
        });
        lawyerbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                lawbtn.setBackgroundResource(0);
                lawyerbtn.setBackgroundResource(R.drawable.textbackground);
                criminalbtn.setBackgroundResource(0);
                lawyer();
            }
        });
        criminalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lawbtn.setBackgroundResource(0);
                lawyerbtn.setBackgroundResource(0);
                criminalbtn.setBackgroundResource(R.drawable.textbackground);
                Criminal();
            }
        });


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, chatBotActivity.class));
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
        textViewName.setText("Name: " + lawyer.getName());
        textViewExpertise.setText("Expertise: " + lawyer.getExpertise());
        textViewCourt.setText("Court: " + lawyer.getCourt());
        textViewContact.setText("Contact: " + lawyer.getContact());
        textViewDescription.setText("Description: " + lawyer.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void laws(){
        progressDialog.show();
        recyclerViewLaws.setVisibility(View.VISIBLE);
        recyclerViewLawyers.setVisibility(View.GONE);
        recyclerViewCriminals.setVisibility(View.GONE);

        // Populate law list (dummy data)
        lawList = new ArrayList<>();
//        lawList.add(new Law("Title and extent of operation of the Code", "1","https://projectstore.vip"));
//        lawList.add(new Law("Punishment of offences committed within India", "2","https://projectstore.vip"));
//        lawList.add(new Law("Punishment of offences committed beyond, but which by law may be tried within, India", "31","https://projectstore.vip"));
//        lawList.add(new Law("Extension of Code to extra-territorial offences", "52","https://projectstore.vip"));
//        lawList.add(new Law("Certain laws not to be affected by this Act", "74","https://projectstore.vip"));
//        lawList.add(new Law("Definitions in the Code to be understood subject to exceptions\n", "85","https://projectstore.vip"));
//        lawList.add(new Law("Sense of expression once explained", "11","https://projectstore.vip"));
//        lawList.add(new Law("Property in possession of wife, clerk or servant", "14","https://projectstore.vip"));
//        lawList.add(new Law("When such an act is criminal by reason of its being done with a criminal knowledge or intention", "99","https://projectstore.vip"));
//        lawList.add(new Law("Persons concerned in criminal Act may be guilty of different offences", "36","https://projectstore.vip"));
//        lawList.add(new Law("Effect caused partly by act and partly by omission", "77","https://projectstore.vip"));
//        // Add more laws as needed

        lawAdapter = new LawAdapter(this, lawList);
        recyclerViewLaws.setAdapter(lawAdapter);


        String url = constant.API_BASE_URL+"law.php";

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            JSONArray jsonArray = response.getJSONArray("laws");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lawList.add(new Law(jsonObject.getString("LawText"),
                                        jsonObject.getString("SectionNumber"),
                                        jsonObject.getString("link")
                                        ));
                            }
                            lawAdapter.notifyDataSetChanged();
                            progressDialog.hide();
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

    private void lawyer(){
        progressDialog.show();
        recyclerViewLaws.setVisibility(View.GONE);
        recyclerViewLawyers.setVisibility(View.VISIBLE);
        recyclerViewCriminals.setVisibility(View.GONE);

        // Populate lawyer list (dummy data)
        lawyerList = new ArrayList<>();
//        lawyerList.add(new Lawyer("John Doe", "Criminal Defense", "District Court", "123-456-7890", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam euismod massa nec lobortis commodo."));
//        lawyerList.add(new Lawyer("Jane Smith", "Family Law", "Supreme Court", "987-654-3210", "Sed cursus magna a ex bibendum convallis. Fusce auctor tortor nec velit suscipit, ut auctor lorem finibus."));
//        // Add more lawyers as needed

        lawyerAdapter = new LawyerAdapter(this, lawyerList);
        recyclerViewLawyers.setAdapter(lawyerAdapter);
        // Click listener for RecyclerView item click
        lawyerAdapter.setOnItemClickListener(new LawyerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showLawyerDetailsDialog(position);
            }
        });

        String url = constant.API_BASE_URL+"lawyer.php";

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            JSONArray jsonArray = response.getJSONArray("lawyers");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lawyerList.add(new Lawyer(jsonObject.getString("LawyerName"),
                                        jsonObject.getString("Expertise"),
                                        jsonObject.getString("Court"),
                                        jsonObject.getString("Contact"),
                                        jsonObject.getString("Description"),
                                        jsonObject.getString("photo"),
                                        jsonObject.getString("user_id")
                                ));
                            }
                            lawyerAdapter.notifyDataSetChanged();
                            progressDialog.hide();
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

    private void Criminal(){
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

        String url = constant.API_BASE_URL+"criminal.php";

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            JSONArray jsonArray = response.getJSONArray("criminals");
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