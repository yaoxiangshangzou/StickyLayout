package com.licrafter.scrolllayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.licrafter.scrolllayout.view.SwipeRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * author: shell
 * date 16/10/9 下午2:40
 **/
public class PageFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private int mPosition;
    private List<String> mList = new ArrayList<>();;
    private GoodsAdapter mAdapter;

    public static PageFragment getInstance(int position) {
        PageFragment fragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPosition = getArguments().getInt("position");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new GoodsAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void loadmore() {
        mList.clear();
        mList.addAll(DataResource.getMoreData());
        Toast.makeText(getActivity(), "加载了" + 20 + "条数据", Toast.LENGTH_SHORT).show();

        // 加载完数据设置为不加载状态，将加载进度收起来
//        mSwipeRefreshView.setLoading(false);
    }
    public void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                mList.addAll(DataResource.getData());
                mAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "刷新了20条数据", Toast.LENGTH_SHORT).show();

                // 加载完数据设置为不刷新状态，将下拉进度收起来
                SwipeRefreshLayout srl = ((HomeFragment) getParentFragment()).getSrl();
                if (srl.isRefreshing()) {
                    srl.setRefreshing(false);
                }
            }
        }, 2000);
    }
    public void loadMoreData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mList.clear();
                mList.addAll(DataResource.getMoreData());
                Toast.makeText(getActivity(), "加载了" + 20 + "条数据", Toast.LENGTH_SHORT).show();

                // 加载完数据设置为不加载状态，将加载进度收起来
                SwipeRefreshView srl = ((HomeFragment) getParentFragment()).getSrl();
                srl.setLoading(false);
            }
        }, 2000);
    }
    private class GoodsAdapter extends RecyclerView.Adapter<GoodsVH> {

        @Override
        public GoodsVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GoodsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false));
        }

        @Override
        public void onBindViewHolder(GoodsVH holder, int position) {
            holder.textView.setText("商品类别->" + mPosition + "  位置->" + position);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private class GoodsVH extends RecyclerView.ViewHolder {

        TextView textView;

        GoodsVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "点击商品", Toast.LENGTH_SHORT).show();
                    android.util.Log.d("ljx", "click Goods");
                }
            });
        }
    }

    public static class DataResource {
        private static List<String> datas = new ArrayList<>();
        private static int page = 0;

        public static List<String> getData() {
            page = 0;
            datas.clear();
            for (int i = 0; i < 20; i++) {
                datas.add("我是天才" + i + "号");
            }

            return datas;
        }

        public static List<String> getMoreData() {
            page = page + 1;
            for (int i = 20 * page; i < 20 * (page + 1); i++) {
                datas.add("我是天才" + i + "号");
            }

            return datas;
        }
    }
}
