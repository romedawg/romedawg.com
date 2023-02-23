package com.romedawg.repository.Metra;

import com.romedawg.domain.Metra.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, String> {

    @Query("SELECT a.id from Alert a WHERE a.id = (:id)")
    String existsAlertByAlertID(@Param("id") String id);
}
