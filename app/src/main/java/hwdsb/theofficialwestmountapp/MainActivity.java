package hwdsb.theofficialwestmountapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    // Instantiate main view, toolbar, view pager, and tabbed layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.context = this;

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    // Unused because no menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Context context;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Get fragment for tab position
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FeedFragment feedFragment = new FeedFragment();
                    feedFragment.context = context;
                    return feedFragment;
                case 1:
                    TimerFragment timerFragment = new TimerFragment();
                    timerFragment.context = context;
                    return timerFragment;
                case 2:
                    PlannerFragment plannerFragment = new PlannerFragment();
                    plannerFragment.context = context;
                    return plannerFragment;
            }
            return null;
        }

        // Get number of tabs
        @Override
        public int getCount() {
            return 3;
        }

        // Get title of tabs
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FEED";
                case 1:
                    return "PERIOD PLANNER";
                case 2:
                    return "COURSES";
            }
            return null;
        }
    }

}
