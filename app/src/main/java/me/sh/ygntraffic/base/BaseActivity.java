package me.sh.ygntraffic.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import me.sh.ygntraffic.R;

/**
 * Created by SH on 16/Jun/2014.
 */
public class BaseActivity extends ActionBarActivity {
  protected Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this.getApplicationContext();
    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
  }

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
  }
}
