package com.example.airport_management;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


//cd /Users/msajjad/IdeaProjects/airport_management/src/main/java/com/example/airport_management/
public class database {
    public static class Plane {
        private SimpleIntegerProperty PlaneID;
        private SimpleStringProperty Manufacturer;
        private SimpleStringProperty Model;
        private SimpleIntegerProperty FlightAttendants;
        private SimpleIntegerProperty Passengers;
        private SimpleStringProperty PlaneLayout;

        public void setPlaneID(int planeID) {this.PlaneID.set(planeID);}
        public void setManufacturer(String manufacturer) {this.Manufacturer.set(manufacturer);}
        public void setModel(String model) {this.Model.set(model);}
        public void setFlightAttendants(int flightAttendants) {this.FlightAttendants.set(flightAttendants);}
        public void setPlaneLayout(String planeLayout) {this.PlaneLayout.set(planeLayout);}
        public void setPassengers(int passengers) {this.Passengers.set(passengers);}

        Plane(SimpleIntegerProperty planeID,
                      SimpleStringProperty manufacturer, SimpleStringProperty model, SimpleIntegerProperty flightAttendants,
                      SimpleIntegerProperty passengers, SimpleStringProperty planeLayout) {

            PlaneID = planeID;
            Manufacturer = manufacturer;
            Model = model;
            FlightAttendants = flightAttendants;
            Passengers = passengers;
            PlaneLayout = planeLayout;
        }

        public int getPlaneID() {return PlaneID.get();}
        public SimpleIntegerProperty planeIDProperty() {return PlaneID;}
        public String getManufacturer() {return Manufacturer.get();}
        public SimpleStringProperty manufacturerProperty() {return Manufacturer;}
        public String getModel() {return Model.get();}
        public SimpleStringProperty modelProperty() {return Model;}
        public int getFlightAttendants() {return FlightAttendants.get();}
        public SimpleIntegerProperty flightAttendantsProperty() {return FlightAttendants;}
        public int getPassengers() {return Passengers.get();}
        public SimpleIntegerProperty passengersProperty() {return Passengers;}
        public String getPlaneLayout() {return PlaneLayout.get();}
        public SimpleStringProperty planeLayoutProperty() {return PlaneLayout;}
    }
    public static class Flight {
        private SimpleStringProperty FlightNumber;
        private SimpleIntegerProperty AirlineID;
        private SimpleStringProperty Origin;
        private SimpleStringProperty Destination;
        private SimpleIntegerProperty Duration;

        public void setAirlineID(int airlineID) {
            this.AirlineID.set(airlineID);
        }
        public void setFlightNumber(String FlightNumber) {
            this.FlightNumber.set(FlightNumber);
        }
        public void setDestination(String destination) {
            this.Destination.set(destination);
        }

        public void setOrigin(String origin) {
            this.Origin.set(origin);
        }

        public void setDuration(int duration) {
            this.Duration.set(duration);
        }

        Flight(SimpleStringProperty FlightNumber,
               SimpleIntegerProperty airlineID,
               SimpleStringProperty origin,
               SimpleStringProperty destination,
               SimpleIntegerProperty duration) {
            this.FlightNumber = FlightNumber;
            this.AirlineID = airlineID;
            this.Origin = origin;
            this.Destination = destination;
            this.Duration = duration;
        }

        public String getFlightNumber() {return FlightNumber.get();}
        public SimpleStringProperty flightNumberProperty() {return FlightNumber;}

        public int getAirlineID() {return AirlineID.get();}
        public SimpleIntegerProperty airlineIDProperty() {return AirlineID;}

        public String getOrigin() {return Origin.get();}
        public SimpleStringProperty originProperty() {return Origin;}

        public String getDestination() {return Destination.get();}
        public SimpleStringProperty destinationProperty() {return Destination;}

        public int getDuration() {return Duration.get();}
        public SimpleIntegerProperty durationProperty() {return Duration;}
    }
    public static class Journey {
        private SimpleIntegerProperty JourneyID;
        private SimpleStringProperty DepartureDateTime;
        private SimpleStringProperty DelayedDateTime;
        private SimpleStringProperty DepartureGate;
        private SimpleIntegerProperty PlaneID;
        private SimpleStringProperty FlightNumber;

        public void setJourneyID(int journeyID) {this.JourneyID.set(journeyID);}
        public void setFlightNumber(String flightNumber) {this.FlightNumber.set(flightNumber);}
        public void setPlaneID(int planeID) {this.PlaneID.set(planeID);}
        public void setDepartureGate(String departureGate) {this.DepartureGate.set(departureGate);}
        public void setDelayedDateTime(String delayedDateTime) {this.DelayedDateTime.set(delayedDateTime);}
        public void setDepartureDateTime(String departureDateTime) {this.DepartureDateTime.set(departureDateTime);}

