package exam.zhouqi.me.myexamapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tabs.ImageTab;

/**
 * Created by Tony on 2015/5/30.
 */
public class MyListViewAdapter extends ArrayAdapter<ImageTab> {
    private LayoutInflater mInflater;
    private List<ImageTab> mImageTabs;
    private int resourceId;

    public MyListViewAdapter(Context context, int resource, List<ImageTab> objects) {
        super(context, resource, objects);

        resourceId = resource;
    }


    @Override
    public ImageTab getItem(int position) {
        return mImageTabs.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            holder = new ViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(R.id.imageTab_title);
            holder.mTime = (TextView) convertView.findViewById(R.id.imageTab_time);
            holder.mimageView = (ImageView) convertView.findViewById(R.id.iamgeTab_image);
            holder.mLike = (TextView) convertView.findViewById(R.id.imageTab_like);
            holder.mNegative = (TextView) convertView.findViewById(R.id.imageTab_negative);
            holder.mTucao = (TextView) convertView.findViewById(R.id.imageTab_tucao);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        ImageTab imageTab = mImageTabs.get(position);
        holder.mTitle.setText(imageTab.getmTitle());
        holder.mTime.setText(imageTab.getmTime());
        holder.mimageView.setImageResource(imageTab.getmImageId());
        holder.mLike.setText(imageTab.getmLike());
        holder.mNegative.setText(imageTab.getmNegative());
        holder.mTucao.setText(imageTab.getmTucao());
        return convertView;
    }

    @Override
    public int getCount() {
        return mImageTabs.size();
    }

    public class ViewHolder {
        TextView mTitle;
        TextView mTime;
        ImageView mimageView;
        TextView mLike;
        TextView mNegative;
        TextView mTucao;
    }
}
