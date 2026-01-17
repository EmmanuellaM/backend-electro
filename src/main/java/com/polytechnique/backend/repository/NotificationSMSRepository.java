package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.NotificationSMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationSMSRepository extends JpaRepository<NotificationSMS, Integer> {
    List<NotificationSMS> findBySucces(Boolean succes);
}
