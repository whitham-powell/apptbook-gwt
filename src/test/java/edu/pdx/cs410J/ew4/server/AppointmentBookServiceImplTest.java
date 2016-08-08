package edu.pdx.cs410J.ew4.server;

import edu.pdx.cs410J.ew4.client.Appointment;
import edu.pdx.cs410J.ew4.client.AppointmentBook;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * The type Appointment book service impl test.
 */
public class AppointmentBookServiceImplTest {

  /**
   * Service returns expected appointment book.
   */
  @Test
  public void serviceReturnsExpectedAppointmentBook() {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();
    int numberOfAppointments = 6;
    AppointmentBook testBook = service.createAppointmentBook(numberOfAppointments);
    assertThat(testBook.getAppointments().size(), equalTo(numberOfAppointments));
  }

  /**
   * Service creates and stores empty appointment book into hash map.
   */
  @Test
  public void serviceCreatesAndStoresEmptyAppointmentBookIntoHashMap() {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();
    String testOwner = "Test Owner";
    AppointmentBook testBook = service.createAppointmentBook(testOwner);
    assertThat(testBook.getAppointments().size(), equalTo(0));
    assertThat(testBook.getOwnerName(), is(testOwner));
  }

  /**
   * Service adds appointments to appointment book belonging to owner.
   *
   * @throws Exception the exception
   */
  @Test
  public void serviceAddsAppointmentsToAppointmentBookBelongingToOwner() throws Exception {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();
    String testOwner = "Test Owner";
    String testDescription = "Test description";
    String beginDateTimeString = "1/2/2016 12:00 am";
    String endDateTimeString = "1/3/2016 12:00 pm";
    AppointmentBook testBook = service.addAppointmentToBook(testOwner, testDescription, beginDateTimeString, endDateTimeString);
    assertThat(testBook.getAppointments().size(), equalTo(1));
    assertThat(testBook.getOwnerName(), is(testOwner));

    DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    for (Appointment from : testBook.getAppointments()) {
      assertThat(from.getBeginTime().toString(), is(df.parse(beginDateTimeString).toString()));
      assertThat(from.getEndTime().toString(), is(df.parse(endDateTimeString).toString()));
    }

  }

  /**
   * Service can add multiple appointments belonging to owner.
   *
   * @throws Exception the exception
   */
  @Test
  public void serviceCanAddMultipleAppointmentsBelongingToOwner() throws Exception {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();

    String testOwner = "Test Owner";
    String testDescription1 = "Test description 1";
    String beginDateTimeString1 = "1/2/2016 12:00 am";
    String endDateTimeString1 = "1/3/2016 12:00 pm";

    String testDescription2 = "Test description 1";
    String beginDateTimeString2 = "1/4/2016 12:00 am";
    String endDateTimeString2 = "1/5/2016 12:00 pm";

    AppointmentBook testBook;

    testBook = service.addAppointmentToBook(testOwner, testDescription1, beginDateTimeString1, endDateTimeString1);
    assertThat(testBook.getAppointments().size(), equalTo(1));

    testBook = service.addAppointmentToBook(testOwner, testDescription2, beginDateTimeString2, endDateTimeString2);
    assertThat(testBook.getAppointments().size(), equalTo(2));

    assertThat(testBook.getOwnerName(), is(testOwner));

  }

}
