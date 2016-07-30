package edu.pdx.cs410J.ew4.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.ew4.client.Appointment;
import edu.pdx.cs410J.ew4.client.AppointmentBook;
import edu.pdx.cs410J.ew4.client.AppointmentBookService;

/**
 * The server-side implementation of the division service
 */
public class AppointmentBookServiceImpl extends RemoteServiceServlet implements AppointmentBookService
{
  // same kind of code as a in project 4 servlet
  @Override
  public AppointmentBook createAppointmentBook(int numberOfAppointments) {
    AppointmentBook book = new AppointmentBook();
    for (int i = 0; i < numberOfAppointments; i++) {
      book.addAppointment(new Appointment());
    }
    return book;
  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}