package edu.pdx.cs410J.ew4.client;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

public class AppointmentBook extends AbstractAppointmentBook<Appointment>
{

    private Collection<Appointment> appts = new ArrayList<>();

    @Override
    public String getOwnerName()
    {
        return "My Owner";
    }

    @Override
    public Collection<Appointment> getAppointments()
    {
        return this.appts;
    }

    @Override
    public void addAppointment( Appointment appt )
    {
        this.appts.add(appt);
    }
}
