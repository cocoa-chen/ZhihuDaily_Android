package com.example.chenaibin.zhihudaily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;


public class StoryActivity extends Activity {

    private static final int SHOW_STORY = 0;
    private static final int REQUEST_FAILED = 1;
    private WebView webView;
    private ImageView imageView;
    private TextView textView;
    private View topView;
    private int storyId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_STORY:
                {
                    String obj = (String)msg.obj;
                    webView.loadDataWithBaseURL("",obj,"text/html","utf-8","");
//                    webView.loadData(obj,"text/html","utf-8");
                    //加载图片
                    Bundle bundle = msg.getData();
                    String imageUrl = bundle.getString("image");
                    String title = bundle.getString("title");
                    Picasso.with(StoryActivity.this).load(imageUrl).into(imageView);
                    textView.setText(title);
                }
                    break;
                case REQUEST_FAILED:
                {
                    Toast.makeText(StoryActivity.this, "获取详情失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        //获取id
        Intent intent = getIntent();
        this.storyId = intent.getIntExtra("storyId",0);
        //webview加载
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //添加标题imageView
        topView = LayoutInflater.from(this).inflate(R.layout.story_title,null);
        imageView = (ImageView) topView.findViewById(R.id.imageView);
        textView = (TextView) topView.findViewById(R.id.textView);
        webView.addView(topView);
        //获取数据
        queryStoryDetailInfo();
    }

    public void queryStoryDetailInfo() {
        String address = "http://news-at.zhihu.com/api/3/news/" + this.storyId;
        HttpUtil.sendHttpRequest(address,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
//                Log.d("MyLog", "详情请求成功:" + response);
                handleStoryDetailInfoResponse(response);
            }

            @Override
            public void onError(Exception e) {
                Log.d("MyLog","详情请求失败:" + e);
                Message msg = new Message();
                msg.what = REQUEST_FAILED;
                handler.sendMessage(msg);
            }
        });
    }

    public void handleStoryDetailInfoResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            String bodyString = obj.getString("body");
            String imageString = obj.getString("image");
            String titleString = obj.getString("title");
            String cssString = obj.getString("css");
            Gson gson = new Gson();
            ArrayList<String> css = gson.fromJson(cssString,new TypeToken<ArrayList<String>>(){}.getType());
//            for (String cssUrl : css) {
//                Log.d("MyLog","cssUrl:" + cssUrl);
//            }
            Message msg = new Message();
            msg.what = SHOW_STORY;
            if (css.size() > 0) {
                String cssUrl = css.get(0);
                String body = "<link href='" + cssUrl + "' rel = 'stylesheet' type='text/css' />" + bodyString;
                msg.obj = body;
            }else {
                msg.obj = response;
            }
            Bundle bundle = new Bundle();
            bundle.putString("image",imageString);
            bundle.putString("title",titleString);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }catch (Exception e) {
            Log.d("MyLog","详情解析失败:" + e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
