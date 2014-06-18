package me.sh.ygntraffic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import me.sh.ygntraffic.R;
import me.sh.ygntraffic.base.BaseFragment;

/**
 * Created by SH on 16/Jun/2014.
 */
public class MyPlaceFragment extends BaseFragment {

  public static MyPlaceFragment newInstance() {
    MyPlaceFragment fragment = new MyPlaceFragment();
    return fragment;
  }

  public MyPlaceFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_place, container, false);
    ButterKnife.inject(this, view);

    return view;
  }
}
