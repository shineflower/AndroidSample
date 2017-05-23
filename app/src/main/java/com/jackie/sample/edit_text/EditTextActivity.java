package com.jackie.sample.edit_text;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ClearEditText;

public class EditTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_text);

        final ClearEditText username = (ClearEditText) findViewById(R.id.username);
        final ClearEditText password = (ClearEditText) findViewById(R.id.password);
        Button button  = (Button) findViewById(R.id.login);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(username.getText().toString().trim())){
                    //设置晃动
                    username.setShakeAnimation();
                    //设置提示
                    Toast.makeText(EditTextActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password.getText().toString().trim())){
                    password.setShakeAnimation();
                    Toast.makeText(EditTextActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}
