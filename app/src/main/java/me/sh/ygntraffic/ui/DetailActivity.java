package me.sh.ygntraffic.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
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
  String recent = null;
  String reportTime = null;

  @InjectView(R.id.trafficName) TextView trafficName;

  @InjectView(R.id.trafficRemark) TextView trafficRemark;

  @InjectView(R.id.trafficReportTime) TextView trafficReportTime;

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
    setContentView(R.layout.activity_detail);
    ButterKnife.inject(this);
    trafficName.setText(name);
    trafficRemark.setText(remark);
    trafficReportTime.setText(reportTime);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.detail, menu);
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
    } else if (id == android.R.id.home) {
      this.onBackPressed();
      finish();
      return false;
    }
    return super.onOptionsItemSelected(item);
  }
}
