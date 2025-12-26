package com.sfl.core.repository;

import com.sfl.core.domain.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

    AppVersion findFirstByOrderByIdDesc();
}
