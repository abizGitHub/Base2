package com.tut.abiz.base.acts;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tut.abiz.base.R;
import com.tut.abiz.base.frags.SlideFragment;
import com.tut.abiz.base.model.GeneralModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abiz on 5/16/2019.
 */

public class TabAct extends BaseActivity {

    ViewPager viewpager;
    TabLayout tabs;
    SlidePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tab);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (TabLayout) findViewById(R.id.tabLayout);
        setUpViewPager();
        tabs.setupWithViewPager(viewpager);
        String type = "IconAndText";//getIntent().getStringExtra("type");
        if ("simple".equals(type)) {
            tabs.setTabMode(TabLayout.MODE_FIXED);
        } else if ("scrollable".equals(type)) {
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            prepareSlides();
        } else if ("IconAndText".equals(type)) {
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            setUpTabIcons();
        } else if ("onlyIcon".equals(type)) {
            tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabs.setTabMode(TabLayout.MODE_FIXED);
            setUpTabIcons();
            for (int i = 0; i < tabs.getTabCount(); i++)
                tabs.getTabAt(i).setText("");
        }
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setSelectedTable(viewpager.getCurrentItem() + 1);
                Toast.makeText(TabAct.this, ">" + getSelectedTable(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    if (i == viewpager.getCurrentItem())
                        tabs.getTabAt(i).getIcon().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    else
                        tabs.getTabAt(i).getIcon().clearColorFilter();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setUpTabIcons() {
        if (tabs.getTabCount() < 4)
            return;
        tabs.getTabAt(0).setIcon(R.drawable.ic_food);
        tabs.getTabAt(1).setIcon(R.drawable.ic_movie);
        tabs.getTabAt(2).setIcon(R.drawable.ic_discount);
        tabs.getTabAt(3).setIcon(R.drawable.ic_travel);
    }

    private void prepareSlides() {
        String[] titles = getResources().getStringArray(R.array.titles);
        String[] descriptions = getResources().getStringArray(R.array.descriptions);
        int[] bgColors = new int[]{R.color.colorMagnet, R.color.bgColor2,
                R.color.bgColor3, R.color.colorOrange};
        int[] imageIds = new int[]{R.drawable.ic_food, R.drawable.ic_movie,
                R.drawable.ic_discount, R.drawable.ic_travel};
        for (int i = 0; i < 4; i++) {
            pagerAdapter.addFragment(
                    SlideFragment.newSlide(ContextCompat.getColor(this, bgColors[i]),
                            imageIds[i], titles[i], descriptions[i]),
                    titles[i]);
        }
    }

    private void setUpViewPager() {
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        prepareSlides();
    }

    public class SlidePagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;
        List<String> tabTitles;

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            tabTitles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public void addFragment(Fragment fragment, String tabTitle) {
            fragments.add(fragment);
            tabTitles.add(tabTitle);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
    }

    @Override
    protected void doStaredTasks() {

    }

    @Override
    protected ArrayAdapter getListAdapter() {
        return null;
    }

    @Override
    protected ArrayList<String> getGeneralTitles() {
        return null;
    }

    @Override
    protected ArrayList<GeneralModel> getGeneralList() {
        return null;
    }

    @Override
    public void onStarChanged(int position, boolean checked) {

    }
}
