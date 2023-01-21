package com.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.driver.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    @Query(value = "DELETE FROM customer c WHERE c.customerId = ?1",
           nativeQuery = true)
    void deleteCustomerByCustomerId(Integer customerId);
}
