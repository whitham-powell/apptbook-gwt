package edu.pdx.cs410J.ew4.client;

import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;

/**
 * The type Appointment book gwt test suite.
 */
public class AppointmentBookGwtTestSuite {
  /**
   * Suite test.
   *
   * @return the test
   */
  public static Test suite() {
    GWTTestSuite suite = new GWTTestSuite("Appointment Book GWT Integration Tests");

    suite.addTestSuite(AppointmentBookGwtIT.class);

    return suite;
  }

}
