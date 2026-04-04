package com.utilities;


import java.io.IOException;
import java.util.Iterator;

import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.htmlunit.javascript.SilentJavaScriptErrorListener;
import org.htmlunit.javascript.host.dom.Document;


class plane {
    private String gate;
    private String departure;
    private String delayed;
    private int passengers;
    private float distance;
    private int attendants;
    private boolean ready;

    plane(String gate, String departure, String delayed, int passengers, float distance, int attendants, boolean ready) {
        this.gate = gate;
        this.departure = departure;
        this.delayed = delayed;
        this.passengers = passengers;
        this.distance = distance;
        this.attendants = attendants;
        this.ready = ready;
    }

    boolean getReady() {return this.ready;}
    public void setReady(boolean ready) {this.ready = ready;}

    int getAttendants() {return this.attendants;}
    public void setAttendants(int attendants) {this.attendants = attendants;}

    float getDistance() {return this.distance;}
    public void setDistance(float distance) {this.distance = distance;}

    int getPassengers() {return this.passengers;}
    public void setPassengers(int passengers) {this.passengers = passengers;}

    public String getDelayed() {return delayed;}
    public void setDelayed(String delayed) {this.delayed = delayed;}

    String getDeparture() {return this.departure;}
    public void setDeparture(String departure) {this.departure = departure;}

    public void setGate(String gate) {this.gate = gate;}
    String getGate() {return this.gate;}


}

public class flight_fetcher {

    public static void main(String[] args) throws IOException {
        fetch_flights_all();
    }

    public static void fetch_flights_all() throws IOException {
        WebClient webClient = new WebClient();
        webClient.setCssErrorHandler(new SilentCssErrorHandler()); // This suppresses the CSS errors
        webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
        webClient.getOptions().setJavaScriptEnabled(false);

        HtmlPage page = webClient.getPage("https://airports.malaysiaairports.com.my/en/klia2/flight-information/departures");

        DomNode container = page.querySelector("ant-pagination \\!mb-10 css-pjsbzo");
        Iterator<DomNode> children = container.getChildren().iterator();

        plane[] planeArray = new plane[2400];

        if(children.hasNext()) {
            children.next();
            children.next();

            HtmlElement child;
            while (children.hasNext()) {
                // Each individual card
                DomNodeList<DomNode> cards = page.querySelectorAll("ant-col ant-col-xs-24 ant-col-sm-24 ant-col-md-24 ant-col-lg-11 css-pjsbzo");

                // Gets the data for flight information - a list of all the data in flight information
                DomNodeList<DomNode> flight_info = page.querySelectorAll("flex gap-2 items-center justify-center md:justify-start");

                //Get departure data
                DomNodeList<DomNode> departure_info = page.querySelectorAll("ant-col my-5 md:my-0 ant-col-xs-24 ant-col-md-7 css-pjsbzo");

                //Get arrival data
                DomNodeList<DomNode> arrival_info = page.querySelectorAll("ant-col !flex flex-col items-center md:justify-start ant-col-xs-24 ant-col-md-8 css-pjsbzo");

                // get check-in/flight data
                DomNodeList<DomNode> terminal_info = page.querySelectorAll("ant-col ant-col-xs-24 ant-col-sm-24 ant-col-md-12 ant-col-lg-5 css-pjsbzo");

                // get boarding gates, etc.
                DomNodeList<DomNode> boarding_info = page.querySelectorAll("ant-col ant-col-xs-24 ant-col-sm-12 ant-col-lg-10 css-pjsbzo");

                //Get boarding status, for example, "departed"/"final call", etc.
                DomNodeList<DomNode> boarding_status = page.querySelectorAll("ant-row ant-row-center ant-row-middle css-pjsbzo");


                int length;
                for(int i=0; i< flight_info.getLength(); i++) {
                    DomNodeList<DomNode> departure_data = (DomNodeList<DomNode>) departure_info.get(i).getChildren();
                    length = departure_data.getLength();

                    String departure, delayed;
                    if(length == 3) {
                        departure = departure_data.get(0).getFirstChild().getVisibleText();
                        delayed = departure_data.get(2).getFirstChild().getVisibleText();

                    }
                    else {
                        departure = departure_data.getFirst().getFirstChild().getVisibleText();
                        delayed = "";
                    }

                }

                child = (HtmlElement) children.next();
                child.click();

            }
        }


    }

}
