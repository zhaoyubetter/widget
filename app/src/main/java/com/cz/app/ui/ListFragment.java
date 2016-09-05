package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cz.app.R;
import com.cz.app.adapter.SimpleAdapter;
import com.cz.app.data.DataProvider;
import com.cz.app.widget.FrameView;
import com.cz.app.widget.RadioLayout;
import com.cz.library.widget.TemplateView;
import com.ldzs.recyclerlibrary.PullToRefreshRecyclerView;

/**
 * Created by cz on 16/3/7.
 */
public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FrameView frameView = (FrameView) view.findViewById(R.id.frame_layout);
        PullToRefreshRecyclerView recyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new SimpleAdapter(getActivity(), DataProvider.ITEMS));
        RadioLayout radioLayout = (RadioLayout) view.findViewById(R.id.rl_frame);
        frameView.setFrame(FrameView.PROGRESS,0);

        frameView.postDelayed(new Runnable() {
            @Override
            public void run() {
                frameView.setFrame(FrameView.CONTAINER,600);
            }
        },0);
        radioLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                switch (position) {
                    case 0:
                        frameView.setFrame(TemplateView.CONTAINER, 300);
                        break;
                    case 1:
                        frameView.setFrame(TemplateView.PROGRESS, 300);
                        break;
                    case 2:
                        frameView.setFrame(TemplateView.DISPLAY, 300);
                        break;
                    case 3:
                        frameView.setFrame(TemplateView.ERROR, 300);
                        break;
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "isVisibleToUser:" + isVisibleToUser);
    }
}
