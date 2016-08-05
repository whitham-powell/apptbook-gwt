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
  private final Alerter alerter;

  private TextBox ownerNameBox;
  private TextArea descriptionArea;
  private DateBox createAppointmentStartDateTime = new DateBox();
  private DateBox createAppointmentEndDateTime = new DateBox();
  private TextBox listingBookOwner;
  private Button listAppointmentsButton;
  private FlexTable appointmentsTable = new FlexTable();
  private TextBox searchingBookOwner;
  private DateBox.DefaultFormat dateBoxDateTimeFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy"));
  private DateBox searchAfterDate = new DateBox();
  private DateBox searchBeforeDate = new DateBox();
  private FlexTable searchResultsTable = new FlexTable();

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

    addWidgets();
  }

  private void addWidgets() {

  }

  private void listAppointmentsForOwner(String bookOwner, int tabIndex) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.getAppointments(bookOwner, new AsyncCallback<AppointmentBook>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(AppointmentBook appointmentBook) {
        makeAppointmentTable(appointmentBook);
      }
    });
  }

  private void makeAppointmentTable(AppointmentBook appointmentBook) {
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
    Collection<Appointment> fromServer = appointmentBook.getAppointments();
    for (Appointment toDisplay : fromServer) {
      int row = appointmentsTable.getRowCount();

      appointmentsTable.setText(row, 0, toDisplay.getDescription());
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
          //TODO make button remove appointment from database with confirmation?
        }
      });

      appointmentsTable.setWidget(row, 4, removeAppointment);
    }
  }


  private void displayNewlyAddedAppointment(AppointmentBook book) {
    ArrayList<Appointment> appointmentList = new ArrayList<>();
    appointmentList.addAll(book.getAppointments());

    Appointment newlyAdded = appointmentList.get(book.getAppointments().size() - 1);
    StringBuilder sb = new StringBuilder()
            .append("Added new appointment to ")
            .append(book.getOwnerName())
            .append("'s appointment book")
            .append(newlyAdded.toString());

    this.alerter.alert(sb.toString());
  }


  private void alert(Throwable ex) {
    alerter.alert(ex.getMessage());
  }

  @Override
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();
    DockPanel newAppointmentDock = makeNewAppointmentDock();
    DockPanel appointmentListingDock = makeAppointmentListingDock();
    VerticalPanel searchAppointmentVP = makeSearchAppointmentPanel();

    TabPanel mainTabPanel = new TabPanel();
    mainTabPanel.add(newAppointmentDock, "New Appointment");
    mainTabPanel.add(appointmentListingDock, "Appointment Listing");
    mainTabPanel.add(searchAppointmentVP, "Search Appointments");

    rootPanel.add(mainTabPanel);

  }

  private VerticalPanel makeSearchAppointmentPanel() {

    // Appointment Searching Widgets
    searchingBookOwner = new TextBox();
    Button searchAppointmentsButton = new Button("Search");
    searchAppointmentsButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
        String owner = searchingBookOwner.getText();

        async.searchAppointments(owner, searchAfterDate.getValue(), searchBeforeDate.getValue(), new AsyncCallback<AppointmentBook>() {
          @Override
          public void onFailure(Throwable caught) {
            alert(caught);
          }

          @Override
          public void onSuccess(AppointmentBook appointmentBook) {

            //TODO table Headings wont show up
            searchResultsTable.setText(0, 0, "Description");
            searchResultsTable.setText(0, 1, "Duration");
            searchResultsTable.setText(0, 2, "Start");
            searchResultsTable.setText(0, 3, "End");

            // Add appointments to the table
            Collection<Appointment> fromServer = appointmentBook.appointmentsByRange(searchAfterDate.getValue(), searchBeforeDate.getValue());
            for (Appointment toDisplay : fromServer) {
              int row = appointmentsTable.getRowCount();
              searchResultsTable.setText(row, 0, toDisplay.getDescription());
              searchResultsTable.setText(row, 1, toDisplay.getDuration());
              searchResultsTable.setText(row, 2, toDisplay.getBeginTimeString());
              searchResultsTable.setText(row, 3, toDisplay.getEndTimeString());
            }
          }
        });
      }
    });

    // Begin Main Vertical Panel
    VerticalPanel searchAppointmentVP = new VerticalPanel();
    searchAppointmentVP.add(new Label("Owner's Name: "));
    searchAppointmentVP.add(searchingBookOwner);

    HorizontalPanel startHP = makeDateTimePanel(new Label("After: "), searchAfterDate);
    HorizontalPanel endHP = makeDateTimePanel(new Label("Before:"), searchBeforeDate);

    searchAppointmentVP.add(startHP);
    searchAppointmentVP.add(endHP);
    searchAppointmentVP.add(searchAppointmentsButton);
    searchAppointmentVP.add(searchResultsTable);
    return searchAppointmentVP;
  }

  private HorizontalPanel makeDateTimePanel(Label w, DateBox dateBox) {
    // Start range
    dateBox.setFormat(dateBoxDateTimeFormat);
    dateBox.setValue(new Date());

    HorizontalPanel hp = new HorizontalPanel();
    hp.add(w);
    hp.add(dateBox);

    // Start times list box
    ListBox lbHours = new ListBox();
    lbHours.addItem(String.valueOf(12), String.valueOf(12));
    for (int i = 1; i < 12; i++) {
      String time = NumberFormat.getFormat("00").format(i);
      lbHours.addItem(time, time);
    }
    hp.add(lbHours);
    hp.add(new Label(":"));

    ListBox lbMinutes = new ListBox();
    for (int i = 0; i < 60; i = i + 15) {
      String time = NumberFormat.getFormat("00").format(i);
      lbMinutes.addItem(time, time);
    }
    hp.add(lbMinutes);

    // Start AMPM list box
    ListBox ampm = new ListBox();
    ampm.addItem("AM", "AM");
    ampm.addItem("PM", "PM");
    hp.add(ampm);
    return hp;
  }

  private DockPanel makeAppointmentListingDock() {
    DockPanel makeAppointmentListingDock = new DockPanel();
    makeAppointmentListingDock.add(new Label("Owner's Name"), DockPanel.NORTH);

    // Appointment Listing
    listingBookOwner = new TextBox();
    listAppointmentsButton = new Button("List Appointments");
    listAppointmentsButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        listAppointmentsForOwner(listingBookOwner.getText(), listAppointmentsButton.getTabIndex());
      }
    });

    makeAppointmentListingDock.add(listingBookOwner, DockPanel.CENTER);
    makeAppointmentListingDock.add(listAppointmentsButton, DockPanel.EAST);
    makeAppointmentListingDock.add(appointmentsTable, DockPanel.SOUTH);
    return makeAppointmentListingDock;
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
    HorizontalPanel startHP = makeDateTimePanel(new Label("Starts:"), createAppointmentStartDateTime);
    HorizontalPanel endHP = makeDateTimePanel(new Label("Ends:"), createAppointmentEndDateTime);

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
    async.addAppointmentToBook(
            owner,
            description,
            createAppointmentStartDateTime.getValue(),
            createAppointmentEndDateTime.getValue(),
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
