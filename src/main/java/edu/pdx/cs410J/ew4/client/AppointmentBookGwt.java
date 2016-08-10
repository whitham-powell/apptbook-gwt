package edu.pdx.cs410J.ew4.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private static final String USAGE_TEXT_HTML = "<h1>Usage</h1>\n" +
          "<ul>\n" +
          "<li>The <em><strong>'New Appointment'</strong></em> tab is used to create a new appointment and add that appointment to the supplied owners book. If the owner does not already have an appointment book a new one is created and that appointment is then added.</li>\n" +
          "<li>The <em><strong>'</strong><strong>Appointment Listing'</strong></em> tab is used to list all of the appointments in a supplied owners appointment book. A dialog box pops up if the owner is not found on the server. Otherwise, a table of appointments is generated and dispalyed. If there is an appointment you do not wish to view, there is a button with labeled an <strong>'X'</strong> to remove it from the table (note: this does not remove it from the server).</li>\n" +
          "<li>The <em><strong>'Search Appointments'</strong></em> tab is used to find appointments within a range of dates and times. If the owner's book is on the server it will display appointments whose begin times are within the supplied range of dates/times. This will <strong>NOT</strong> exclude appointments that end after the range.</li>\n" +
          "</ul>";
  private static final String README_TEXT_HTML =
          "<h1 style=\"text-align: center;\">CS410J Project 5</h1>\n" +
                  "<h2 style=\"text-align: center;\">Creating a Rich Internet Application using GWT</h2>\n" +
                  "<h3>Author:</h3>\n" +
                  "<p>Elijah Whitham-Powell</p>\n" +
                  "<h3>Date Completed:</h3>\n" +
                  "<p>August 10th, 2016</p>\n" +
                  "<h3>Description:</h3>\n" +
                  "<p>This project is a RIA using GWT that creates an Appointment and adds it to an AppointmentBook based on the information " +
                  "provided via the application. The user can create an appointment book upon creating a new appointment." +
                  " Once the appointment book is created, the user can list all of the appointments for a supplied owner. " +
                  "Also, a user can list only appointments in which start between two dates and times. " +
                  "All of the appointment books are stored on the server and are stored by owner name.</p>";
  private final Alerter alerter;
  private final int descriptionWidth = 350;
  private final ListBox searchAfterHour = new ListBox();
  private final ListBox searchAfterMinute = new ListBox();
  private final ListBox searchAfterAMPM = new ListBox();
  private final ListBox searchBeforeHour = new ListBox();
  private final ListBox searchBeforeMinute = new ListBox();
  private final ListBox searchBeforeAMPM = new ListBox();
  private TextBox ownerNameBox;
  private TextArea descriptionArea;
  private DateBox createAppointmentStartDateTime = new DateBox();
  private DateBox createAppointmentEndDateTime = new DateBox();
  private TextBox listingBookOwner;
  private FlexTable appointmentsTable = new FlexTable();
  private TextBox searchingBookOwner;
  private DateBox.DefaultFormat dateBoxDateTimeFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy"));
  private DateBox searchAfterDate = new DateBox();
  private DateBox searchBeforeDate = new DateBox();
  private FlexTable searchResultsTable = new FlexTable();
  private ListBox createAppointmentStartHour = new ListBox();
  private ListBox createAppointmentStartMinute = new ListBox();
  private ListBox createAppointmentStartAMPM = new ListBox();
  private ListBox createAppointmentEndHour = new ListBox();
  private ListBox createAppointmentEndMinute = new ListBox();
  private ListBox createAppointmentEndAMPM = new ListBox();


  /**
   * Instantiates a new Appointment book gwt.
   */
  public AppointmentBookGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  /**
   * Instantiates a new Appointment book gwt.
   *
   * @param alerter the alerter
   */
  @VisibleForTesting
  AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;
  }

  @Override
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();
    DockPanel newAppointmentDock = makeNewAppointmentDock();
    VerticalPanel appointmentListingDock = makeAppointmentListingDock();
    VerticalPanel searchAppointmentVP = makeSearchAppointmentPanel();

    HTMLPanel readmeHTML = new HTMLPanel(README_TEXT_HTML);

    HTMLPanel usageHTML = new HTMLPanel(USAGE_TEXT_HTML);

    TabPanel mainTabPanel = new TabPanel();
    mainTabPanel.add(readmeHTML, "Readme");
    mainTabPanel.add(newAppointmentDock, "New Appointment");
    mainTabPanel.add(appointmentListingDock, "Appointment Listing");
    mainTabPanel.add(searchAppointmentVP, "Search Appointments");
    mainTabPanel.add(usageHTML, "Help");
    mainTabPanel.selectTab(0);
    rootPanel.add(mainTabPanel);
  }

  private DockPanel makeNewAppointmentDock() {

    // Instantiate fields for new Appointment Dock
    ownerNameBox = new TextBox();

    descriptionArea = new TextArea();
    descriptionArea.setCharacterWidth(50);
    descriptionArea.setVisibleLines(5);

    // Appointment Owner Dock
    DockPanel makeAppointmentDockOwner = new DockPanel();
    makeAppointmentDockOwner.add(new Label("Appointment Owner: "), DockPanel.NORTH);
    makeAppointmentDockOwner.add(ownerNameBox, DockPanel.SOUTH);

    // Description Dock
    DockPanel makeAppointmentDockDescription = new DockPanel();
    makeAppointmentDockDescription.add(new Label("Description: "), DockPanel.NORTH);
    makeAppointmentDockDescription.add(descriptionArea, DockPanel.SOUTH);

    // Start and End Time Dock
    HorizontalPanel startHP = makeDateTimePanel(new Label("Starts:"),
            createAppointmentStartDateTime,
            createAppointmentStartHour,
            createAppointmentStartMinute,
            createAppointmentStartAMPM);
    HorizontalPanel endHP = makeDateTimePanel(new Label("Ends:"),
            createAppointmentEndDateTime,
            createAppointmentEndHour,
            createAppointmentEndMinute,
            createAppointmentEndAMPM);

    // Create Appointment Button
    Button createAppointmentButton = new Button("Create Appointment");
    createAppointmentButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        createAnAppointment();
      }
    });

    // Appointment Info Dock
    DockPanel makeAppointmentDock = new DockPanel();
    makeAppointmentDock.add(makeAppointmentDockOwner, DockPanel.NORTH);
    makeAppointmentDock.add(makeAppointmentDockDescription, DockPanel.NORTH);
    makeAppointmentDock.add(startHP, DockPanel.NORTH);
    makeAppointmentDock.add(endHP, DockPanel.NORTH);

    makeAppointmentDock.add(createAppointmentButton, DockPanel.EAST);
    return makeAppointmentDock;
  }

  private void createAnAppointment() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    String owner = ownerNameBox.getText();
    String description = descriptionArea.getText();
    Date beginTime = createAppointmentStartDateTime.getValue();
    Date endTime = createAppointmentEndDateTime.getValue();

    String pattern = "MM/dd/yyyy";

    String beginBuilder = DateTimeFormat.getFormat(pattern).format(beginTime) +
            createAppointmentStartHour.getSelectedValue() +
            createAppointmentStartMinute.getSelectedValue() +
            createAppointmentStartAMPM.getSelectedValue();
    beginTime = DateTimeFormat.getFormat("MM/dd/yyyyhhmma").parseStrict(beginBuilder);

    String endBuilder = DateTimeFormat.getFormat(pattern).format(endTime) +
            createAppointmentEndHour.getSelectedValue() +
            createAppointmentEndMinute.getSelectedValue() +
            createAppointmentEndAMPM.getSelectedValue();
    endTime = DateTimeFormat.getFormat("MM/dd/yyyyhhmma").parseStrict(endBuilder);

    async.addAppointmentToBook(
            owner,
            description,
            beginTime,
            endTime,
            new AsyncCallback<AppointmentBook>() {
              @Override
              public void onFailure(Throwable caught) {
                alert(caught);
              }

              @Override
              public void onSuccess(AppointmentBook book) {
                displayNewlyAddedAppointment(book);
              }
            });
  }

  private void displayNewlyAddedAppointment(AppointmentBook book) {
    ArrayList<Appointment> appointmentList = new ArrayList<>();
    appointmentList.addAll(book.getAppointments());

    Appointment newlyAdded = appointmentList.get(book.getAppointments().size() - 1);
    String alertMsg = "Added new appointment to " +
            book.getOwnerName() +
            "'s appointment book" +
            newlyAdded.toString();

    this.alerter.alert(alertMsg);
  }

  private VerticalPanel makeAppointmentListingDock() {
    VerticalPanel makeAppointmentListingDock = new VerticalPanel();
    makeAppointmentListingDock.add(new Label("Owner's Name"));

    // Appointment Listing
    listingBookOwner = new TextBox();
    Button listAppointmentsButton = new Button("List Appointments");
    listAppointmentsButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (appointmentsTable.getRowCount() > 0) {
          appointmentsTable.removeAllRows();
        }
        listAppointmentsForOwner(listingBookOwner.getText());
      }
    });

    makeAppointmentListingDock.add(listingBookOwner);
    makeAppointmentListingDock.add(listAppointmentsButton);
    makeAppointmentListingDock.add(appointmentsTable);
    return makeAppointmentListingDock;
  }

  private void listAppointmentsForOwner(String bookOwner) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.getAppointments(bookOwner, new AsyncCallback<AppointmentBook>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(AppointmentBook appointmentBook) {
        makeAppointmentListingTable(appointmentBook);
      }
    });
  }

  private void makeAppointmentListingTable(AppointmentBook appointmentBook) {
    if (appointmentBook.getAppointments().size() == 0) {
      Window.alert(appointmentBook.getOwnerName() + " has an empty AppointmentBook, try another name.");
      return;
    }

    appointmentsTable.setText(0, 0, "Description");
    appointmentsTable.setText(0, 1, "Duration");
    appointmentsTable.setText(0, 2, "Start");
    appointmentsTable.setText(0, 3, "End");
    appointmentsTable.setText(0, 4, "Delete");

    // Add appointments to the table
    Collection<Appointment> fromServer = appointmentBook.getSortedSet();
    for (final Appointment toDisplay : fromServer) {
      int row = appointmentsTable.getRowCount();

      appointmentsTable.setText(row, 0, toDisplay.getDescription());

      appointmentsTable.getCellFormatter().setWidth(row, 0, String.valueOf(descriptionWidth) + "px");
      appointmentsTable.getCellFormatter().setWordWrap(row, 0, true);

      appointmentsTable.setText(row, 1, toDisplay.getDuration());
      appointmentsTable.setText(row, 2, toDisplay.getBeginTimeString());
      appointmentsTable.setText(row, 3, toDisplay.getEndTimeString());

      Button removeAppointment = new Button("x");
      removeAppointment.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {

          int rowIndex = appointmentsTable.getCellForEvent(event).getRowIndex();
          appointmentsTable.removeRow(rowIndex);

          if (appointmentsTable.getRowCount() == 1) {
            appointmentsTable.removeRow(0);
          }
        }
      });
      appointmentsTable.setWidget(row, 4, removeAppointment);
    }
  }

  private void alert(Throwable ex) {
    alerter.alert(ex.getMessage());
  }

  private VerticalPanel makeSearchAppointmentPanel() {

    // Appointment Searching Widgets
    searchingBookOwner = new TextBox();
    Button searchAppointmentsButton = new Button("Search");
    searchAppointmentsButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (searchResultsTable.getRowCount() > 0) {
          searchResultsTable.removeAllRows();
        }
        displayRangeOfAppointmentsTable();
      }
    });

    // Begin Main Vertical Panel
    VerticalPanel searchAppointmentVP = new VerticalPanel();
    searchAppointmentVP.add(new Label("Owner's Name: "));
    searchAppointmentVP.add(searchingBookOwner);

    HorizontalPanel startHP = makeDateTimePanel(new Label("After:"), searchAfterDate, searchAfterHour, searchAfterMinute, searchAfterAMPM);
    HorizontalPanel endHP = makeDateTimePanel(new Label("Before:"), searchBeforeDate, searchBeforeHour, searchBeforeMinute, searchBeforeAMPM);

    searchAppointmentVP.add(startHP);
    searchAppointmentVP.add(endHP);
    searchAppointmentVP.add(searchAppointmentsButton);
    searchAppointmentVP.add(searchResultsTable);
    return searchAppointmentVP;
  }

  private void displayRangeOfAppointmentsTable() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    String owner = searchingBookOwner.getText();
    async.searchAppointments(owner, searchAfterDate.getValue(), searchBeforeDate.getValue(), new AsyncCallback<AppointmentBook>() {
      @Override
      public void onFailure(Throwable caught) {
        alert(caught);
      }

      @Override
      public void onSuccess(AppointmentBook appointmentBook) {
        makeAppointmentResultsTable(appointmentBook);
      }
    });
  }

  private void makeAppointmentResultsTable(AppointmentBook appointmentBook) {
    // Make table column headings
    searchResultsTable.setText(0, 0, "Description");
    searchResultsTable.setText(0, 1, "Duration");
    searchResultsTable.setText(0, 2, "Start");
    searchResultsTable.setText(0, 3, "End");

    // Add appointments to the table
    Date afterThisDate = searchAfterDate.getValue();
    Date beforeThisDate = searchBeforeDate.getValue();

    String pattern = "MM/dd/yyyy";

    String beginBuilder = DateTimeFormat.getFormat(pattern).format(afterThisDate) +
            searchAfterHour.getSelectedValue() +
            searchAfterMinute.getSelectedValue() +
            searchAfterAMPM.getSelectedValue();
    afterThisDate = DateTimeFormat.getFormat("MM/dd/yyyyhhmma").parseStrict(beginBuilder);

    String endBuilder = DateTimeFormat.getFormat(pattern).format(beforeThisDate) +
            searchBeforeHour.getSelectedValue() +
            searchBeforeMinute.getSelectedValue() +
            searchBeforeAMPM.getSelectedValue();
    beforeThisDate = DateTimeFormat.getFormat("MM/dd/yyyyhhmma").parseStrict(endBuilder);

    Collection<Appointment> fromServer = appointmentBook.appointmentsByRange(afterThisDate, beforeThisDate);
    for (Appointment toDisplay : fromServer) {
      int row = searchResultsTable.getRowCount();
      searchResultsTable.getCellFormatter().setWidth(row, 0, String.valueOf(descriptionWidth) + "px");
      searchResultsTable.getCellFormatter().setWordWrap(row, 0, true);
      searchResultsTable.setText(row, 0, toDisplay.getDescription());
      searchResultsTable.setText(row, 1, toDisplay.getDuration());
      searchResultsTable.setText(row, 2, toDisplay.getBeginTimeString());
      searchResultsTable.setText(row, 3, toDisplay.getEndTimeString());
    }
  }

  private HorizontalPanel makeDateTimePanel(Label w, DateBox dateBox, ListBox hours, ListBox minutes, ListBox ampm) {
    // Start range
    dateBox.setFormat(dateBoxDateTimeFormat);
    dateBox.setValue(new Date());

    HorizontalPanel hp = new HorizontalPanel();
    hp.add(w);
    hp.add(dateBox);

    // Start times list box
    hours.addItem(String.valueOf(12), String.valueOf(12));
    for (int i = 1; i < 12; i++) {
      String time = NumberFormat.getFormat("00").format(i);
      hours.addItem(time, time);
    }
    hp.add(hours);
    hp.add(new Label(":"));

    for (int i = 0; i < 60; i = i + 15) {
      String time = NumberFormat.getFormat("00").format(i);
      minutes.addItem(time, time);
    }
    hp.add(minutes);

    // Start AMPM list box
    ampm.addItem("AM", "AM");
    ampm.addItem("PM", "PM");
    hp.add(ampm);
    return hp;
  }

  /**
   * The interface Alerter.
   */
  @VisibleForTesting
  interface Alerter {
    /**
     * Alert.
     *
     * @param message the message
     */
    void alert(String message);
  }

}
