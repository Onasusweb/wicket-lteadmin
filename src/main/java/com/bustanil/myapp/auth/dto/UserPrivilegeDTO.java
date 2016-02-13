package com.bustanil.myapp.auth.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserPrivilegeDTO implements Serializable {

    List<PrivilegeGroupDTO> privilegeGroups = new ArrayList<>();

    public void addPrivilege(PrivilegeGroupDTO group, PrivilegeDTO privilege) {
        PrivilegeGroupDTO existingGroup = findPrivilegeGroup(group.getId());
        if (existingGroup == null) {
            privilegeGroups.add(group);
            group.getPrivileges().add(privilege);
        } else {
            existingGroup.getPrivileges().add(privilege);
        }
    }

    private PrivilegeGroupDTO findPrivilegeGroup(Long groupId) {
        for (PrivilegeGroupDTO privilegeGroup : privilegeGroups) {
            if (privilegeGroup.getId().equals(groupId)) {
                return privilegeGroup;
            }
        }
        return null;
    }

    public List<PrivilegeGroupDTO> getPrivilegeGroups() {
        return privilegeGroups;
    }

    public void setPrivilegeGroups(List<PrivilegeGroupDTO> privilegeGroups) {
        this.privilegeGroups = privilegeGroups;
    }
}
