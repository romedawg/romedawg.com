package com.romedawg.repository.Metra;

import com.romedawg.domain.Metra.StopTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StopTimeRepository extends JpaRepository<StopTime, Long> {

    @Query("SELECT st.tripId FROM StopTime AS st WHERE st.tripId=(:trip_id)")
    String getTripId(@Param("trip_id") String tripId);

    @Query("SELECT DISTINCT st.arrivalTime FROM StopTime AS st WHERE st.tripId=(:trip_id) AND st.stopId=(:stop_id)")
    String getArrivalTime(@Param("trip_id") String tripId, @Param("stop_id") String stopId );
}
