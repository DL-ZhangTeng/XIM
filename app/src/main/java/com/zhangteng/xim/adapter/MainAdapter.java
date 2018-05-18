package com.zhangteng.xim.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhangteng.xim.base.BaseFragment;
import com.zhangteng.xim.fragment.CircleFragment;
import com.zhangteng.xim.fragment.MessageFragment;
import com.zhangteng.xim.fragment.LinkmanFragment;

import java.util.ArrayList;

/**
 * Created by Lanxumit on 2017/11/24.
 */

public class MainAdapter extends FragmentPagerAdapter {
    private String[] titles = {"消息", "联系人", "朋友圈"};
    private ArrayList<Fragment> fragmentlist = new ArrayList<Fragment>();

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    public ArrayList<Fragment> getFragmentlist() {
        return fragmentlist;
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MessageFragment();
                break;
            case 1:
                fragment = new LinkmanFragment();
                break;
            case 2:
                fragment = new CircleFragment();
            default:
                break;
        }
        fragmentlist.add(fragment);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
