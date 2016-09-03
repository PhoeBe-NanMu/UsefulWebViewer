package com.example.usefulwebviewer;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button btn_forward;
    private Button btn_back;
    private Button btn_go;
    private EditText editText;
    private WebView webView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_forward = (Button) findViewById(R.id.btn_forward);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_go = (Button) findViewById(R.id.btn_go);
        editText = (EditText) findViewById(R.id.edit_text);
        webView = (WebView) findViewById(R.id.web_view);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        /*设置Javascript可用*/
        webView.getSettings().setJavaScriptEnabled(true);

        /*处理Javascript对话框*/
        webView.setWebChromeClient(new WebChromeClient());

        /*处理各种请求和通知，如果不使用该局代码，将使用内置浏览器访问网页*/
        webView.setWebViewClient(new WebViewClient());

        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.goForward();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.goBack();
            }
        });
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(editText.getText().toString())) {
                    openBrowser();
                }
                else {
                    showEmptyDialog();
                }
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER) {
                    if (!"".equals(editText.getText().toString())) {
                        openBrowser();
                        return true;
                    } else {
                        showEmptyDialog();
                    }
                }
                return false;
            }
        });
    }

    private void showEmptyDialog() {
        new AlertDialog.Builder(this)
                .setTitle("网页浏览器")
                .setMessage("地址为空,请重新输入地址")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "输入地址！", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void openBrowser() {

        webView.loadUrl(editText.getText().toString());
//        linearLayout.setVisibility(View.INVISIBLE);

        Toast.makeText(this, "正在加载...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        if (webView.i)
        webView.goBack();
    }
}
