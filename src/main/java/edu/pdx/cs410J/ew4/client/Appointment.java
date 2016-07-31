package edu.pdx.cs410J.ew4.client;

import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.pdx.cs410J.AbstractAppointment;

import java.util.Date;

public class Appointment extends AbstractAppointment {
  private String description = "< empty description >";
  private Date beginTime;
  private Date endTime;


  public Appointment() {

  }

  public Appointment(String description, Date beginDateTime, Date endDateTime) {
    this.description = description;
    this.beginTime = beginDateTime;
    this.endTime = endDateTime;
  }

  //TODO use DateTimeFormat.getFormat("yyyy/MM/dd hh:mm a"); to format date
  private String formatDate(Date date) {
    String pattern = "yyyy/MM/dd hh:mm a";
    return DateTimeFormat.getFormat(pattern).format(date);
  }

  @Override
  public String getBeginTimeString() {
    return "START " + formatDate(this.beginTime);
  }

  @Override
  public String getEndTimeString() {
    return "END " + formatDate(this.getEndTime());
  }

  @Override
  public Date getEndTime() {
    return this.endTime;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public Date getBeginTime() {
    return this.beginTime;
  }
}
