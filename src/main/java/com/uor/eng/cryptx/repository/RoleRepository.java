package com.uor.eng.cryptx.repository;

import com.uor.eng.cryptx.model.AppRole;
import com.uor.eng.cryptx.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRoleName(AppRole appRole);

}
