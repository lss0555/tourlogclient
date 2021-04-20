package com.example.tourlog.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tourlog.R;
import com.example.tourlog.bean.Actions;
import com.example.tourlog.config.HTTPURL;
import com.example.tourlog.utils.ToastUtils;
import com.example.tourlog.view.CustomGrideview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**动态列表
 */
public class ActionListAdapter extends BaseAdapter {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);//线程池
    private Context context;
    private ArrayList<Actions> mDate;
    private ViewHold viewHold;
    private PhotoListAdapter adapter;
    public ActionListAdapter(Context context, ArrayList<Actions> mDate) {
        this.context = context;
        this.mDate = mDate;
    }
    @Override
    public int getCount() {
        return mDate.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_action, null);
            viewHold = new ViewHold();
            viewHold.ImgVedio= (ImageView) convertView.findViewById(R.id.img_vedio);
            viewHold.RtlDel= (RelativeLayout) convertView.findViewById(R.id.rtl_delete);
            viewHold.RtlEdit= (RelativeLayout) convertView.findViewById(R.id.rtl_edit);
            viewHold.TvContent= (TextView) convertView.findViewById(R.id.tv_content);
            viewHold.TvAddress= (TextView) convertView.findViewById(R.id.tv_address);
            viewHold.gridview= (CustomGrideview) convertView.findViewById(R.id.list);
            viewHold.TvTime= (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHold);
        }else {
            viewHold= (ViewHold) convertView.getTag();
        }
        viewHold.TvAddress.setText(mDate.get(position).getAddress());
        viewHold.TvContent.setText(mDate.get(position).getContent());
        viewHold.TvTime.setText(mDate.get(position).getCreatTime());



        String imglist = mDate.get(position).getImglist();
        ArrayList<String> imgList=new ArrayList<>();
        if(imglist!=null&&!imglist.equals("")){
            String[] split = imglist.split(",");
            if(split.length>0){
                for (String str:split){
                    imgList.add(str);
                }
            }
        }

        if(mDate.get(position).getType()==1){
            viewHold.ImgVedio.setVisibility(View.GONE);
            viewHold.gridview.setVisibility(View.VISIBLE);
            adapter=new PhotoListAdapter(context,imgList);
            adapter.setOnPicClickLitner(new PhotoListAdapter.onPicClickLitner() {
                @Override
                public void click(int pos) {
                    if(onShowImageLitner!=null){
                        onShowImageLitner.show(position,pos);
                    }
                }
            });
            viewHold.gridview.setAdapter(adapter);
        }else {
            viewHold.ImgVedio.setVisibility(View.VISIBLE);
            viewHold.gridview.setVisibility(View.GONE);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = getNetVideoBitmap(HTTPURL.VEDIO+mDate.get(position).getVediopath());
                    viewHold.ImgVedio.post(new Runnable() {
                        @Override
                        public void run() {
                            viewHold.ImgVedio.setImageBitmap(bitmap);
                        }
                    });
                }
            });
        }


        viewHold.RtlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEditLitner!=null){
                    onEditLitner.edit(position);
                }
            }
        });

        viewHold.RtlDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDelLitner!=null){
                    onDelLitner.del(position);
                }
            }
        });
        return convertView;
    }
    class ViewHold{
        RelativeLayout RtlEdit;
        RelativeLayout RtlDel;
        TextView TvContent;
        TextView TvAddress;
        CustomGrideview gridview;
        ImageView ImgVedio;
        TextView TvTime;
    }

    public interface onDelLitner{
        void del(int pos);
    }

    public interface onEditLitner{
        void edit(int pos);
    }

    public onDelLitner onDelLitner;
    private onEditLitner onEditLitner;

    public void setOnDelLitner(ActionListAdapter.onDelLitner onDelLitner) {
        this.onDelLitner = onDelLitner;
    }

    public void setOnEditLitner(ActionListAdapter.onEditLitner onEditLitner) {
        this.onEditLitner = onEditLitner;
    }

    public interface  onShowImageLitner{
        void show(int pos,int position);
    }
    public onShowImageLitner onShowImageLitner;

    public void setOnShowImageLitner(ActionListAdapter.onShowImageLitner onShowImageLitner) {
        this.onShowImageLitner = onShowImageLitner;
    }

    public Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }
}