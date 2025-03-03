package edu.kau.fcit.cpit252;

import org.apache.hc.core5.net.URIBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Car implements Cloneable {
    private String make;
    private String model;
    private int year;
    private List<Recall> recalls;

    // constructor that fetche recall data
    public Car(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.recalls = fetchRecalls();
    }

    
    public Car(Car other) {
        this.make = other.make;
        this.model = other.model;
        this.year = other.year;
        this.recalls = new ArrayList<>(); 
        this.recalls.addAll(other.recalls); 
    }

    // fetch recall data from The API
    private List<Recall> fetchRecalls() {
        System.out.println("====================================");
        System.out.println("Fetching recall information...");
        List<Recall> recalls = new ArrayList<>();

        try {
            URIBuilder uriBuilder = new URIBuilder("https://api.nhtsa.gov/recalls/recallsByVehicle");
            uriBuilder.addParameter("make", this.make);
            uriBuilder.addParameter("model", this.model);
            uriBuilder.addParameter("modelYear", Integer.toString(this.year));
            URI requestUri = uriBuilder.build();

            HttpResponse<String> response = HTTPHelper.sendGet(requestUri);
            if (response != null) {
                recalls = HTTPHelper.parseIntoCollection(response.body(), List.class, Recall.class);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return recalls;
    }

    
    @Override
    public Car clone() {
        return new Car(this);
    }

   
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Recall> getRecalls() {
        return recalls;
    }

    
    @Override
    public String toString() {
        StringBuilder recallDetails = new StringBuilder();
        recallDetails.append(String.format("Total Recalls: %d\n", recalls.size()));
        for (Recall recall : recalls) {
            recallDetails.append(recall.toString()).append("\n");
        }
        return "Car Details: " + make + " | " + model + " | " + year + "\n" + recallDetails;
    }
}
