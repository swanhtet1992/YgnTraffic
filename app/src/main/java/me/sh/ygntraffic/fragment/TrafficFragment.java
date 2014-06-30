package me.sh.ygntraffic.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
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
import butterknife.OnClick;
import me.sh.ygntraffic.R;
import me.sh.ygntraffic.adapter.PlaceAdapter;
import me.sh.ygntraffic.base.BaseFragment;
import me.sh.ygntraffic.model.Place;
import me.sh.ygntraffic.util.Constants;
import me.sh.ygntraffic.util.NetUtil;

public class TrafficFragment extends BaseFragment implements AbsListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

  private OnPlaceClickListener mListener;
  private ProgressDialog mDialog;
  private String mPlace;

  /**
   * The fragment's ListView/GridView.
   */
  @InjectView(android.R.id.list)
  AbsListView mListView;
  @InjectView(android.R.id.empty)
  TextView mEmptyView;
  @InjectView(R.id.swipe_refresh)
  SwipeRefreshLayout mSwipeRefreshLayout;

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

    mDialog = new ProgressDialog(getActivity());

    mSwipeRefreshLayout.setOnRefreshListener(this);
    mSwipeRefreshLayout.setColorScheme(R.color.yellow, R.color.white,
        R.color.yellow, R.color.white);

    // Set OnItemClickListener so we can be notified on item clicks
    mListView.setOnItemClickListener(this);
    // Set OnScrollListener to fix ListView scroll up conflict with SwipeRefresh
    mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int topRowVerticalPosition =
            (mListView == null || mListView.getChildCount() == 0) ?
                0 : mListView.getChildAt(0).getTop();
        mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0); // if first row shown != top most position, disable SwipeRefreshLayout
      }
    });
    setHasOptionsMenu(true);

    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    onRefresh();
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
        .setLogging("ION", Log.DEBUG)
        .asString()
        .setCallback(new FutureCallback<String>() {
          @Override
          public void onCompleted(Exception e, String result) {
            mSwipeRefreshLayout.setRefreshing(false);

            if (result == null || e != null) {
              mPlaces.clear();
              setEmptyText(getText(R.string.error_internet));
            } else if (result.equalsIgnoreCase("{\"empty\":true}")) {
              mPlaces.clear();
              setEmptyText(getText(R.string.error_empty));
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
            }
          }
        });
  }

  @Override
  public void onRefresh() {
    if (NetUtil.isOnline(context)) {
      mSwipeRefreshLayout.setRefreshing(true);
      getAllTraffic();
    } else {
      setEmptyText(getString(R.string.error_internet));
    }
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
        onRefresh();
        mEmptyView.setVisibility(View.GONE);
      } else {
        setEmptyText(getString(R.string.error_internet));
      }
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * TODO Need to refactor this Reporting part
   */
  @OnClick(R.id.btn_report)
  public void reportTraffic() {
    mDialog.setMessage("Loading places...");
    mDialog.show();
    Ion.with(getActivity())
        .load(Constants.PLACES)
        .setLogging("ION", Log.DEBUG)
        .asString()
        .setCallback(new FutureCallback<String>() {
          @Override
          public void onCompleted(Exception e, String result) {
            mDialog.dismiss();
            Log.e("", "Result: " + result);
            if (result == null || e != null) {
              setEmptyText(getText(R.string.error_internet));
            } else {
              JsonParser parser = new JsonParser();
              JsonArray array = (JsonArray) parser.parse(result);
              String[] places = new String[array.size()];
              for (int i = 0; i < array.size(); i++) {
                places[i] = array.get(i).getAsString();
              }
              createPlaceDialog(places);
            }
          }
        });
  }

  private void createPlaceDialog(final String[] places) {
    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
    builderSingle.setTitle("Choose place to report");
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
        R.layout.dialog_list_item, places);

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
            mPlace = places[which];
            createStatusDialog(places[which]);
          }
        }
    );
    builderSingle.show();
  }

  private void createStatusDialog(String place) {
    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
    builderSingle.setTitle(place);
    final String[] status = getResources().getStringArray(R.array.report_status);
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
        R.layout.dialog_list_item, status);

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
    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
    builderSingle.setTitle(title);
    String[] remark = getResources().getStringArray(R.array.report_remark);
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
        R.layout.dialog_list_item, remark);

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
          .load(Constants.REPORT + mPlace + "&s=" + status + "&c=" + remark)
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
                    getActivity().finish();
                  }
                });
              } else if (result.equalsIgnoreCase("{err: 3, msg: \"Bad request!\"}")) {
                mDialog.setMessage(getString(R.string.error_reprot));
                mDialog.show();
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                  @Override
                  public void onCancel(DialogInterface dialog) {
                    getActivity().finish();
                  }
                });
              } else {
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(result);
                Toast.makeText(context, "Reported: " + jsonObject.get("remark"), Toast.LENGTH_SHORT).show();
                onRefresh();
              }
            }
          });
    } else {
      Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * The default content for this Fragment has a TextView that is shown when
   * the list is empty. This method is called to change the error text.
   */
  public void setEmptyText(CharSequence emptyText) {
    mEmptyView.setVisibility(View.VISIBLE);
    mEmptyView.setText(emptyText);
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
