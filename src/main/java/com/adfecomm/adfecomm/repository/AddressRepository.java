package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.user.userId = ?1")
    List<Address> findAddressByUserId(Long userId);
}
