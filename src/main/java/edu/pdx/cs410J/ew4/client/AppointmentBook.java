package edu.pdx.cs410J.ew4.client;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.*;

/**
 * The type Appointment book.
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {

  private Collection<Appointment> appointments = new ArrayList<>();
  private String ownerName = "< default owner >";

  /**
   * Instantiates a new Appointment book.
   */
  public AppointmentBook() {

  }

  /**
   * Creates an instance of an <code>AppointmentBook</code> stores a <code>Collection</code> of {@link Appointment} objects
   *
   * @param ownerName The name of the owner of <code>AppointmentBook</code>. Of type <code>String</code>
   */
  public AppointmentBook(String ownerName) {
    this.ownerName = ownerName;
  }

  /**
   * Returns the name of the owner of this appointment book.
   */
  @Override
  public String getOwnerName() {
    return this.ownerName;
  }

  /**
   * Returns all of the appointments in this appointment book as a
   * collection of {@link Appointment}s.
   */
  @Override
  public Collection<Appointment> getAppointments() {
    return this.appointments;
  }

  /**
   * Adds an appointment to this appointment book
   *
   * @param appointment the appointment to add to the book.
   */
  @Override
  public void addAppointment(Appointment appointment) {
    this.appointments.add(appointment);
  }

  /**
   * Gets sorted set.
   *
   * @return the sorted set
   */
  public SortedSet<Appointment> getSortedSet() {
    SortedSet<Appointment> sortedAppointments = new TreeSet<>(new Comparator<Appointment>() {
      @Override
      public int compare(Appointment appointment, Appointment that) {
        return appointment.compareTo(that);
      }
    });
    for (Appointment app : this.appointments) {
      sortedAppointments.add(app);
    }
    return sortedAppointments;
  }


  /**
   * Size int.
   *
   * @return the int
   */
  public int size() {
    return appointments.size();
  }

  /**
   * By range sorted set.
   *
   * @param beginTimeRange the begin time range
   * @param endTimeRange   the end time range
   * @return the sorted set
   */
  public SortedSet<Appointment> appointmentsByRange(Date beginTimeRange, Date endTimeRange) {
    SortedSet<Appointment> s = new TreeSet<>(new Comparator<Appointment>() {
      @Override
      public int compare(Appointment appointment, Appointment that) {
        return appointment.compareTo(that);
      }
    });
    for (Appointment app : getSortedSet()) {
      if (app.getBeginTime().after(beginTimeRange) && app.getBeginTime().before(endTimeRange)
              || app.getBeginTime().equals(beginTimeRange)) {
        s.add(app);
      }
    }
    return s;
  }

}