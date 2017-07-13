package com.jackie.sample.share_element;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetailFragment extends Fragment {
    private static final String ARG_NUMBER = "arg_number";
    private List<DetailData> mDetailList;

    private ImageView mImage;
    private TextView mHead;
    private TextView mBody;

    public PhotoDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PhotoDetailFragment newInstance(int position ) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImage = (ImageView) view.findViewById(R.id.detail_image);
        mHead = (TextView) view.findViewById(R.id.detail_head);
        mBody = (TextView) view.findViewById(R.id.detail_body);
        initData();

        int number = getArguments().getInt(ARG_NUMBER);
        mImage.setImageResource(mDetailList.get(number).getImage());
        mHead.setText(mDetailList.get(number).getHead());
        mBody.setText(mDetailList.get(number).getBody());

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    // 初始化数据
    private void initData() {
        mDetailList = new ArrayList<>();
        mDetailList.add(new DetailData(R.drawable.taeyeon, R.string.taeyeon, R.string.taeyeon_detail));
        mDetailList.add(new DetailData(R.drawable.jessica, R.string.jessica, R.string.jessica_detail));
        mDetailList.add(new DetailData(R.drawable.sunny, R.string.sunny, R.string.sunny_detail));
        mDetailList.add(new DetailData(R.drawable.tiffany, R.string.tiffany, R.string.tiffany_detail));
        mDetailList.add(new DetailData(R.drawable.yuri, R.string.yuri, R.string.yuri_detail));
        mDetailList.add(new DetailData(R.drawable.yoona, R.string.yoona, R.string.yoona_detail));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // 定义类
    private class DetailData {
        private int mImage;
        private int mHead;
        private int mBody;

        public DetailData(int image, int head, int body) {
            mImage = image;
            mHead = head;
            mBody = body;
        }

        public int getImage() {
            return mImage;
        }

        public int getHead() {
            return mHead;
        }

        public int getBody() {
            return mBody;
        }
    }
}
