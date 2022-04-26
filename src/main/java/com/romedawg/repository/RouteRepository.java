package com.romedawg.repository;

import com.romedawg.domain.Metra.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

//    @Query("SELECT route_id from Route WHERE route_id = (:route_id)")
//    String findRouteID(@Param("route_id") String route_id);

}
