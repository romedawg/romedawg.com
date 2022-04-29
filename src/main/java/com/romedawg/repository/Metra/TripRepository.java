package com.romedawg.repository.Metra;

import com.romedawg.domain.Metra.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT t.tripId FROM Trip as t WHERE t.tripId = (:trip_id)")
    String getTripById(@Param("trip_id") String trip_id);
}

