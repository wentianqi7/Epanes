package cmu.math.epanes.database;

import java.util.List;

import cmu.math.epanes.model.ProjectItem;

/**
 * Created by Tianqi.Wen on 2016/5/1.
 */
public interface ProjectCRUD {
    void insertProject(ProjectItem proj);
    ProjectItem readProjectById(int pid);
    List<ProjectItem> readHistory(int size);
    ProjectItem readLastProject();
}
