package edu.pdx.cs410J.ew4.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;

  @VisibleForTesting
  Button button;
  TextBox textBox;
  private TabPanel mainTabPanel;
  private TextBox ownerNameBox;
  private Button createBookButton;
  private TextArea descriptionArea;
  private TextBox startDateTime;
  private TextBox endDateTime;
  private Button createAppointmentButton;
  private TextBox listingBookOwner;
  private Button listAppointmentsButton;
  private FlexTable appointmentsTable;

  public AppointmentBookGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  @VisibleForTesting
  AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;

    addWidgets();
  }

  private void addWidgets() {
    button = new Button("Create Appointments");
    button.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        createAppointments();
      }
    });


    // Buttons
    createBookButton = new Button("Create Book");
    createBookButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        createAppointmentBook();
      }
    });

    this.textBox = new TextBox();

    // Main Action Tabs
    this.mainTabPanel = new TabPanel();


    // Adding a new Appointment fields
    ownerNameBox = new TextBox();

    descriptionArea = new TextArea();
    descriptionArea.setCharacterWidth(50);
    descriptionArea.setVisibleLines(5);

    startDateTime = new TextBox();
    endDateTime = new TextBox();

    // Adding a new appointment
    createAppointmentButton = new Button("Create Appointment");
    createAppointmentButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        addNewAppointmentToBook();
      }
    });

    // Appointment Listing
    listingBookOwner = new TextBox();
    listAppointmentsButton = new Button("List Appointments");
    listAppointmentsButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        listAppointmentsForOwner(listingBookOwner.getText(), listAppointmentsButton.getTabIndex());
      }
    });


    appointmentsTable = new FlexTable();

  }

  private void addNewAppointmentToBook() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    String owner = this.ownerNameBox.getText();
    String description = this.descriptionArea.getText();
    String beginDateTime = this.startDateTime.getText();
    String endDateTime = this.endDateTime.getText();
    async.addAppointmentToBook(owner, description, beginDateTime, endDateTime, new AsyncCallback<AppointmentBook>() {
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

  private void listAppointmentsForOwner(String text, int tabIndex) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    String bookOwner = text;
    async.getAppointments(bookOwner, new AsyncCallback<AppointmentBook>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(AppointmentBook appointmentBook) {
        if (appointmentBook.getAppointments().size() == 0) {
          Window.alert(appointmentBook.getOwnerName() + " has an empty AppointmentBook, try another name.");
          return;
        }

        appointmentsTable.setText(0, 0, "Description");
        appointmentsTable.setText(0, 1, "Duration");
        appointmentsTable.setText(0, 2, "Start");
        appointmentsTable.setText(0, 3, "End");
        appointmentsTable.setText(0, 4, "Delete");


        // Add appointment to the table
        Collection<Appointment> fromServer = appointmentBook.getAppointments();
        for (Appointment toDisplay : fromServer) {
          int row = appointmentsTable.getRowCount();
          appointmentsTable.setText(row, 0, toDisplay.getDescription());
//          appointmentsTable.setText(row, 0, duration);
          appointmentsTable.setText(row, 2, toDisplay.getBeginTimeString());
          appointmentsTable.setText(row, 3, toDisplay.getEndTimeString());
          Button removeAppointment = new Button("x");
          removeAppointment.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
              int removedRow = appointmentsTable.getRowCount();
              appointmentsTable.removeRow(removedRow - 1);
              //TODO make button remove appointment from database with confirmation?
            }
          });
          appointmentsTable.setWidget(row, 4, removeAppointment);
        }
      }
    });
//    this.alerter.alert("pressed listing appointments button");
  }

  private void createAppointmentBook() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    String bookOwner = getOwnerName();
    async.createAppointmentBook(bookOwner, new AsyncCallback<AppointmentBook>() {
      @Override
      public void onFailure(Throwable ex) {
        alert(ex);
      }

      @Override
      public void onSuccess(AppointmentBook appointmentBook) {
        alerter.alert("Created Appointment Book for " + appointmentBook.getOwnerName());
        mainTabPanel.selectTab(2);
      }
    });
  }

  private String getOwnerName() {
    return this.ownerNameBox.getText();
  }

  private void createAppointments() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    int numberOfAppointments = getNumberOfAppointments();
    async.createAppointmentBook(numberOfAppointments, new AsyncCallback<AppointmentBook>() {

      @Override
      public void onSuccess(AppointmentBook airline) {
        displayInAlertDialog(airline);
      }

      @Override
      public void onFailure(Throwable ex) {
        alert(ex);
      }

    });
  }

  private int getNumberOfAppointments() {
    String number = this.textBox.getText();
    return Integer.parseInt(number);
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

  private void displayInAlertDialog(AppointmentBook airline) {
    StringBuilder sb = new StringBuilder(airline.toString());
    sb.append("\n");

    Collection<Appointment> flights = airline.getAppointments();
    for (Appointment flight : flights) {
      sb.append(flight);
      sb.append("\n");
    }

    alerter.alert(sb.toString());
//    mainTabPanel.selectTab(1);
  }

  private void alert(Throwable ex) {
    alerter.alert(ex.getMessage());
  }

  @Override
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();

    DockPanel makeAppointmentDock = makeNewAppointmentDock();

    DockPanel makeAppointmentListingDock = new DockPanel();
    makeAppointmentListingDock.add(new Label("Owner's Name"), DockPanel.NORTH);
    makeAppointmentListingDock.add(listingBookOwner, DockPanel.CENTER);
    makeAppointmentListingDock.add(listAppointmentsButton, DockPanel.EAST);


    makeAppointmentListingDock.add(appointmentsTable, DockPanel.SOUTH);


    mainTabPanel.add(makeAppointmentDock, "New Appointment");
    mainTabPanel.add(makeAppointmentListingDock, "Appointment Listing");


    rootPanel.add(mainTabPanel);

  }

  private DockPanel makeNewAppointmentDock() {
    // Appointment Owner Dock
    DockPanel makeAppointmentDockOwner = new DockPanel();
    makeAppointmentDockOwner.add(new Label("Appointment Owner: "), DockPanel.NORTH);
    makeAppointmentDockOwner.add(ownerNameBox, DockPanel.SOUTH);

    // Description Dock
    DockPanel makeAppointmentDockDescription = new DockPanel();
    makeAppointmentDockDescription.add(new Label("Description: "), DockPanel.NORTH);
    makeAppointmentDockDescription.add(descriptionArea, DockPanel.SOUTH);

    // Start and End Time Dock
    DockPanel makeAppointmentDockStartAndEnd = new DockPanel();
    makeAppointmentDockStartAndEnd.add(new Label("From:"), DockPanel.NORTH);
    makeAppointmentDockStartAndEnd.add(startDateTime, DockPanel.NORTH);
    makeAppointmentDockStartAndEnd.add(new Label("Until:"), DockPanel.NORTH);
    makeAppointmentDockStartAndEnd.add(endDateTime, DockPanel.SOUTH);

    // Appointment Info Dock
    DockPanel makeAppointmentDock = new DockPanel();
    makeAppointmentDock.add(makeAppointmentDockOwner, DockPanel.NORTH);
    makeAppointmentDock.add(makeAppointmentDockDescription, DockPanel.NORTH);
    makeAppointmentDock.add(makeAppointmentDockStartAndEnd, DockPanel.NORTH);
    makeAppointmentDock.add(createAppointmentButton, DockPanel.EAST);
    return makeAppointmentDock;
  }


  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

}
