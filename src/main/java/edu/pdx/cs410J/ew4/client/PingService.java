package edu.pdx.cs410J.ew4.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A GWT remote service that returns a dummy appointment book
 */
@RemoteServiceRelativePath("ping")
public interface PingService extends RemoteService {

  /**
   * Returns the current date and time on the server
   * @param numberOfAppointments
   */
  public AppointmentBook createAppointmentBook(int numberOfAppointments);

}
