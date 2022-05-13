package com.romedawg.repository.Metra;

import com.romedawg.domain.Metra.Stop;
import liquibase.pro.packaged.Q;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {

    @Query("SELECT s.stopId FROM Stop s WHERE s.stopId=(:stop_id)")
    String findStopByStopId(@Param("stop_id") String stop_id);

}
