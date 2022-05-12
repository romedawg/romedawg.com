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

//    List<HinsdaleSchedule> findAll();

    @Query("SELECT DISTINCT hs.departureTime FROM HinsdaleSchedule as hs WHERE hs.departureTime= (:departure_time)")
    String getTripDepartureTime(@Param("departure_time") String departure_time);

}
