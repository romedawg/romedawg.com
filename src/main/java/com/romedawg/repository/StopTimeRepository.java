package com.romedawg.repository;

import com.romedawg.domain.Metra.StopTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StopTimeRepository extends JpaRepository<StopTime, Long> {

    @Query("SELECT st.tripId from StopTime as st WHERE st.tripId=(:trip_id)")
    String getTripId(@Param("trip_id") String tripId);

}
