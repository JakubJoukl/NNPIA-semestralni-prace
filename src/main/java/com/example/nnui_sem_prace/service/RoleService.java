package com.example.nnui_sem_prace.service;

import com.example.nnui_sem_prace.model.Role;
import com.example.nnui_sem_prace.repository.RoleRepository;
import com.example.nnui_sem_prace.repository.UzivatelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleById(int id){
        return roleRepository.getRoleByRoleId(id);
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }
}
