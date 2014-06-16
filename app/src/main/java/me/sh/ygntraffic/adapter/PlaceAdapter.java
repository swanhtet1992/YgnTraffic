package me.sh.ygntraffic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sh.ygntraffic.R;
import me.sh.ygntraffic.model.Place;

/**
 * Created by SH on 16/Jun/2014.
 */
public class PlaceAdapter extends ArrayAdapter<Place> {
  private final Context mContext;
  private ArrayList<Place> places = new ArrayList<Place>();

  public PlaceAdapter(Context context, ArrayList<Place> places) {
    super(context, R.layout.place_item, places);

    this.mContext = context;
    this.places = places;
    Log.e("", "Places Size (Adapter): " + places.size());
  }

  private ViewHolder getHolder(View v) {
    ViewHolder holder = new ViewHolder(v);

    return holder;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    View vi = convertView;

    if (vi == null) {
      LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      vi = inflater.inflate(R.layout.place_item, parent, false);
      holder = getHolder(vi);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }

    holder.name.setText(places.get(position).getName());

    String status = places.get(position).getStatus();
    if (status.equalsIgnoreCase("1")) {
      holder.status.setText("Normal");
      holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.green));
    } else if (status.equalsIgnoreCase("2")) {
      holder.status.setText("Bad Traffic");
      holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
    } else if (status.equalsIgnoreCase("3")) {
      holder.status.setText("Worst Traffic");
      holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.red));
    } else {
      holder.status.setText("Unknown");
      holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
    }
    holder.time.setText(places.get(position).getReportedTime());

    return vi;
  }

  static class ViewHolder {
    @InjectView(R.id.txt_name)
    TextView name;
    @InjectView(R.id.txt_status)
    TextView status;
    @InjectView(R.id.txt_time)
    TextView time;

    public ViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}