        Journey(SimpleIntegerProperty journeyID, SimpleStringProperty departureDateTime, SimpleStringProperty delayedDateTime, SimpleStringProperty departureGate, SimpleIntegerProperty planeID, SimpleStringProperty flightNumber) {
            JourneyID = journeyID;
            DepartureDateTime = departureDateTime;
            DelayedDateTime = delayedDateTime;
            DepartureGate = departureGate;
            PlaneID = planeID;
            FlightNumber = flightNumber;
        }


        public int getJourneyID() {return JourneyID.get();}
        public SimpleIntegerProperty journeyIDProperty() {return JourneyID;}

        public String getDepartureDateTime() {return DepartureDateTime.get();}
        public SimpleStringProperty departureDateTimeProperty() {return DepartureDateTime;}

        public String getDelayedDateTime() {return DelayedDateTime.get();}
        public SimpleStringProperty delayedDateTimeProperty() {return DelayedDateTime;}

        public int getPlaneID() {return PlaneID.get();}
        public SimpleIntegerProperty planeIDProperty() {return PlaneID;}

        public String getDepartureGate() {return DepartureGate.get();}
        public SimpleStringProperty departureGateProperty() {return DepartureGate;}

        public String getFlightNumber() {return FlightNumber.get();}
        public SimpleStringProperty flightNumberProperty() {return FlightNumber;}
    }
    public static class Airline {
        private SimpleIntegerProperty AirlineID;
        private SimpleStringProperty AirlineName;

        public int getAirlineID() {return AirlineID.get();}
        public SimpleIntegerProperty airlineIDProperty() {return AirlineID;}

        public String getAirlineName() {return AirlineName.get();}
        public SimpleStringProperty airlineNameProperty() {return AirlineName;}

        public void setAirlineName(String airlineName) {this.AirlineName.set(airlineName);}
        public void setAirlineID(int airlineID) {this.AirlineID.set(airlineID);}

        Airline(SimpleIntegerProperty airlineID, SimpleStringProperty airlineName) {
            AirlineID = airlineID;
            AirlineName = airlineName;
        }
    }
    public static class TerminalFlightList {
        private SimpleStringProperty flightNumber;
        private SimpleStringProperty plane;
        private SimpleStringProperty departureTime;
        private ImageView airlineLogo;
        private SimpleStringProperty status;
        private SimpleStringProperty destination;
        private SimpleStringProperty gate;
        private SimpleIntegerProperty JourneyID;

        TerminalFlightList(SimpleStringProperty flightNumber, SimpleStringProperty plane, SimpleStringProperty departureTime, ImageView airlineLogo, SimpleStringProperty destination, SimpleStringProperty status, SimpleStringProperty gate, SimpleIntegerProperty JourneyID) {
            this.flightNumber = flightNumber;
            this.plane = plane;
            this.departureTime = departureTime;
            this.airlineLogo = airlineLogo;
            this.destination = destination;
            this.status = status;
            this.gate = gate;
            this.JourneyID = JourneyID;
        }

        public Integer getJourneyID() {return JourneyID.get();}
        public SimpleIntegerProperty journeyIDProperty() {return JourneyID;}
        public void setJourneyID(int journeyID) {this.JourneyID.set(journeyID);}

        public String getFlightNumber() {return flightNumber.get();}
        public SimpleStringProperty flightNumberProperty() {return flightNumber;}
        public void setFlightNumber(String flightNumber) {this.flightNumber.set(flightNumber);}

        public String getPlane() {return plane.get();}
        public SimpleStringProperty planeProperty() {return plane;}
        public void setPlane(String plane) {this.plane.set(plane);}

        public ImageView getAirlineLogo() {return airlineLogo;}
        public void setAirlineLogo(ImageView airlineLogo) {this.airlineLogo = airlineLogo;}

        public String getDepartureTime() {return departureTime.get();}
        public void setDepartureTime(String departureTime) {this.departureTime.set(departureTime);}
        public SimpleStringProperty departureProperty() {return departureTime;}


        public String getStatus() {return status.get();}
        public SimpleStringProperty statusProperty() {return status;}
        public void setStatus(String status) {this.status.set(status);}

        public String getDestination() {return destination.get();}
        public SimpleStringProperty destinationProperty() {return destination;}
        public void setDestination(String destination) {this.destination.set(destination);}


        public String getGate() {return gate.get();}
        public SimpleStringProperty gateProperty() {return gate;}
        public void setGate(String gate) {this.gate.set(gate);}
    }

}
