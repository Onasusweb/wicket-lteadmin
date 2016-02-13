package com.bustanil.myapp.auth.service;

import com.bustanil.myapp.auth.model.Privilege;
import com.bustanil.myapp.auth.model.User;
import com.bustanil.myapp.auth.dto.UserPrivilegeDTO;

import java.util.Collection;

public interface UserManagementService {
    Collection<Privilege> findPrivilegesByKode(String email);

    User getUser(Long userId);

    User getUser(String kodeUser);

    UserPrivilegeDTO findPrivilegeGroupByKode(String kodeUser);
}
