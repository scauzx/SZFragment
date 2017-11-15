package scauzx.com.myapplication;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.scauzx.utils.OsUtil;

import static android.os.Build.*;

/**
 * @author scauzx
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String Title[] = {"First","Second","Third","Fourth"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
    }



    private void setupView() {
        initToolBar();
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        for (int i = 0; i < Title.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setLayoutMode(TabLayout.MODE_SCROLLABLE);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < Title.length; i++) {
            if (mTabLayout.getTabAt(i) != null) {
                mTabLayout.getTabAt(i).setText(Title[i]);
            }
        }
    }

    private void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mToolBar.setPadding(0, OsUtil.getStatusBarHeight(this), 0, 0);
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    class MyAdapter extends FragmentPagerAdapter{



        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position % 2 ==0) {
                return FirstFragment.getInstance();
            }

            return SecondFragment.getInstance();
        }

        @Override
        public int getCount() {
            return Title.length;
        }
    }

}

