/*
 * The MIT License
 *
 * Copyright 2015 Diego.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eaero.flights.models;

import com.eaero.flights.FlightResume;
import com.eaero.DataAccessObject;
import com.eaero.flights.Flight;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FlightResumeDAO extends DataAccessObject {
    
    private String sqlBase = "SELECT " +
                "flight.id AS flight_id, " +
                "flight.hour  AS flight_hour, " +
                "flight.cost AS flight_cost, " +
                "flight.date AS flight_date, " +
                "aircraft.id AS aircraft_id, " +
                "aircraft.code AS aircraft_code, " +
                "aircraft.seats AS aircraft_seats, " +
                "aircraft.seatsFirstClass AS aircraft_firstclass, " +
                "company.id AS company_id, " +
                "company.name AS company_name, " +
                "itinerary.id AS itinerary_id, " +
                "itinerary.code AS itinerary_code, " +
                "itinerary.departure AS itinerary_departure, " +
                "itinerary.destination AS itinerary_destination, " +
                "itinerary.duration AS itinerary_duration, " +
                "routines.id AS routine_id, " +
                "routines.days AS routine_days, " +
                "(SELECT COUNT(*) FROM tickets WHERE tickets.flight_id = flight.id AND tickets.firstClass = 0) AS tickets_sale, " +
                "(SELECT COUNT(*) FROM tickets WHERE tickets.flight_id = flight.id AND tickets.firstClass = 1) AS tickets_sale_firstclass " +
                "FROM flights AS flight " +
                "LEFT JOIN aircrafts AS aircraft ON flight.aircraft_id = aircraft.id " +
                "LEFT JOIN companies AS company ON aircraft.company_id = company.id " +
                "LEFT JOIN itineraries AS itinerary ON flight.itinerary_id = itinerary.id " +
                "LEFT JOIN flights_routines AS routines ON flight.routine_id = routines.id";
    
    private ArrayList<FlightResume> fetch(String superQuery) throws SQLException
    {
        ArrayList<FlightResume> list = new ArrayList<>();
        
        try(PreparedStatement stmt = this.query(superQuery)){
           ResultSet result = stmt.executeQuery();
           
           while(result.next())
           {
                FlightResume resume = new FlightResume();
                resume.setFlightId(result.getInt("flight_id"));
                resume.setFlightHour(result.getTime("flight_hour"));
                resume.setFlightCost(result.getDouble("flight_cost"));
                resume.setFlightDate(result.getDate("flight_date"));
                resume.setAircraftId(result.getInt("aircraft_id"));
                resume.setAircraftCode(result.getString("aircraft_code"));
                resume.setAircraftSeats(result.getInt("aircraft_seats"));
                resume.setAircraftSeatsFistClass(result.getInt("aircraft_firstclass"));
                resume.setCompanyId(result.getInt("company_id"));
                resume.setCompanyName(result.getString("company_name"));
                resume.setItineraryID(result.getInt("itinerary_id"));
                resume.setItineraryCode(result.getString("itinerary_code"));
                resume.setItineraryDeparture(result.getString("itinerary_departure"));
                resume.setItineraryDestination(result.getString("itinerary_destination"));
                resume.setItineraryDuration(result.getDouble("itinerary_duration"));
                resume.setRoutineId(result.getInt("routine_id"));
                resume.setRoutineDays(result.getString("routine_days"));
                resume.setTicketsSale(result.getInt("tickets_sale"));
                resume.setTicketsSaleFirstClass(result.getInt("tickets_sale_firstclass"));
                
                list.add(resume);
           }
       }
        
        return list;
    }        
    
    public ArrayList<FlightResume> search(String departure, String destination, String date)  throws SQLException
    {
        return this.fetch(this.sqlBase + " WHERE itinerary.departure LIKE '%" + departure + "%' AND itinerary.destination LIKE '%" + destination + "%' AND routines.days LIKE '%"+ date +"%';");
    }
    
    public FlightResume getResume(int flight_id) throws SQLException
    {   
        ArrayList<FlightResume> result = this.fetch(this.sqlBase + " WHERE flight.id = " + flight_id +";");
        return (result.size() > 0) ? result.get(0) : null;
    }
    
    public ArrayList<FlightResume> getRes() throws SQLException
    {
        ArrayList<FlightResume> list = new ArrayList<>();
        
        try(PreparedStatement stms = this.query(sqlBase)){
            
            ResultSet result = stms.executeQuery();
            
            while(result.next())
           {
                FlightResume resume = new FlightResume();
                resume.setFlightId(result.getInt("flight_id"));
                resume.setFlightHour(result.getTime("flight_hour"));
                resume.setFlightCost(result.getDouble("flight_cost"));
                resume.setFlightDate(result.getDate("flight_date"));
                resume.setAircraftId(result.getInt("aircraft_id"));
                resume.setAircraftCode(result.getString("aircraft_code"));
                resume.setAircraftSeats(result.getInt("aircraft_seats"));
                resume.setAircraftSeatsFistClass(result.getInt("aircraft_firstclass"));
                resume.setCompanyId(result.getInt("company_id"));
                resume.setCompanyName(result.getString("company_name"));
                resume.setItineraryID(result.getInt("itinerary_id"));
                resume.setItineraryCode(result.getString("itinerary_code"));
                resume.setItineraryDeparture(result.getString("itinerary_departure"));
                resume.setItineraryDestination(result.getString("itinerary_destination"));
                resume.setItineraryDuration(result.getDouble("itinerary_duration"));
                resume.setRoutineId(result.getInt("routine_id"));
                resume.setRoutineDays(result.getString("routine_days"));
                resume.setTicketsSale(result.getInt("tickets_sale"));
                resume.setTicketsSaleFirstClass(result.getInt("tickets_sale_firstclass"));
                
                list.add(resume);
           }
            stms.close();
        }
        return list;
    }
    private ArrayList<Flight> toList(ResultSet resultset) throws SQLException{
        ArrayList<Flight> list = new ArrayList<>();
        
        while(resultset.next()) {
            Flight item= new Flight();
            item.setId(resultset.getInt("id"));
                       
            list.add(item);
        }
        
        resultset.close();
        
        return list;
    }
    public ArrayList<Flight> read() throws SQLException {
        ArrayList<Flight> aircraft;
        try (PreparedStatement stmt = this.query("SELECT * FROM aircrafts " )) 
        {
            ResultSet rs = stmt.executeQuery();
            aircraft = this.toList(rs);
        }
        
        return aircraft;
    }
}
