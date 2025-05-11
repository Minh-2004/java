package com.phamquangminh.example5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phamquangminh.example5.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

}
