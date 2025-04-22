package interfaces;

import entity.Project;

import java.time.LocalDate;
import java.util.List;

public interface ProjectContainer {
      List<Project> getProjects();
      List<Project> getProjects(List<Project.Status> filter);
      boolean addProject(Project project);
      boolean removeProject(Project project);
}
