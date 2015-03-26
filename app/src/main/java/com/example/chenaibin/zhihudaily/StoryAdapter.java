package com.example.chenaibin.zhihudaily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chenaibin on 15/3/24.
 */
public class StoryAdapter extends ArrayAdapter<TodayStory> {
    private int resourceId;
    public StoryAdapter(Context context, int resource, List<TodayStory> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodayStory story = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.story_textView);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.story_imageView);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(story.getTitle());
        if (story.getImages().size() > 0) {
            String imageUrlString = story.getImages().get(0);
//            Log.d("MyLog","cell图片地址:" + imageUrlString);
            Picasso.with(getContext()).load(imageUrlString).into(viewHolder.imageView);
        }
        return view;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

}
