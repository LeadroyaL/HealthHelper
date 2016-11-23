package com.leadroyal.isee.healthhelper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.google.protobuf.InvalidProtocolBufferException;
import com.leadroyal.isee.healthhelper.proto.HealthData;
import com.leadroyal.isee.healthhelper.util.HttpUtils;
import com.leadroyal.isee.healthhelper.util.ToastUtils;
import com.loopj.android.http.AsyncHttpClient;

import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowResultActivity extends AppCompatActivity {


    public static Handler handler;
    private Context context;
    public static final int MSG_OK = 0;
    public static final int MSG_FAIL = 1;

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.activity_show_result)
    RelativeLayout activityShowResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        ButterKnife.bind(this);
        context = this;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_OK:
                        String s = (String) msg.obj;
                        webview.loadData(s, "text/html", "UTF-8");
                        ToastUtils.show(context, "parse data success!");
                        break;
                    case MSG_FAIL:
                        ToastUtils.show(context, "download data fail! please check your network");
                        break;
                    default:
                }
            }
        };
        HttpUtils.downloadData();
    }
}
