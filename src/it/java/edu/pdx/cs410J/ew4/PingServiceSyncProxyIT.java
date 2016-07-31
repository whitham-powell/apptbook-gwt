package edu.pdx.cs410J.ew4;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import edu.pdx.cs410J.ew4.client.AppointmentBook;
import edu.pdx.cs410J.ew4.client.AppointmentBookService;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class PingServiceSyncProxyIT extends HttpRequestHelper {

  private final int httpPort = Integer.getInteger("http.port", 8888);
  private String webAppUrl = "http://localhost:" + httpPort /*+ "/apptbook"*/;

  @Test
  public void gwtWebApplicationIsRunning() throws IOException {
    Response response = get(this.webAppUrl);
    assertEquals(200, response.getCode());
  }

  @Test
  public void canInvokePingServiceWithGwtSyncProxy() {
    String moduleName = "apptbook";
    SyncProxy.setBaseURL(this.webAppUrl + "/" + moduleName + "/");

    AppointmentBookService service = SyncProxy.createSync(AppointmentBookService.class);
    int numberOfAppointments = 5;
    AppointmentBook apptbook = service.createAppointmentBook(numberOfAppointments);
    assertEquals("< default owner >", apptbook.getOwnerName());
    assertEquals(numberOfAppointments, apptbook.getAppointments().size());
  }

}
