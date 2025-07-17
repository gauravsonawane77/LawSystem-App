package com.example.lawsystem;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class chatBotActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressDialog progressDialog;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_bot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize WebView
        webView = findViewById(R.id.webView);
        rootView = findViewById(android.R.id.content);

        // Enable JavaScript (required for most web content including chatbots)
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        // Set a WebViewClient to handle events and show ProgressDialog
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show(); // Show ProgressDialog when page starts loading
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss(); // Dismiss ProgressDialog when page finishes loading
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressDialog.dismiss(); // Dismiss ProgressDialog if there's an error loading the page
            }
        });

        // Listen for keyboard visibility changes
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is visible, adjust WebView height
                    adjustWebViewHeight(keypadHeight);
                } else {
                    // Keyboard is hidden, reset WebView height
                    resetWebViewHeight();
                }
            }
        });


        // Load the chatbot URL
        webView.loadUrl("https://mediafiles.botpress.cloud/2c0c920f-3bd8-4588-b331-0b7a0b03b594/webchat/bot.html");


    }

    private void adjustWebViewHeight(int keyboardHeight) {
        // Calculate the new height for the WebView after considering keyboard height
        int newHeight = rootView.getHeight() - keyboardHeight;
        webView.getLayoutParams().height = newHeight;
        webView.requestLayout();
    }

    private void resetWebViewHeight() {
        // Reset the WebView height to match the root view's height
        webView.getLayoutParams().height = rootView.getHeight();
        webView.requestLayout();
    }
}