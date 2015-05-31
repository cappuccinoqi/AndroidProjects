package exam.zhouqi.me.myexamapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tabs.ImageTab;

/**
 * Created by Tony on 2015/5/30.
 */
public class Fragment1 extends ListFragment {
    private List<ImageTab> imageTabs = new ArrayList<ImageTab>();
    private ListView mListView;
    private ImageView mImageView;

    public ListAdapter getListAdapter() {

        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment1, container, false);
//        mListView = (ListView) view.findViewById(android.R.id.list);//此处要使用这个android.R.id.list

        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        MyListViewAdapter adapter = new MyListViewAdapter(getActivity(), R.drawable.icon, imageTabs);
//        mListView.setAdapter(adapter);
    }
}
