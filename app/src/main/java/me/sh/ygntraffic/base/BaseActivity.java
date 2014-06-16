package me.sh.ygntraffic.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import me.sh.ygntraffic.R;

/**
 * Created by SH on 16/Jun/2014.
 */
public class BaseActivity extends ActionBarActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
  }

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
  }
}
