package edu.pdx.cs410J.ew4.client;

import edu.pdx.cs410J.AbstractAppointment;

import java.util.Date;

public class Appointment extends AbstractAppointment {
  //TODO use DateTimeFormat.getFormat("yyyy/MM/dd hh:mm a"); to format date

  @Override
  public String getBeginTimeString() {
    return "START " + getBeginTime();
  }

  @Override
  public String getEndTimeString() {
    return "END + " + getEndTime();
  }

  @Override
  public Date getEndTime() {
    return new Date();
  }

  @Override
  public String getDescription() {
    return "My description";
  }

  @Override
  public Date getBeginTime() {
    return new Date();
  }
}
