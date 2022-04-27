package com.romedawg.repository;

import com.romedawg.domain.Metra.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT r.routeId from Route r WHERE r.routeId = (:route_id)")
    Boolean existsRouteByRouteID(@Param("route_id") String route_id);

    @Query("SELECT r.routeId from Route r WHERE r.routeId = (:route_id)")
    String getRouteID(@Param("route_id") String route_id);

}
