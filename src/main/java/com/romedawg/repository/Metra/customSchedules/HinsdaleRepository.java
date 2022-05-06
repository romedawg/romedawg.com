package com.romedawg.repository.Metra.customSchedules;

import com.romedawg.domain.customSchedules.HinsdaleSchedule;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface HinsdaleRepository extends JpaRepository<HinsdaleSchedule, Long> {

    @Query("SELECT DISTINCT hs.departureTime, hs.departureLocation, hs.arrivalTime, hs.arrivalLocation, hs.tripId FROM HinsdaleSchedule as hs ORDER BY hs.departureTime")
    List<String>  getScheduleByDepartureTime();

    List<HinsdaleSchedule> findAllByOrderByIdAsc();

    @Query("SELECT hs.tripId FROM HinsdaleSchedule as hs WHERE hs.departureTime= (:departure_time)")
    String getRouteDepartureTime(@Param("departure_time") String departure_time);

}
