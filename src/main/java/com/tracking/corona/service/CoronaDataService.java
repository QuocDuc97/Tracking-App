package com.tracking.corona.service;

import com.tracking.corona.models.LocationState;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service
@Getter
public class CoronaDataService {

    private List<LocationState> allStats = new ArrayList<>();
    private final static String DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";


    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchData() {

        List<LocationState> newStats = new ArrayList<>();
        //create http client
        HttpClient httpClient = HttpClient.newHttpClient();
        //create http request
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(DATA_URL))
                .build();
        try {


            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            //read data from csv file
            StringReader stringReader = new StringReader(response.body());
            Iterable<CSVRecord> records =
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
            for (CSVRecord record : records) {
                LocationState locationState = new LocationState();
                locationState.setState(record.get("Province/State"));
                locationState.setCountry(record.get("Country/Region"));
                locationState.setLastTotalCases(Integer.parseInt(record.get(record.size() - 1)));
                locationState.setNewTotalCases(locationState.getLastTotalCases()-Integer.parseInt(record.get(record.size() - 2)));
                System.out.println(locationState.toString());
                newStats.add(locationState);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.allStats=newStats;
    }

}
