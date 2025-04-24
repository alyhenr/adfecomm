package com.adfecomm.adfecomm.repository;

import com.adfecomm.adfecomm.model.AppRole;
import com.adfecomm.adfecomm.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole roleName);
}
