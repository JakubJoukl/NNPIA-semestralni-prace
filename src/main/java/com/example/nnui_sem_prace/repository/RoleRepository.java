package com.example.nnui_sem_prace.repository;

import com.example.nnui_sem_prace.model.Role;
import com.example.nnui_sem_prace.model.Uzivatel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    public Role getRoleByRoleId(Integer roleId);
}
