package com.cz.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

/**
 * 一个activity壳子
 *
 * @author momo
 * @Date 2014/6/19
 */
public class ShellActivity extends AppCompatActivity {
    public static final String CLASS_TAG = "class";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);
        setTitle(getIntent().getStringExtra("title"));
        if (null == savedInstanceState) {
            addFragment();
        }
    }


    /**
     * 检测数据类型,跳转到指定fragment
     */

    private void addFragment() {
        Intent intent = getIntent();
        if (null != intent) {
            String className = intent.getStringExtra(CLASS_TAG);
            if (!TextUtils.isEmpty(className)) {
                try {
                    Class<?> clazz = Class.forName(className);
                    Object instance = clazz.newInstance();
                    if (instance instanceof Fragment) {
                        Fragment fragment = (Fragment) instance;
                        Bundle extras = getIntent().getExtras();
                        if (null != extras) {
                            fragment.setArguments(extras);
                        }
                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
            }
        }
    }


    /**
     * 跳转到一个activity内
     */
    public static void toActivity(Activity activity, Class<? extends Fragment> clazz, Bundle extras) {
        if (null != activity) {
            Intent intent = new Intent(activity, ShellActivity.class);
            intent.putExtra(CLASS_TAG, clazz.getName());
            if (null != extras) {
                intent.putExtras(extras);
            }
            activity.startActivity(intent);
        }
    }

}
