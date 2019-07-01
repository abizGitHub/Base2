package com.tut.abiz.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.tut.abiz.base.frags.PagerFragment;
import com.tut.abiz.base.model.FragmentPack;

import java.util.ArrayList;

/**
 * Created by abiz on 4/13/2019.
 */

public class GeneralPagerAdapter extends FragmentPagerAdapter {

    private int PAGE_COUNT;
    private ArrayList<FragmentPack> fragmentPacks;

    /**
     * Constructor of the class
     */
    public GeneralPagerAdapter(FragmentManager fm, ArrayList<FragmentPack> fragmentPacks) {
        super(fm);
        this.fragmentPacks = fragmentPacks;
        PAGE_COUNT = fragmentPacks.size();
    }

    /**
     * This method will be invoked when a page is requested to create
     */
    @Override
    public Fragment getItem(int arg0) {
        PagerFragment myFragment = fragmentPacks.get(arg0).getPagerFragment();
        //Bundle data = new Bundle();
        /*data.putInt(Consts.CURRENTPAGE, arg0);
        data.putInt(Consts.PAGELAYOUT, pagesLayout.get(arg0));
        myFragment.setArguments(data);*/
        return myFragment;
    }

    /**
     * Returns the number of pages
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentPacks.get(position).getPageTitle();
    }

}
