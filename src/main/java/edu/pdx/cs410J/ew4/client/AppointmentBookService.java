package edu.pdx.cs410J.ew4.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A GWT remote service that returns a dummy appointment book
 */
@RemoteServiceRelativePath("appointments")
public interface AppointmentBookService extends RemoteService {

  /**
   * Returns the current date and time on the server
   * @param numberOfAppointments number of appointments to create.
   */
  public AppointmentBook createAppointmentBook(int numberOfAppointments);

  public AppointmentBook createAppointmentBook(String ownerName);

  public AppointmentBook addAppointmentToBook(String ownerName,
                                              String description,
                                              String beginDateTimeString,
                                              String endDateTimeString) throws Exception;

  public AppointmentBook getAppointments(String owner);

}
