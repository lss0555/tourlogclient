package com.example.tourlog.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.example.tourlog.R;
import com.example.tourlog.adapter.ActionListAdapter;
import com.example.tourlog.bean.Actions;
import com.example.tourlog.bean.TResult;
import com.example.tourlog.config.HTTPURL;
import com.example.tourlog.okhttp.DialogCallBack;
import com.example.tourlog.okhttp.HttpUtils;
import com.example.tourlog.okhttp.JsonCallback;
import com.example.tourlog.okhttp.LoadingDialog;
import com.example.tourlog.photopicker.PhotoPreviewActivity;
import com.example.tourlog.photopicker.intent.PhotoPreviewIntent;
import com.example.tourlog.ui.MainActivity;
import com.example.tourlog.ui.PostActionActivity;
import com.example.tourlog.ui.PostActionVedioActivity;
import com.example.tourlog.ui.ShowBigImageActivity;
import com.example.tourlog.utils.CaCheUtils;
import com.example.tourlog.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态
 */
public class ActionFragment extends Fragment {
    private final int REQUEST_VIDEO_CODE = 1111;

    private ArrayList<Actions> mData = new ArrayList<>();
    private ListView mList;
    private RelativeLayout mRtlPost;
    private ActionListAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action, null);
        iniview(view);
        initdata();
        initevnet();
        return view;
    }

    private void iniview(View view) {
        mRtlPost = view.findViewById(R.id.rtl_post);
        refreshLayout = view.findViewById(R.id.refresh);
        mList = view.findViewById(R.id.list);
        adapter = new ActionListAdapter(getActivity(), mData);
        mList.setAdapter(adapter);
    }

    private void initdata() {
        refreshLayout.setRefreshing(true);
        HttpUtils.getInstance().get(getActivity(), CacheMode.NO_CACHE, HTTPURL.BASE + "actionList?userid=" + CaCheUtils.GetId(getActivity()), null, new JsonCallback<TResult<Actions>>() {
            @Override
            public void onSuccess(Response<TResult<Actions>> response) {
                List<Actions> data = response.body().getData();
                mData.clear();
                mData.addAll(data);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Response<TResult<Actions>> response) {
                super.onError(response);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initevnet() {
        //删除
        adapter.setOnDelLitner(new ActionListAdapter.onDelLitner() {
            @Override
            public void del(int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认要删除？");
                builder.setTitle("提示");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteAction(mData.get(pos).getId());
                    }
                });
                builder.create().show();
            }
        });

        //查看图片
        adapter.setOnShowImageLitner(new ActionListAdapter.onShowImageLitner() {
            @Override
            public void show(int pos,int pos1) {
                String imglist = mData.get(pos).getImglist();
                ArrayList<String> imgList = new ArrayList<>();
                if (imglist != null && !imglist.equals("")) {
                    String[] split = imglist.split(",");
                    if (split.length > 0) {
                        for (String str : split) {
                            imgList.add(HTTPURL.FIle + str);
                        }
                    }
                }
                PhotoPreviewIntent intent = new PhotoPreviewIntent(getActivity());
                intent.setCurrentItem(pos1);
                intent.setPhotoPaths(imgList);
                startActivityForResult(intent, PhotoPreviewActivity.REQUEST_PREVIEW);
            }
        });

        //修改
        adapter.setOnEditLitner(new ActionListAdapter.onEditLitner() {
            @Override
            public void edit(int pos) {
                Actions actions = mData.get(pos);
                if(actions.getType()==1){
                    startActivityForResult(new Intent(getActivity(), PostActionActivity.class).putExtra("data", mData.get(pos)), 11);
                }else {
                    startActivityForResult(new Intent(getActivity(), PostActionVedioActivity.class).putExtra("data", mData.get(pos)), 11);
                }
            }

        });


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });

        //发布
        mRtlPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"图片", "视频"};
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(getActivity());
                listDialog.setTitle("选择发布类型");
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQUEST_VIDEO_CODE);
                        } else {
                            startActivityForResult(new Intent(getActivity(), PostActionActivity.class), 11);
                        }
                    }
                });
                listDialog.show();
            }
        });
    }

    //删除
    private void deleteAction(int id) {
        HttpUtils.getInstance().get(getActivity(), CacheMode.NO_CACHE, HTTPURL.BASE + "deleteAction?id=" + id, null, new DialogCallBack<TResult<Actions>>(getActivity()) {
            @Override
            public void onSuccess(Response<TResult<Actions>> response) {
                ToastUtils.showToast("删除成功");
                initdata();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                String path = uri.getPath();
                uploadFile(path);
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                String path = getPath(getActivity(), uri);
                uploadFile(path);
            } else {//4.4以下下系统调用方法
                String path = getRealPathFromURI(uri);
                uploadFile(path);
            }
        }
        if (resultCode == 1) {
            initdata();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private LoadingDialog dialog;

    //上传
    public void uploadFile(String paths) {
        if (dialog == null) {
            dialog = new LoadingDialog(getActivity());
        }
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        File file = new File(paths);
        HttpParams param = new HttpParams();
        OkGo.<String>post(HTTPURL.BASE + "uploadFile")
                .tag(this)
                .params("file", file)
                .params(param)
                .isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        String body = response.body();
                        TResult<String> tResult = JSON.parseObject(body, TResult.class);
                        Actions actions = new Actions();
                        actions.setType(2);
                        actions.setVediopath(tResult.getStringData());
                        startActivityForResult(new Intent(getActivity(), PostActionVedioActivity.class)
                        .putExtra("data",actions),11);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        ToastUtils.showToast("上传失败");
                    }
                });
    }
}
