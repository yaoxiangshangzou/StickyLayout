package com.licrafter.scrolllayout;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.licrafter.scrolllayout.view.SwipeRefreshView;
import com.squareup.picasso.Picasso;

/**
 * author: shell
 * date 16/10/9 下午2:30
 **/
public class HomeFragment extends Fragment {

    ViewPager mGoodsViewPager;
    ImageView mAdvHeader;
    TabLayout mTabLayout;
    GoodsPageAdapter mPageAdapter;
    private SwipeRefreshView srl = null;
    private PageFragment mFragmentByTag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public SwipeRefreshView getSrl() {
        return srl;
    }

    public void setSrl(SwipeRefreshView srl) {
        this.srl = srl;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srl = (SwipeRefreshView) view.findViewById(R.id.srl);

        initEvent();

        mAdvHeader = (ImageView) view.findViewById(R.id.header01);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mGoodsViewPager = (ViewPager) view.findViewById(R.id.goodsViewPager);
        mTabLayout.setupWithViewPager(mGoodsViewPager);


        Picasso.with(getActivity()).load("https://img.alicdn.com/tps/TB1zZD3NFXXXXXxaFXXXXXXXXXX-1920-320.jpg_Q90.jpg")
                .into(mAdvHeader);

        mPageAdapter = new GoodsPageAdapter(getChildFragmentManager());
        mGoodsViewPager.setAdapter(mPageAdapter);
        mGoodsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String tag = mFragmentTags.get(position);
                if (tag == null) {
                    return ;
                }
                mFragmentByTag = (PageFragment) getChildFragmentManager().findFragmentByTag(tag);
                ((PageFragment) mFragmentByTag).loadmore();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvent() {

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFragmentByTag.initData();
            }
        });


        // 设置下拉加载更多
        srl.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mFragmentByTag.loadMoreData();
            }
        });
    }

    private SparseArray<String> mFragmentTags = new SparseArray<>();

    public class GoodsPageAdapter extends FragmentPagerAdapter {

        private String[] titles = new String[]{"最新", "进度", "需求", "热门"};

        public GoodsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        public PageFragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            mFragmentByTag = (PageFragment) getChildFragmentManager().findFragmentByTag(tag);
            return mFragmentByTag;
        }

    }
}
