package cmu.math.epanes.entity;

import android.content.Context;

import java.util.List;

import cmu.math.epanes.database.DBAdapter;
import cmu.math.epanes.database.ProjectCRUD;
import cmu.math.epanes.model.ProjectItem;

/**
 * Created by STuotuo.Wen on 2016/4/14.
 */
public class ProjectManager implements ProjectCRUD {
    private static ProjectManager manager;
    private Context appContext;
    DBAdapter adapter = null;

    public ProjectManager(Context context) {
        appContext = context;
        adapter = new DBAdapter(context);
    }

    public static ProjectManager get(Context context) {
        if (manager == null) {
            manager = new ProjectManager(context.getApplicationContext());
        }
        return manager;
    }

    public void insertProject(ProjectItem proj) {
        adapter.insertProject(proj);
    }

    public ProjectItem readProjectById(int pid) {
        return adapter.readProjectById(pid);
    }

    public List<ProjectItem> readHistory(int size) {
        return adapter.readHistory(size);
    }

    public ProjectItem readLastProject() {
        return adapter.readLastProject();
    }
}
