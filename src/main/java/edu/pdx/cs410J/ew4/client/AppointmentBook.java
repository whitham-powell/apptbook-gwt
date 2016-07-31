package edu.pdx.cs410J.ew4.client;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

public class AppointmentBook extends AbstractAppointmentBook<Appointment> {

  private Collection<Appointment> appointments = new ArrayList<>();
  private String ownerName = "< default owner >";

  public AppointmentBook() {
  }

  public AppointmentBook(String ownerName) {
    this.ownerName = ownerName;
  }

  @Override
  public String getOwnerName() {
    return this.ownerName;
  }

  @Override
  public Collection<Appointment> getAppointments() {
    return this.appointments;
  }

  @Override
  public void addAppointment(Appointment appointment) {
    this.appointments.add(appointment);
  }
}
