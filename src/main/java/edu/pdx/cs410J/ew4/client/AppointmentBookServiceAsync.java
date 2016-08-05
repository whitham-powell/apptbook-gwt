package edu.pdx.cs410J.ew4.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

/**
 * The client-side interface to the createAppointmentBook service
 */
public interface AppointmentBookServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void createAppointmentBook(int numberOfAppointments,
                             AsyncCallback<AppointmentBook> async);

  void createAppointmentBook(String ownerName,
                             AsyncCallback<AppointmentBook> async);

  void addAppointmentToBook(String owner,
                            String description,
                            String beginDateTimeString,
                            String endDateTimeString,
                            AsyncCallback<AppointmentBook> async);

  void getAppointments(String owner, AsyncCallback<AppointmentBook> async);

  void addAppointmentToBook(String owner,
                            String description,
                            Date beginDateTime,
                            Date endDateTime,
                            AsyncCallback<AppointmentBook> async);


  void searchAppointments(String owner,
                          Date searchAfterDateValue,
                          Date searchBeforeDateValue,
                          AsyncCallback<AppointmentBook> async);

}
