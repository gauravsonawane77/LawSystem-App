package com.example.lawsystem;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerChat;
    EditText editMessage;
    Button buttonSend;

    ArrayList<Message> messageList;
    MessageAdapter adapter;

    int senderId = 0;
    String receiverId = "0";

    private Handler messageHandler;
    private Runnable messageRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerChat = findViewById(R.id.recyclerChat);
        editMessage = findViewById(R.id.editMessage);
        buttonSend = findViewById(R.id.buttonSend);

        String[] userInfo = MyPreferences.getUserInfo(ChatActivity.this);
        String id = userInfo[2];

        if (getIntent().hasExtra("userID")) {
            senderId = Integer.parseInt(id);
            receiverId = getIntent().getStringExtra("userID");
        } else {
            senderId = Integer.parseInt(id);
            receiverId = getIntent().getStringExtra("lawyerID");
        }

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList, senderId);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(adapter);

        // Start auto-fetch messages every second
        startAutoFetchMessages();

        buttonSend.setOnClickListener(v -> {
            String msg = editMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                sendMessage(msg);
                editMessage.setText("");
            }
        });
    }

    private void startAutoFetchMessages() {
        messageHandler = new Handler();
        messageRunnable = new Runnable() {
            @Override
            public void run() {
                fetchMessages();
                messageHandler.postDelayed(this, 1000); // Run every 1000 milliseconds (1 second)
            }
        };
        messageHandler.post(messageRunnable);
    }

    private void stopAutoFetchMessages() {
        if (messageHandler != null && messageRunnable != null) {
            messageHandler.removeCallbacks(messageRunnable);
        }
    }

    private void fetchMessages() {
        String url = constant.API_BASE_URL + "get-messages.php?sender_id=" + senderId + "&receiver_id=" + receiverId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    messageList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            Message msg = new Message(
                                    obj.getInt("sender_id"),
                                    obj.getInt("receiver_id"),
                                    obj.getString("message"),
                                    obj.getString("sent_at")
                            );
                            messageList.add(msg);
                        }
                        adapter.notifyDataSetChanged();
                        recyclerChat.scrollToPosition(messageList.size() - 1);
                    } catch (Exception e) {
                        Toast.makeText(this, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error loading messages", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void sendMessage(String msgText) {
        String url = constant.API_BASE_URL + "send-message.php";
        try {
            JSONObject json = new JSONObject();
            json.put("sender_id", senderId);
            json.put("receiver_id", receiverId);
            json.put("message", msgText);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                    response -> fetchMessages(),
                    error -> Toast.makeText(this, "Send failed", Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAutoFetchMessages(); // Stop fetching when activity is destroyed
    }
}
