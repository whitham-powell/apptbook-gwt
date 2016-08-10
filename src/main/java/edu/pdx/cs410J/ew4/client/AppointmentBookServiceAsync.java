package edu.pdx.cs410J.ew4.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

/**
 * The client-side interface to the createAppointmentBook service
 */
@SuppressWarnings("WeakerAccess")
public interface AppointmentBookServiceAsync {

  /**
   * Return the current date/time on the server
   *
   * @param numberOfAppointments the number of appointments
   * @param async                the async
   */
  void createAppointmentBook(int numberOfAppointments,
                             AsyncCallback<AppointmentBook> async);

  /**
   * Create appointment book.
   *
   * @param ownerName the owner name
   * @param async     the async
   */
  void createAppointmentBook(String ownerName,
                             AsyncCallback<AppointmentBook> async);

  /**
   * Add appointment to book.
   *
   * @param owner               the owner
   * @param description         the description
   * @param beginDateTimeString the begin date time string
   * @param endDateTimeString   the end date time string
   * @param async               the async
   */
  void addAppointmentToBook(String owner,
                            String description,
                            String beginDateTimeString,
                            String endDateTimeString,
                            AsyncCallback<AppointmentBook> async);

  /**
   * Gets appointments.
   *
   * @param owner the owner
   * @param async the async
   */
  void getAppointments(String owner, AsyncCallback<AppointmentBook> async);

  /**
   * Add appointment to book.
   *
   * @param owner         the owner
   * @param description   the description
   * @param beginDateTime the begin date time
   * @param endDateTime   the end date time
   * @param async         the async
   */
  void addAppointmentToBook(String owner,
                            String description,
                            Date beginDateTime,
                            Date endDateTime,
                            AsyncCallback<AppointmentBook> async);


  /**
   * Search appointments.
   *
   * @param owner                 the owner
   * @param searchAfterDateValue  the search after date value
   * @param searchBeforeDateValue the search before date value
   * @param async                 the async
   */
  void searchAppointments(String owner,
                          Date searchAfterDateValue,
                          Date searchBeforeDateValue,
                          AsyncCallback<AppointmentBook> async);

}
