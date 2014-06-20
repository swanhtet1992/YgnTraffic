package me.sh.ygntraffic.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sh.ygntraffic.R;
import me.sh.ygntraffic.model.Place;

public class DetailActivity extends ActionBarActivity {

  public static final String PLACE = "place";
  private Place mPlace;

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

    setContentView(R.layout.activity_detail);
    ButterKnife.inject(this);

    if (!getIntent().getExtras().isEmpty()) {
      mPlace = (Place) getIntent().getExtras().get(PLACE);
      trafficRemark.setText(mPlace.getStatus());
      trafficRemark.setBackgroundColor(mPlace.getStatusColor(this, mPlace.getStatus()));
      trafficName.setText(mPlace.getName());
      trafficReportTime.setText(mPlace.getReportedTime());
    }
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
