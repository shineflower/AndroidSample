package com.jackie.sample.share_element;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackie.sample.R;
import com.jackie.sample.adapter.PhotoGridAdapter;
import com.jackie.sample.listener.OnPhotoGridItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class PhotoListFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private List<Pair<Integer, Integer>> mList;

    public PhotoListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(new PhotoGridAdapter(mList, mListener));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 一行两个
    }

    // 初始化数据
    private void initData() {
        mList = new ArrayList<>();

        mList.add(Pair.create(R.string.taeyeon, R.drawable.taeyeon));
        mList.add(Pair.create(R.string.jessica, R.drawable.jessica));
        mList.add(Pair.create(R.string.sunny, R.drawable.sunny));
        mList.add(Pair.create(R.string.tiffany, R.drawable.tiffany));
        mList.add(Pair.create(R.string.yuri, R.drawable.yuri));
        mList.add(Pair.create(R.string.yoona, R.drawable.yoona));
    }

    /**
     * 点击事件, 转换元素的动画,
     * 关键addSharedElement(holder.getImageView(), getResources().getString(R.string.image_transition))
     * 绑定ViewHolder的图片和DetailFragment的跳转.
     */
    private OnPhotoGridItemClickListener mListener = new OnPhotoGridItemClickListener() {
        @Override
        public void onPhotoGridItemClick(PhotoGridAdapter.ViewHolder holder, int position) {
            PhotoDetailFragment detailFragment = PhotoDetailFragment.newInstance(position);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                detailFragment.setSharedElementEnterTransition(new DetailTransition());
                setExitTransition(new Fade());
                detailFragment.setEnterTransition(new Fade());
                detailFragment.setSharedElementReturnTransition(new DetailTransition());
            }

            getActivity().getSupportFragmentManager().beginTransaction()
                    .addSharedElement(holder.imageView, getResources().getString(R.string.image_transition))
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
