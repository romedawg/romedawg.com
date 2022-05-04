package com.romedawg.repository.Metra;

import com.romedawg.domain.Metra.Stop;
import com.romedawg.domain.Metra.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT t.tripId FROM Trip AS t WHERE t.tripId = (:trip_id)")
    String getTripById(@Param("trip_id") String trip_id);

    //Hinsdale(or any stops trip_ids)
    @Query("SELECT DISTINCT t.tripId FROM Trip AS t JOIN StopTime AS st ON t.tripId = st.tripId JOIN Stop AS s ON s.stopId = st.stopId WHERE t.directionId = 1 AND st.stopId = (:stop_id)")
    List<String> getTripIdByStopId(@Param("stop_id") String stop_id);

    // Select arrivale time by stop_id(CUS) AND trip_ID)
    // The idea here is to filter all hinsdale stop, to retrieve the trip_id is
    // Then query Chicato Union station times to get arrival time to Chicago for the trip_ids for all Hinsdale stops
    // CUS has many more trip_ids than Hinsdale
    @Query("SELECT DISTINCT st.arrivalTime FROM Trip AS t JOIN StopTime AS st ON t.tripId = st.tripId JOIN Stop AS s ON s.stopId = st.stopId WHERE t.directionId = 1 AND st.stopId = 'CUS' AND t.tripId IN (:trip_id)")
    List<String> getArrivalByCUSTripID(@Param("trip_id") List<String> trip_id);

    @Query("SELECT DISTINCT st.arrivalTime FROM Trip AS t JOIN StopTime AS st ON t.tripId = st.tripId JOIN Stop AS s ON s.stopId = st.stopId WHERE t.directionId = 1 AND st.stopId = 'HINSDALE' AND t.tripId IN (:trip_id)")
    List<String> getArrivalByHinsdaleTripID(@Param("trip_id") List<String> trip_id);


    // To Chicago
    // select t.direction_id, t.trip_headsign, t.trip_id , st.arrival_time, st.departure_time, st.stop_id from trip as t INNER JOIN stop_time as st ON t.trip_id = st.trip_id INNER JOIN stop as s on s.stop_id = st.stop_id
    // WHERE t.direction_id = 1 AND st.stop_id = 'HINSDALE' OR st.stop_id = 'CUS' AND t.trip_headsign = 'Chicago Union Station' or t.trip_headsign = 'AURORA';

    // From Chicago
    // select t.direction_id, t.trip_headsign, t.trip_id , st.arrival_time, st.departure_time, st.stop_id from trip as t INNER JOIN stop_time as st ON t.trip_id = st.trip_id INNER JOIN stop as s on s.stop_id = st.stop_id
    // WHERE t.direction_id = 0 AND st.stop_id = 'HINSDALE' OR st.stop_id = 'CUS' AND t.trip_headsign = 'Chicago Union Station' or t.trip_headsign = 'AURORA';

}

