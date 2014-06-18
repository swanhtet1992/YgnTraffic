package me.sh.ygntraffic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sh.ygntraffic.R;
import me.sh.ygntraffic.adapter.PagerAdapter;
import me.sh.ygntraffic.base.BaseActivity;
import me.sh.ygntraffic.fragment.TrafficFragment;
import me.sh.ygntraffic.model.Place;

public class MainActivity extends BaseActivity
    implements ActionBar.TabListener, TrafficFragment.OnPlaceClickListener {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  PagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  @InjectView(R.id.pager)
  ViewPager mViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);

    // Set up the action bar.
    final ActionBar actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());

    // Set up the ViewPager with the sections adapter.
    mViewPager.setAdapter(mSectionsPagerAdapter);

    // When swiping between different sections, select the corresponding
    // tab. We can also use ActionBar.Tab#select() to do this if we have
    // a reference to the Tab.
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
      }
    });

    // For each of the sections in the app, add a tab to the action bar.
    for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
      // Create a tab with text corresponding to the page title defined by
      // the adapter. Also specify this Activity object, which implements
      // the TabListener interface, as the callback (listener) for when
      // this tab is selected.
      actionBar.addTab(
          actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    // When the given tab is selected, switch to the corresponding page in
    // the ViewPager.
    mViewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
  }

  @Override
  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
  }

  @Override
  public void onPlaceClick(ArrayList<Place> placeArrayList, int position) {
    Bundle arguments = new Bundle();
    Intent intentToDetail = new Intent(getApplicationContext(), DetailActivity.class);
    String name;
    String flag;
    String date;
    String remark;
    String reportTime;
    String status;
    int color;
    name = placeArrayList.get(position).getName();
    flag = placeArrayList.get(position).getFlag();
    date = placeArrayList.get(position).getCreatedDate();
    remark = placeArrayList.get(position).getRemark();
    reportTime = placeArrayList.get(position).getReportedTime();
    status = placeArrayList.get(position).getStatus();
    color = placeArrayList.get(position).getStatusColor(this, status);
    arguments.putString("name", name);
    arguments.putString("flag", flag);
    arguments.putString("date", date);
    arguments.putString("remark", remark);
    arguments.putString("reportTime", reportTime);
    arguments.putString("status", status);
    arguments.putInt("color", color);
    intentToDetail.putExtras(arguments);
    startActivity(intentToDetail);
  }
}
