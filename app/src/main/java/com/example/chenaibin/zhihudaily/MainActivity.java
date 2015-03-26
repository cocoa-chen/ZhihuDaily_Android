package com.example.chenaibin.zhihudaily;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static final int UPDATE_STORY = 0;
    private List<TopStory> topStories;
    private List<TodayStory> todayStories;
    private ListView listView;
    private ViewPager viewPager;
    private List<View> viewList = new ArrayList<View>();

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_STORY:
                {
                    //刷新列表视图
                    StoryAdapter adapter = new StoryAdapter(MainActivity.this,R.layout.story_item,todayStories);
                    listView.setAdapter(adapter);
                    //刷新横向视图
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    for (int i = 0; i < topStories.size(); i++) {
                        final TopStory story = topStories.get(i);
                        View view = inflater.inflate(R.layout.story_title,null);
                        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                        TextView textView = (TextView) view.findViewById(R.id.textView);
                        Picasso.with(MainActivity.this).load(story.getImage()).into(imageView);
                        textView.setText(story.getTitle());
                        final int storyId = story.getId();
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this,StoryActivity.class);
                                intent.putExtra("storyId",storyId);
                                startActivity(intent);
                            }
                        });
                        viewList.add(view);
                    }
                    viewPager.setAdapter(new StoryViewPagerAdapter(viewList));
                    viewPager.setCurrentItem(0);
                }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodayStory story = todayStories.get(position);
//                Log.d("MyLog","点击的标题是:" + story.getTitle());
                Intent intent = new Intent(MainActivity.this,StoryActivity.class);
                intent.putExtra("storyId",story.getId());
                startActivity(intent);
            }
        });
        //横向数据
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //请求数据
        queryTodayStoriesFromServer();
    }
    //请求数据
    private void queryTodayStoriesFromServer() {
        String address = "http://news-at.zhihu.com/api/3/news/latest";
        HttpUtil.sendHttpRequest(address,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
//                Log.d("MyLog","数据请求成功:" + response);
                handleTodayStoriesResponse(response);
            }

            @Override
            public void onError(Exception e) {
//                Log.d("MyLog","数据请求失败:" + e);
                Toast.makeText(MainActivity.this,"获取数据失败,请稍后重试!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //处理返回数据
    private void handleTodayStoriesResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
//            String date = jsonObject.getString("date");
            String stories = jsonObject.getString("stories");
            String top_stories = jsonObject.getString("top_stories");
//            Log.d("MyLog","date:" + date);
//            Log.d("MyLog","stories:" + stories);
//            Log.d("MyLog","top_stories:" + top_stories);
            Gson gson = new Gson();
            topStories = gson.fromJson(top_stories,new TypeToken<List<TopStory>>(){}.getType());
//            for (TopStory topStory : topStories) {
//                Log.d("MyLog","topStory id:" + topStory.getId());
//                Log.d("MyLog","topStory image:" + topStory.getImage());
//                Log.d("MyLog","topStory title:" + topStory.getTitle());
//            }
            todayStories = gson.fromJson(stories,new TypeToken<List<TodayStory>>(){}.getType());
//            for (TodayStory story : todayStories) {
//                Log.d("MyLog","stody id:" + story.getId());
//                for (String imageUrl : story.getImages()) {
//                    Log.d("MyLog","stody image:" + imageUrl);
//                }
//                Log.d("MyLog","stody title:" + story.getTitle());
//            }
            Message message = new Message();
            message.what = UPDATE_STORY;
            handler.sendMessage(message);
        }catch (Exception e) {
            Toast.makeText(MainActivity.this,"数据解析失败,请稍后重试!",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
