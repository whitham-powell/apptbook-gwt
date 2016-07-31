package edu.pdx.cs410J.ew4.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.ew4.client.Appointment;
import edu.pdx.cs410J.ew4.client.AppointmentBook;
import edu.pdx.cs410J.ew4.client.AppointmentBookService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The server-side implementation of the AppointmentBook Service
 */
public class AppointmentBookServiceImpl extends RemoteServiceServlet implements AppointmentBookService {
  private ConcurrentHashMap<String, AppointmentBook> appointmentBooks = new ConcurrentHashMap<>();


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
  public AppointmentBook createAppointmentBook(String owner) {
    AppointmentBook book = new AppointmentBook(owner);
    this.appointmentBooks.put(owner, book);
    return this.appointmentBooks.get(owner);
  }

  @Override
  public AppointmentBook addAppointmentToBook(String owner,
                                              String description,
                                              String beginDateTimeString,
                                              String endDateTimeString) throws Exception {

    AppointmentBook book = this.appointmentBooks.get(owner);

    if (book == null) {
      book = new AppointmentBook(owner);
    }

    DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    Date beginDateTime;
    Date endDateTime;


    try {
      beginDateTime = df.parse(beginDateTimeString);
      try {
        endDateTime = df.parse(endDateTimeString);
      } catch (ParseException e) {
        throw new Exception("Server received bad formatting of date and/or time: \n " + " endTime: " + endDateTimeString);
      }
    } catch (ParseException e) {
      throw new Exception("Server received bad formatting of date and/or time: \n " +
              "beginTime: " + beginDateTimeString + " ");
    }


    book.addAppointment(new Appointment(description, beginDateTime, endDateTime));

    this.appointmentBooks.put(owner, book);
    return this.appointmentBooks.get(owner);
  }

  @Override
  public AppointmentBook getAppointments(String owner) {
    AppointmentBook book = this.appointmentBooks.get(owner);
    if (book == null) {
      book = new AppointmentBook(owner);
    }

    return book;
  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
