package com.example.tourlog.photopicker;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.tourlog.R;
import com.example.tourlog.config.HTTPURL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoPagerAdapter extends PagerAdapter {

  public interface PhotoViewClickListener{
    void OnPhotoTapListener(View view, float v, float v1);
  }

  public PhotoViewClickListener listener;

  private List<String> paths = new ArrayList<>();
  private Context mContext;
  private LayoutInflater mLayoutInflater;
  private boolean isWeb;


  public PhotoPagerAdapter(Context mContext, List<String> paths) {
    this.mContext = mContext;
    this.paths = paths;
    mLayoutInflater = LayoutInflater.from(mContext);
  }

  public void setPhotoViewClickListener(PhotoViewClickListener listener){
    this.listener = listener;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {

    View itemView = mLayoutInflater.inflate(R.layout.item_preview, container, false);

    PhotoView imageView = (PhotoView) itemView.findViewById(R.id.iv_pager);

    final String path = paths.get(position);
    final Uri uri;
    if (path.startsWith("http")) {
      uri = Uri.parse(path);
    } else {
      uri = Uri.fromFile(new File(path));
    }
    Glide.with(mContext)
            .load(uri)
//            .placeholder(R.mipmap.default_error)
            .error(R.mipmap.default_error)
            .into(imageView);

    imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float v, float v1) {
          if(listener != null){
            listener.OnPhotoTapListener(view, v, v1);
          }
        }
    });

    container.addView(itemView);

    return itemView;
  }


  @Override public int getCount() {
    return paths.size();
  }


  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }


  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override
  public int getItemPosition (Object object) { return POSITION_NONE; }

}
