package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.cz.app.R;
import com.cz.app.adapter.SimpleAdapter;
import com.cz.app.data.DataProvider;
import com.cz.app.widget.RadioLayout;
import com.cz.library.util.Utils;
import com.cz.library.widget.ListIndicator;
import com.ldzs.recyclerlibrary.PullToRefreshRecyclerView;

import java.util.ArrayList;

/**
 * Created by cz on 16/3/7.
 */
public class ListIndicateFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_indicate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String[] words = Utils.getStringArray(R.array.word);
        final PullToRefreshRecyclerView recyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.recycler_view);
        final ListIndicator listIndicator = (ListIndicator) view.findViewById(R.id.li_indicator);
        RadioLayout radioLayout = (RadioLayout) view.findViewById(R.id.rl_mode);
        final TextView label = (TextView) view.findViewById(R.id.tv_label);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new SimpleAdapter(getActivity(), DataProvider.ITEMS));

        listIndicator.setData(words);

        final ArrayList<Integer> positions = new ArrayList<>();
        if (null != DataProvider.ITEMS) {
            String word = null;
            int length = DataProvider.ITEMS.length;
            for (int i = 0; i < length; i++) {
                String value = DataProvider.ITEMS[i];
                if (TextUtils.isEmpty(word)) {
                    word = value.substring(0, 1);
                    positions.add(i);
                } else {
                    String newWord = value.substring(0, 1);
                    if (!word.equals(newWord)) {
                        word = newWord;
                        positions.add(i);
                    }
                }
            }
        }

        ViewCompat.setAlpha(label, 0f);
        ViewCompat.setScaleX(label, 0.8f);
        ViewCompat.setScaleY(label, 0.8f);
        listIndicator.setOnIndicatorListener(new ListIndicator.OnIndicatorListener() {
            @Override
            public void onPress() {
                ViewCompat.animate(label).alpha(1f).scaleX(1f).scaleY(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(300);
            }

            @Override
            public void onSelect(String word, int position) {
                label.setText(word);

                recyclerView.scrollToPosition(positions.get(position));
            }

            @Override
            public void onCancel() {
                ViewCompat.animate(label).alpha(0f).scaleX(0f).scaleY(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(300);
            }
        });
        radioLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                listIndicator.setType(position);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = linearLayoutManager.findFirstVisibleItemPosition();
                Integer[] indexArray = new Integer[positions.size()];
                positions.toArray(indexArray);
                int selectPosition = getSelectPosition(indexArray, position);
                listIndicator.setSelectPosition(selectPosition);
                label.setText(words[selectPosition]);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    //滑动结束时消失
                    ViewCompat.animate(label).alpha(0f).scaleX(0f).scaleY(0f).setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(300);
                } else if (RecyclerView.SCROLL_STATE_SETTLING == newState) {
                    //按下滑动时展示
                    ViewCompat.animate(label).alpha(1f).scaleX(1f).scaleY(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(300);
                }
            }
        });

    }

    /**
     * 使用二分查找法,根据firstVisiblePosition找到SelectPositions中的位置
     *
     * @return
     */
    public static int getSelectPosition(Integer[] positions, int firstVisiblePosition) {
        int start = 0, end = positions.length;
        while (end - start > 1) {
            // 中间位置
            int middle = (start + end) >> 1;
            // 中值
            int middleValue = positions[middle];
            if (firstVisiblePosition > middleValue) {
                start = middle;
            } else if (firstVisiblePosition < middleValue) {
                end = middle;
            } else {
                start = middle;
                break;
            }
        }
        return start;
    }
}
