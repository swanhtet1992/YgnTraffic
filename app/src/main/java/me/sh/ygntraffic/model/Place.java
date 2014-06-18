package me.sh.ygntraffic.model;

import android.content.Context;

import java.io.Serializable;

import me.sh.ygntraffic.R;

/**
 * Created by SH on 16/Jun/2014.
 */
public class Place implements Serializable {
  String name;
  String status;
  String created_date;
  String remark_id;
  String flag;
  String reported_time;
  String recent;
  String remark;
  int status_color;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStatus() {
    if (status.equalsIgnoreCase("0")) {
      return "No Report";
    } else if (status.equalsIgnoreCase("1")) {
      return "Normal";
    } else if (status.equalsIgnoreCase("2")) {
      return "Bad Traffic";
    } else if (status.equalsIgnoreCase("3")) {
      return "Worst Traffic";
    } else {
      return "Unknown";
    }
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCreatedDate() {
    return created_date;
  }

  public void setCreatedDate(String created_date) {
    this.created_date = created_date;
  }

  public String getRemarkId() {
    return remark_id;
  }

  public void setRemarkId(String remark_id) {
    this.remark_id = remark_id;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public String getReportedTime() {
    return "Reported at " + reported_time;
  }

  public void setReportedTime(String reported_time) {
    this.reported_time = reported_time;
  }

  public String getRecent() {
    return recent;
  }

  public void setRecent(String recent) {
    this.recent = recent;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public int getStatusColor(Context context, String status) {
    if (status.equalsIgnoreCase("No Report")) {
      return context.getResources().getColor(R.color.grey);
    } else if (status.equalsIgnoreCase("Normal")) {
      return context.getResources().getColor(R.color.green);
    } else if (status.equalsIgnoreCase("Bad Traffic")) {
      return context.getResources().getColor(R.color.yellow);
    } else if (status.equalsIgnoreCase("Worst Traffic")) {
      return context.getResources().getColor(R.color.red);
    } else {
      return context.getResources().getColor(R.color.grey);
    }
  }
}
