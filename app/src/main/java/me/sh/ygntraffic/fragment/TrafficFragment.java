package me.sh.ygntraffic.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sh.ygntraffic.R;
import me.sh.ygntraffic.adapter.PlaceAdapter;
import me.sh.ygntraffic.base.BaseFragment;
import me.sh.ygntraffic.model.Place;
import me.sh.ygntraffic.util.Constants;
import me.sh.ygntraffic.util.NetUtil;

public class TrafficFragment extends BaseFragment implements AbsListView.OnItemClickListener {

  private OnPlaceClickListener mListener;

  /**
   * The fragment's ListView/GridView.
   */
  @InjectView(android.R.id.list)
  AbsListView mListView;
  @InjectView(R.id.progress_bar)
  ProgressBar mProgressBar;

  /**
   * The Adapter which will be used to populate the ListView/GridView with
   * Views.
   */
  private PlaceAdapter mAdapter;
  private ArrayList<Place> mPlaces = new ArrayList<Place>();

  public static TrafficFragment newInstance() {
    TrafficFragment fragment = new TrafficFragment();
    return fragment;
  }

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public TrafficFragment() {
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

    if (NetUtil.isOnline(getActivity().getApplicationContext())) {
      mProgressBar.setVisibility(View.VISIBLE);
      getAllTraffic();
    } else {
      setEmptyText(getString(R.string.error_internet));
    }

    // Set OnItemClickListener so we can be notified on item clicks
    mListView.setOnItemClickListener(this);
    setHasOptionsMenu(true);

    return view;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnPlaceClickListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  private void getAllTraffic() {
    Ion.with(getActivity())
        .load(Constants.ALL_TRAFFIC)
        .setLogging("iON", Log.DEBUG)
        .asString()
        .setCallback(new FutureCallback<String>() {
          @Override
          public void onCompleted(Exception e, String result) {
            Log.e("", "Result: " + result);
            if (result == null) {
              Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
            } else {
              mPlaces.clear();
              JsonParser parser = new JsonParser();
              JsonArray jArray = (JsonArray) parser.parse(result);
              Gson gson = new GsonBuilder().create();

              for (int i = 0; i < jArray.size(); i++) {
                JsonObject obj = jArray.get(i).getAsJsonObject();
                Place item = gson.fromJson(obj, Place.class);
                mPlaces.add(item);
              }
              mAdapter = new PlaceAdapter(getActivity(), mPlaces);
              // Set the adapter
              ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
              mProgressBar.setVisibility(View.GONE);
            }
          }
        });
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (null != mListener) {
      // Notify the active callbacks interface (the activity, if the
      // fragment is attached to one) that an item has been selected.
      mListener.onPlaceClick(mPlaces.get(position));
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_refresh) {
      if (NetUtil.isOnline(getActivity().getApplicationContext())) {
        mProgressBar.setVisibility(View.VISIBLE);
        getAllTraffic();
      } else {
        setEmptyText(getString(R.string.error_internet));
      }
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * The default content for this Fragment has a TextView that is shown when
   * the list is empty. If you would like to change the text, call this method
   * to supply the text it should use.
   */
  public void setEmptyText(CharSequence emptyText) {
    View emptyView = mListView.getEmptyView();

    if (emptyText instanceof TextView) {
      ((TextView) emptyView).setText(emptyText);
    }
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnPlaceClickListener {
    public void onPlaceClick(Place place);
  }
}
