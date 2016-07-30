package edu.pdx.cs410J.ew4.server;

import edu.pdx.cs410J.ew4.client.AppointmentBook;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PingServiceImplTest {

  @Test
  public void pingReturnsExpectedAirline() {
    PingServiceImpl service = new PingServiceImpl();
    int numberOfAppointments = 6;
    AppointmentBook airline = service.createAppointmentBook(numberOfAppointments);
    assertThat(airline.getAppointments().size(), equalTo(numberOfAppointments));
  }
}
