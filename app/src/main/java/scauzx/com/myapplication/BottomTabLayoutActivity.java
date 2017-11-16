package scauzx.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;

import com.scauzx.utils.DataGenerator;

import java.lang.ref.WeakReference;

/**
 *
 * @author scauzx
 * @date 2017/11/16
 */

public class BottomTabLayoutActivity extends BaseActivity {
    private TabLayout mTabLayout;

    private SparseArrayCompat<WeakReference<Fragment>> mFragments = new SparseArrayCompat<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
      //  mFragments = DataGenerator.getFragments();
        initView();

    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                //改变Tab 状态
                for(int i=0;i< mTabLayout.getTabCount();i++){
                    if(i == tab.getPosition()){
                        mTabLayout.getTabAt(i).setIcon(getResources().getDrawable(DataGenerator.mTabResPressed[i]));
                    }else{
                        mTabLayout.getTabAt(i).setIcon(getResources().getDrawable(DataGenerator.mTabRes[i]));
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.auth_icon_twitter)).setText(DataGenerator.mTabTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.auth_icon_twitter)).setText(DataGenerator.mTabTitle[1]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.auth_icon_twitter)).setText(DataGenerator.mTabTitle[2]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(getResources().getDrawable(R.mipmap.auth_icon_twitter)).setText(DataGenerator.mTabTitle[3]));

    }

    private void onTabItemSelected(int position){
        Fragment fragment = null;
        if (mFragments.get(position) != null) {
            fragment = mFragments.get(position).get();
        }
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = FollowFragment.getInstance();
                    break;
                case 1:
                    fragment = SecondFragment.getInstance();
                    break;
                case 2:
                    fragment = ThirdFragment.getInstance();
                    break;
                case 3:
                    fragment = FourthFragment.getInstance();
                    break;
                default:
                    fragment = FourthFragment.getInstance();
                    break;
            }
            mFragments.put(position,new WeakReference<>(fragment));
        }
        if (fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
        }
    }
}