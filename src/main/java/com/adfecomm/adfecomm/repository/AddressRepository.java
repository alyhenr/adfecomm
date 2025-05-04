package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.Address;
import com.adfecomm.adfecomm.payload.AddressDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.user.userId = ?1")
    List<Address> findAddressByUserId(Long userId);
    @Query("SELECT a FROM Address a WHERE a.user.userId = ?1 AND a.addressId = ?2")
    Address findAddressByUserIdAndAddressId(Long userId, Long addressId);
}
