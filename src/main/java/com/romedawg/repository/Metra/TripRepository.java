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

    // Used as a check for stop_time loading in MetraUpdates
    @Query("SELECT count(t.tripId) FROM Trip AS t")
    int getTripCount();

    @Query("SELECT t.tripId FROM Trip AS t WHERE t.tripId = (:trip_id)")
    String getTripById(@Param("trip_id") String trip_id);

    // Hinsdale(or any stops trip_ids)
    @Query("SELECT DISTINCT t.tripId FROM Trip AS t JOIN StopTime AS st ON t.tripId = st.tripId JOIN Stop AS s ON s.stopId = st.stopId WHERE t.directionId = 1 AND st.stopId = (:stop_id)")
    List<String> getTripIdByStopId(@Param("stop_id") String stop_id);
}

