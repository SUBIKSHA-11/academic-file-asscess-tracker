package com.university.securetracker.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.university.securetracker.model.DownloadToken;

public interface DownloadTokenRepository
        extends JpaRepository<DownloadToken, Long> {

    Optional<DownloadToken> findByToken(String token);

    void deleteByToken(String token);
}
