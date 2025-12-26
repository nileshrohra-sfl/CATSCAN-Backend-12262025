package com.sfl.core.repository;

import com.sfl.core.domain.UserDeviceDetail;
import com.sfl.core.service.dto.CountPerOSDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeviceDetailRepository extends JpaRepository<UserDeviceDetail,Long>, JpaSpecificationExecutor<UserDeviceDetail> {

    @Query("Select new com.sfl.core.service.dto.CountPerOSDTO(count(udd),udd.os) from UserDeviceDetail udd group by udd.os")
    List<CountPerOSDTO> getOSWiseDemographic();

    @Query("SELECT FUNCTION('DATE_FORMAT', u.createdDate, '%b-%y') AS month, COUNT(u.id) AS userCount " +
        "FROM UserDeviceDetail u " +
        "GROUP BY FUNCTION('DATE_FORMAT', u.createdDate, '%b')")
    List<Object[]> getUserCountByMonth();
}


