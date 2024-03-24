package com.daw.webapp07.service;

import com.daw.webapp07.model.Project;
import com.daw.webapp07.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;
    public List<UserEntity> getTopInvestors() {
        List<UserEntity> users = userService.findAll();
        int n = users.size();
        boolean swapped;

        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (users.get(i).getTotalInvested() < users.get(i + 1).getTotalInvested()) {
                    UserEntity temp = users.get(i);
                    users.set(i, users.get(i + 1));
                    users.set(i + 1, temp);
                    swapped = true;
                }
            }
            n--;
        } while (swapped && n > 0);
        return users;
    }

    public List<Project> getTopProjects() {
        List<Project> projects = projectService.findAll();
        int n = projects.size();
        boolean swapped;

        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (projects.get(i).getCurrentAmount() < projects.get(i + 1).getCurrentAmount()) {
                    Project temp = projects.get(i);
                    projects.set(i, projects.get(i + 1));
                    projects.set(i + 1, temp);
                    swapped = true;
                }
            }
            n--;
        } while (swapped && n > 0);
        return projects;
    }
}
