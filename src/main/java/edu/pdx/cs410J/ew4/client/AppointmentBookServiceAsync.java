package edu.pdx.cs410J.ew4.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client-side interface to the createAppointmentBook service
 */
public interface AppointmentBookServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void createAppointmentBook(int numberOfAppointments, AsyncCallback<AppointmentBook> async);
}
