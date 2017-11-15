package scauzx.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public class SecondFragment extends BaseFragment {

    public static SecondFragment getInstance() {
        return new SecondFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_second,null);
    }
}
