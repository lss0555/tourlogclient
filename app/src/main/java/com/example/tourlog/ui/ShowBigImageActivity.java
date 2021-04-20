package com.example.tourlog.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tourlog.R;
import com.example.tourlog.fragment.PictureSlideFragment;

import java.util.ArrayList;
import java.util.Collections;

public class ShowBigImageActivity  extends BaseAcitivity{
    private ViewPager viewPager;
    private TextView tv_indicator;
    private ArrayList<String> urlList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_image);

        String [] urls={"http://www.zhagame.com/wp-content/uploads/2016/01/JarvanIV_0.jpg",
                "http://www.zhagame.com/wp-content/uploads/2016/01/JarvanIV_1.jpg",
                "http://www.zhagame.com/wp-content/uploads/2016/01/JarvanIV_2.jpg",
                "http://www.zhagame.com/wp-content/uploads/2016/01/JarvanIV_3.jpg",
                "http://www.zhagame.com/wp-content/uploads/2016/01/JarvanIV_4.jpg",
                "http://www.zhagame.com/wp-content/uploads/2016/01/JarvanIV_5.jpg",
                "http://www.zhagame.com/wp-content/uploads/2016/01/JarvanIV_6.jpg",};//需要加载的网络图片

        urlList = new ArrayList<>();
        Collections.addAll(urlList, urls);

        viewPager = (ViewPager) findViewById(R.id.viewPage);
        tv_indicator = (TextView) findViewById(R.id.tv_indicator);

        viewPager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tv_indicator.setText(String.valueOf(position+1)+"/"+urlList.size());//<span style="white-space: pre;">在当前页面滑动至其他页面后，获取position值</span>
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private  class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

        public PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(urlList.get(position));//<span style="white-space: pre;">返回展示不同网络图片的PictureSlideFragment</span>
        }

        @Override
        public int getCount() {
            return urlList.size();//<span style="white-space: pre;">指定ViewPager的总页数</span>
        }
    }
}
