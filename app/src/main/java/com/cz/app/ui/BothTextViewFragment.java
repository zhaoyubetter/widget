package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cz.app.R;
import com.cz.app.widget.CheckLayout;
import com.cz.app.widget.RadioLayout;
import com.cz.library.widget.BothTextView;

import java.util.ArrayList;

/**
 * Created by cz on 9/14/16.
 */
public class BothTextViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_both_text_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final BothTextView textView= (BothTextView) view.findViewById(R.id.bv_text_View);
        CheckLayout gravityCheckLayout= (CheckLayout) view.findViewById(R.id.cl_gravity);
        final int[] gravities=new int[]{0x01,0x02,0x04,0x08};
        gravityCheckLayout.setOnCheckedListener(new CheckLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, ArrayList<Integer> items, int position) {
                int value=0;
                for(int item:items){
                    value+=gravities[item];
                }
                textView.setHandTextGravity(value);
            }
        });
        RadioLayout attachmentLayout= (RadioLayout) view.findViewById(R.id.cl_attachment);
        attachmentLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                textView.setAttachmentModeInner(position);
            }
        });
    }
}
