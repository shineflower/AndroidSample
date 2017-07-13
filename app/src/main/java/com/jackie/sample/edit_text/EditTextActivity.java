package com.jackie.sample.edit_text;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ClearEditText;
import com.jackie.sample.custom_view.TextInputLayout;

public class EditTextActivity extends AppCompatActivity {
    private TextInputLayout mTextInputLayout;
    private EditText mEditText;

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

        mTextInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);

        mEditText = (EditText) findViewById(R.id.text_edit_text);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 11) {
                    mTextInputLayout.setStateWrong("手机号码格式不对");
                } else {
                    mTextInputLayout.setStateNormal();
                }
            }
        });
    }
}
