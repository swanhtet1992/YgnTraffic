package me.sh.ygntraffic.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by SH on 16/Jun/2014.
 */
public class BaseFragment extends Fragment {
  protected Context context;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Retain this fragment across configuration changes.
    setRetainInstance(true);

    context = getActivity().getApplicationContext();
  }
}
