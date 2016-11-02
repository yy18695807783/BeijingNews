package com.atguigu.addsubviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    private AddSubView add_sub_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_sub_view = (AddSubView)findViewById(R.id.add_sub_view);

        add_sub_view.setOnButtonSubAddChangeListener(new AddSubView.OnButtonSubAddChangeListener() {
            @Override
            public void onButtonSubChange(View v, int value) {
                Toast.makeText(MainActivity.this, "减少" + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonAddChange(View v, int value) {
                Toast.makeText(MainActivity.this, "增加" + value, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
