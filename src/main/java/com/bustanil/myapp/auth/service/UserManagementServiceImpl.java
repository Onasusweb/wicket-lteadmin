package com.bustanil.myapp.auth.service;

import com.bustanil.myapp.auth.dto.PrivilegeDTO;
import com.bustanil.myapp.auth.dto.PrivilegeGroupDTO;
import com.bustanil.myapp.auth.dto.UserPrivilegeDTO;
import com.bustanil.myapp.auth.model.Privilege;
import com.bustanil.myapp.auth.model.PrivilegeGroup;
import com.bustanil.myapp.auth.model.Role;
import com.bustanil.myapp.auth.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    public static final Role ADMIN_ROLE = new Role(1L, "Administrator");
    public static final User ADMIN = new User("admin", "Admin User", "admin", ADMIN_ROLE);

    static {

        PrivilegeGroup privilegeGroup = new PrivilegeGroup(1L, "Menu");
        privilegeGroup.addPrivilege(new Privilege(1L, "Sub Menu 1", ""));
        privilegeGroup.addPrivilege(new Privilege(2L, "Sub Menu 2", ""));
        privilegeGroup.addPrivilege(new Privilege(3L, "Sub Menu 3", ""));

        ADMIN_ROLE.getPrivileges().addAll(privilegeGroup.getPrivileges());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Collection<Privilege> findPrivilegesByKode(String kode) {
        if (!kode.equals(ADMIN.getKode())) {
            throw new IllegalArgumentException("Kode user tidak valid");
        }
        User user = ADMIN;
        Role role = user.getRole();
        return role.getPrivileges();
    }

    @Override
    public User getUser(Long userId) {
        return ADMIN;
    }

    @Override
    public User getUser(String kodeUser) {
        if (kodeUser.equals(ADMIN.getKode())) {
            return ADMIN;
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserPrivilegeDTO findPrivilegeGroupByKode(String kodeUser) {
        List<Privilege> sorted = new ArrayList<>(findPrivilegesByKode(kodeUser));
        Collections.sort(sorted, (o1, o2) -> o1.getId().compareTo(o2.getId()));

        UserPrivilegeDTO dto = new UserPrivilegeDTO();
        for (Privilege privilege : sorted) {
            dto.addPrivilege(new PrivilegeGroupDTO(privilege.getGroup()), new PrivilegeDTO(privilege));
        }

        return dto;
    }

}
