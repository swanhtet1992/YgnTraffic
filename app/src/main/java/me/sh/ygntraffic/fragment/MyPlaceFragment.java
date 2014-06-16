package me.sh.ygntraffic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sh.ygntraffic.R;
import me.sh.ygntraffic.base.BaseFragment;

/**
 * Created by SH on 16/Jun/2014.
 */
public class MyPlaceFragment extends BaseFragment {

  @InjectView(android.R.id.list)
  AbsListView mListView;
  @InjectView(android.R.id.empty)
  TextView mEmptyView;

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
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_place, container, false);
    ButterKnife.inject(this, view);
    mEmptyView.setText("Not implemented yet.");
    return view;
  }
}
