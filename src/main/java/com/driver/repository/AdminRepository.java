package com.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.driver.model.Admin;
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{
    @Query(value = "DELETE FROM admin a WHERE a.admin_id = ?1",
            nativeQuery = true)
    void delete(int adminId);
}
