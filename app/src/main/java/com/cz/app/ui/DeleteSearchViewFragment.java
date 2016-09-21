package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.cz.app.R;
import com.cz.app.widget.SeekLayout;
import com.cz.library.widget.DeleteEditText;

/**
 * Created by Administrator on 2016/9/21.
 */
public class DeleteSearchViewFragment extends Fragment {
    public static final String TAG = "DeleteSearchViewFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_search_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DeleteEditText editor= (DeleteEditText) view.findViewById(R.id.dt_editor);
        SeekLayout seekLayout= (SeekLayout) view.findViewById(R.id.sl_layout);
        editor.setOnDeleteItemClickListener(new DeleteEditText.OnDeleteItemClickListener() {
            @Override
            public void onTextDeleted() {

            }
        });
        editor.setTextChangeListener(new DeleteEditText.TextChangeListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG,"text:"+s);
            }
        });
        seekLayout.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                int offset=progress<50?-progress:100-progress;
                editor.setDrawableCenterOffset(offset);
            }
        });
    }
}
