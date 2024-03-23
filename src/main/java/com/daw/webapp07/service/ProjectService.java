package com.daw.webapp07.service;

import com.daw.webapp07.model.Comment;
import com.daw.webapp07.model.Image;
import com.daw.webapp07.model.Project;
import com.daw.webapp07.repository.CommentRepository;
import com.daw.webapp07.repository.ImageRepository;
import com.daw.webapp07.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ImageRepository imageRepository;


    public Page<Project> searchProjects(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return  projectRepository.findAll(pageable);
    }

   public Page<Project> searchRecommendedProjects(int page, int size, long id)
    {
        return projectRepository.recomendedProjects(id, PageRequest.of(page, size));
    }

    public Optional<Project> getOptionalProject(Long id){
        return projectRepository.findById(id);
    }

    public void saveProject(Project p){
        projectRepository.save(p);
    }

    public void deleteProject(Long id){
        System.out.println("Deleting project with id: "+id);
        commentRepository.deleteByProjectId(id);
        System.out.println("Comments deleted");
        for(Image i: projectRepository.findById(id).get().getImages()){
            imageRepository.deleteById(i.getId());
        }
        projectRepository.deleteImageRelartions(id);
        projectRepository.deleteUserRelations(id);
        projectRepository.deleteById1(id);
    }

    public Image getImage(Project project, Long img){
        if (project.getImages().contains(imageRepository.findById(img).get())){
            return imageRepository.findById(img).get();
        }
        return null;
    }

    public List<Project> findByOwnerName(String name){
        return projectRepository.findByOwnerName(name);
    }

    public List<Project> findAll(){
        return projectRepository.findAll();
    }

}
