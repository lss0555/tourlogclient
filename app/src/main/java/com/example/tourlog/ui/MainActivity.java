package com.example.tourlog.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tourlog.R;
import com.example.tourlog.fragment.ActionFragment;
import com.example.tourlog.fragment.AlbumFragment;
import com.example.tourlog.fragment.MineFragment;

import java.util.ArrayList;

//主页面
public class MainActivity extends BaseAcitivity implements View.OnClickListener {
    private ViewPager mVpTabs;
    private RelativeLayout mRtlMine;
    private TextView mTvMine;
    private ArrayList<Fragment> mFragments;
    private ActionFragment actionFragment;
    private MineFragment mineFragment;
    private FragmentPagerAdapter mAdapter;
    private AlbumFragment albumFragment;
    private RelativeLayout mRtlAction;
    private RelativeLayout mRtlXc;
    private TextView mTvAction;
    private TextView mTvXc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        initPage();
    }

    private void initPage() {
        mVpTabs.setOffscreenPageLimit(2);
        mFragments=new ArrayList<Fragment>();
        if(actionFragment ==null){
            actionFragment =new ActionFragment();
        }
        if(albumFragment ==null){
            albumFragment =new AlbumFragment();
        }
        if(mineFragment ==null){
            mineFragment =new MineFragment();
        }
        mFragments.add(actionFragment);
        mFragments.add(albumFragment);
        mFragments.add(mineFragment);
        mAdapter =new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }
            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mVpTabs.setAdapter(mAdapter);
        mVpTabs.setCurrentItem(0);
        mVpTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public int mCurentPageIndex;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                ResetTab();
                switch (position) {
                    case 0:
                        ChangeType(mTvAction, getResources().getDrawable(R.mipmap.action1), R.color.blue);
                        break;
                    case 1:
                        ChangeType(mTvXc, getResources().getDrawable(R.mipmap.xc1), R.color.blue);
                        break;
                    case 2:
                        ChangeType(mTvMine, getResources().getDrawable(R.mipmap.mine1), R.color.blue);
                        break;
                }
                mCurentPageIndex = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initview() {
        mVpTabs = (ViewPager) findViewById(R.id.viewPage);
        mRtlAction = (RelativeLayout) findViewById(R.id.rtl_action);
        mRtlXc = (RelativeLayout) findViewById(R.id.rtl_xc);
        mRtlMine = (RelativeLayout) findViewById(R.id.rtl_mime);
        mTvAction = (TextView) findViewById(R.id.tv_action);
        mTvXc = (TextView) findViewById(R.id.tv_xc);
        mTvMine = (TextView) findViewById(R.id.tv_mime);
        mRtlAction.setOnClickListener(this);
        mRtlMine.setOnClickListener(this);
        mRtlXc.setOnClickListener(this);
    }

    /**
     * 重置Tab
     */
    public  void  ResetTab(){
        ChangeType(mTvAction, getResources().getDrawable(R.mipmap.action),R.color.light_black);
        ChangeType(mTvXc, getResources().getDrawable(R.mipmap.xc),R.color.light_black);
        ChangeType(mTvMine, getResources().getDrawable(R.mipmap.mine),R.color.light_black);
    }
    public  void ChangeType(TextView mTv, Drawable drawable, int color){
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTv.setCompoundDrawables(null, drawable, null, null);
        mTv.setTextColor(getResources().getColor(color));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_action:
                mVpTabs.setCurrentItem(0);
                break;
            case R.id.rtl_xc:
                mVpTabs.setCurrentItem(1);
                break;
            case R.id.rtl_mime:
                mVpTabs.setCurrentItem(2);
                break;
        }
    }

    private long firstTime=0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                long secondTime= System.currentTimeMillis();
                if(secondTime-firstTime>2000){
                    Toast.makeText(MainActivity.this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime=secondTime;
                    return true;
                }else{
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}