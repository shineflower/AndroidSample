package com.jackie.sample.address;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.AddressSelectDialog;

/**
 * Created by Jackie on 2017/6/23.
 */

public class AddressSelectActivity extends AppCompatActivity {
    private TextView mTextView;

    private AddressSelectDialog mAddressSelectDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address_select);

        mTextView = (TextView) findViewById(R.id.address);

        findViewById(R.id.choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressSelectDialog == null) {
                    mAddressSelectDialog = new AddressSelectDialog(AddressSelectActivity.this);

                    mAddressSelectDialog.setOnAddressListener(new AddressSelectDialog.onAddressListener() {
                        @Override
                        public void onAddress(String province, String city, String district, String street) {
                            mTextView.setText(province + city + district + street);
                        }
                    });
                }

                mAddressSelectDialog.setCancelable(true);
                mAddressSelectDialog.show();
            }
        });
    }
}
