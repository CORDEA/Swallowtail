package jp.cordea.swallowtail.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.cordea.swallowtail.BuildType;
import jp.cordea.swallowtail.MainPageFragment;
import jp.cordea.swallowtail.R;

/**
 * Created by Yoshihiro Tanaka on 2016/07/10.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MainPageFragment.newInstance(BuildType.LATEST);
            case 1:
                return MainPageFragment.newInstance(BuildType.FAILED);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.latest_fragment_title);
            case 1:
                return context.getResources().getString(R.string.failed_fragment_title);
        }
        return super.getPageTitle(position);
    }
}
