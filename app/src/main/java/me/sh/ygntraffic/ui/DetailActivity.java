package me.sh.ygntraffic.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sh.ygntraffic.R;

public class DetailActivity extends ActionBarActivity {

  String name = null;
  String flag = null;
  String date = null;
  String remark = null;
  String status = null;
  String reportTime = null;
  int color;

  @InjectView(R.id.traffic_name)
  TextView trafficName;
  @InjectView(R.id.traffic_remark)
  TextView trafficRemark;
  @InjectView(R.id.traffic_report_time)
  TextView trafficReportTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    Bundle bundleFromFragment = getIntent().getExtras();
    name = bundleFromFragment.getString("name");
    flag = bundleFromFragment.getString("flag");
    date = bundleFromFragment.getString("date");
    reportTime = bundleFromFragment.getString("reportTime");
    remark = bundleFromFragment.getString("remark");
    status = bundleFromFragment.getString("status");
    color = bundleFromFragment.getInt("color");

    setContentView(R.layout.activity_detail);
    ButterKnife.inject(this);

    trafficRemark.setText(status);
    trafficRemark.setBackgroundColor(color);
    trafficName.setText(name);
    trafficReportTime.setText(reportTime);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == android.R.id.home) {
      this.onBackPressed();
      finish();
      return false;
    }
    return super.onOptionsItemSelected(item);
  }
}
