package me.sh.ygntraffic.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.util.FileUtility;
import com.koushikdutta.ion.Ion;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.sh.ygntraffic.R;
import me.sh.ygntraffic.base.BaseActivity;
import me.sh.ygntraffic.model.Place;
import me.sh.ygntraffic.util.Constants;
import me.sh.ygntraffic.util.NetUtil;

public class DetailActivity extends BaseActivity {

  public static final String PLACE = "place";
  private Place mPlace;
  private ProgressDialog mDialog;

  @InjectView(R.id.traffic_name)
  TextView trafficName;
  @InjectView(R.id.traffic_remark)
  TextView trafficRemark;
  @InjectView(R.id.traffic_report_time)
  TextView trafficReportTime;
  @InjectView(R.id.btn_myplace)
  Button btnMyPlace;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    setContentView(R.layout.activity_detail);
    ButterKnife.inject(this);
    mDialog = new ProgressDialog(this);
    if (!getIntent().getExtras().isEmpty()) {
      mPlace = (Place) getIntent().getExtras().get(PLACE);
      trafficRemark.setText(mPlace.getStatus());
      trafficRemark.setBackgroundColor(mPlace.getStatusColor(this, mPlace.getStatus()));
      trafficName.setText(mPlace.getName());
      trafficReportTime.setText(mPlace.getReportedTime());
    }
  }

  @OnClick(R.id.btn_myplace)
  public void saveAsMyPlace() {
    btnMyPlace.setText("Saved");
  }

  @OnClick(R.id.btn_report)
  public void reportTraffic() {
    createStatusDialog();
  }

  private void createStatusDialog() {
    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
    builderSingle.setTitle(mPlace.getName());
    final String[] status = getResources().getStringArray(R.array.report_status);
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, status);

    builderSingle.setNegativeButton("Dismiss",
        new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        }
    );

    builderSingle.setAdapter(arrayAdapter,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            if (which == 0) {
              createRemarkDialog("3", status[which]);
            } else if (which == 1) {
              createRemarkDialog("2", status[which]);
            } else if (which == 2) {
              reportToServer("1", "");
            }
          }
        }
    );
    builderSingle.show();
  }

  private void createRemarkDialog(final String id, String title) {
    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
    builderSingle.setTitle(title);
    String[] remark = getResources().getStringArray(R.array.report_remark);
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, remark);

    builderSingle.setNegativeButton("Dismiss",
        new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        }
    );

    builderSingle.setAdapter(arrayAdapter,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            int remark = which + 1;
            reportToServer(id, remark + "");
          }
        }
    );
    builderSingle.show();
  }

  private void reportToServer(final String status, String remark) {
    if (NetUtil.isOnline(context)) {
      mDialog.setMessage(getString(R.string.loading_report));
      mDialog.show();
      Ion.with(context)
          .load(Constants.REPORT + mPlace.getName() + "&s=" + status + "&c=" + remark)
          .setLogging("ION-ReportTraffic", Log.DEBUG)
          .asString()
          .setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
              mDialog.dismiss();
              Log.d("", "Result: " + result);
              if (result.equalsIgnoreCase("") || result == null) {
                mDialog.setMessage(getString(R.string.error_reprot));
                mDialog.show();
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                  @Override
                  public void onCancel(DialogInterface dialog) {
                    finish();
                  }
                });
              } else {
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(result);
                Toast.makeText(context, "Reported: " + jsonObject.get("remark"), Toast.LENGTH_SHORT).show();
              }
            }
          });
    } else {
      Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
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
