package com.example.tourlog.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tourlog.R;
import com.example.tourlog.config.HTTPURL;
import com.example.tourlog.utils.ImageUtils;

import java.util.ArrayList;

/**
 */

/**
 * 图片列表适配器
 */
public class PhotoListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mPhotos;

    public PhotoListAdapter(Context context, ArrayList<String> mPhotos) {
        this.context = context;
        this.mPhotos = mPhotos;
    }
    public PhotoListAdapter(Context context, ArrayList<String> mPhotos, int Num) {
        this.context = context;
        this.mPhotos = mPhotos;
    }
    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public String getItem(int position) {
        return mPhotos.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_image1, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageUtils.displayImage(HTTPURL.FIle+mPhotos.get(position), viewHolder.image);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            Intent intent = new Intent(context, ImageShowActivity.class);
//            intent.putExtra("ImgPath", mPhotos.get(position));
//            context.startActivity(intent);
                if(onPicClickLitner!=null){
                    onPicClickLitner.click(position);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public Button bt;
    }

    public interface onPicClickLitner{
        void click(int pos);
    }
    public onPicClickLitner onPicClickLitner;

    public void setOnPicClickLitner(PhotoListAdapter.onPicClickLitner onPicClickLitner) {
        this.onPicClickLitner = onPicClickLitner;
    }
}