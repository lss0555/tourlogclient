package com.example.tourlog.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tourlog.R;
import com.example.tourlog.ui.LoginActivity;
import com.example.tourlog.ui.UpdatePasswordActivity;
import com.example.tourlog.ui.UserInfoActivity;
import com.example.tourlog.utils.CaCheUtils;

/**
 * 个人中心
 */
public class MineFragment extends Fragment {

    private TextView mTvUser;
    private RelativeLayout mRtlUpdatePassword;
    private RelativeLayout mRtlExist;
    private RelativeLayout mRtlInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        iniview(view);
        initdata();
        initevnet();
        return view;
    }

    private void iniview(View view) {
        mTvUser = view.findViewById(R.id.tv_user);
        mRtlInfo = view.findViewById(R.id.rtl_userinfo);
        mRtlExist = view.findViewById(R.id.rtl_exist);
        mRtlUpdatePassword = view.findViewById(R.id.rtl_updatepassword);
    }

    private void initdata() {
        mTvUser.setText("欢迎您，"+CaCheUtils.GetUser(getActivity()));
    }


    @Override
    public void onStart() {
        super.onStart();
        initdata();
    }

    private void initevnet() {
        //个人信息
        mRtlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), UserInfoActivity.class),11);
            }
        }); //修改密码
        mRtlUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), UpdatePasswordActivity.class),11);
            }
        });

        //退出登录
        mRtlExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认要退出？");
                builder.setTitle("提示");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();}
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {
                    CaCheUtils.saveIsLogin(getActivity(),false);
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                    dialog.dismiss();
                }
                });
                builder.create().show();
            }
        });
    }


}
