package scauzx.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;

/**
 *
 * @author scauzx
 * @date 2017/11/15
 */

public class FourthFragment extends BaseFragment {
    private String TAG = "FourthFragment";

    public static FourthFragment getInstance() {
        return new FourthFragment();
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
