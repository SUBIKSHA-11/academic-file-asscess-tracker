package com.university.securetracker.repository;

import com.university.securetracker.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findByUserId(Long userId);

    List<ActivityLog> findByAction(String action);


long countByUserIdAndActionAndTimestampAfter(
        Long userId,
        String action,
        LocalDateTime time
);
@Query("""
SELECT l.user.id, COUNT(l)
FROM ActivityLog l
GROUP BY l.user.id
ORDER BY COUNT(l) DESC
""")
List<Object[]> findMostActiveUsers();
}
