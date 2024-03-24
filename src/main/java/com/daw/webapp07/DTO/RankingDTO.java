package com.daw.webapp07.DTO;

import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class RankingDTO {
    private List<Long> TopUsersId;
    private List<Long> TopProjectsId;

    public RankingDTO(List<UserEntity> users, List<Project> projects) {
        TopUsersId = new ArrayList<Long>();
        TopUsersId.add(users.get(0).getId());
        TopUsersId.add(users.get(1).getId());
        TopUsersId.add(users.get(2).getId());
        TopProjectsId = new ArrayList<Long>();
        TopProjectsId.add(projects.get(0).getId());
        TopProjectsId.add(projects.get(1).getId());
        TopProjectsId.add(projects.get(2).getId());
    }

    public List<Long> getTopUsersId() {
        return TopUsersId;
    }

    public void setTopUsersId(List<Long> topUsersId) {
        TopUsersId = topUsersId;
    }

    public List<Long> getTopProjectsId() {
        return TopProjectsId;
    }

    public void setTopProjectsId(List<Long> topProjectsId) {
        TopProjectsId = topProjectsId;
    }
}
