package me.sh.ygntraffic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import me.sh.ygntraffic.R;
import me.sh.ygntraffic.fragment.MyPlaceFragment;
import me.sh.ygntraffic.fragment.TrafficFragment;


/**
 * Created by SH on 16/Jun/2014.
 */

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PagerAdapter extends FragmentPagerAdapter {

  private Context mContext;

  public PagerAdapter(FragmentManager fm, Context context) {
    super(fm);
    mContext = context;
  }

  @Override
  public Fragment getItem(int position) {
    // getItem is called to instantiate the fragment for the given page.
    // Return a PlaceholderFragment (defined as a static inner class below).
    switch (position) {
      case 0:
        return TrafficFragment.newInstance();
      case 1:
        return MyPlaceFragment.newInstance();
      default:
        return TrafficFragment.newInstance();
    }
  }

  @Override
  public int getCount() {
    // Show 3 total pages.
    return 2;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    Locale l = Locale.getDefault();
    switch (position) {
      case 0:
        return mContext.getString(R.string.title_traffic).toUpperCase(l);
      case 1:
        return mContext.getString(R.string.title_my_place).toUpperCase(l);
    }
    return null;
  }
}