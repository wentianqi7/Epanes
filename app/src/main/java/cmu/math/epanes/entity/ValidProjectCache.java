package cmu.math.epanes.entity;

import java.util.HashMap;
import java.util.Map;

import cmu.math.epanes.model.ProjectItem;

/**
 * Created by STuotuo.Wen on 2016/5/3.
 */
public class ValidProjectCache {
    private static ValidProjectCache cache;
    private Map<String, ProjectItem> urlToProjectMap;
    private Map<Integer, ProjectItem> idToProjectMap;

    public ValidProjectCache() {
        urlToProjectMap = new HashMap<String, ProjectItem>();
        idToProjectMap = new HashMap<Integer, ProjectItem>();
    }

    public static ValidProjectCache getInstance() {
        if (cache == null) {
            cache = new ValidProjectCache();
        }
        return cache;
    }

    public ProjectItem getProjectByUrl(String url) {
        if (urlToProjectMap.containsKey(url)) {
            return urlToProjectMap.get(url);
        }
        return null;
    }

    public ProjectItem getProjectById(Integer pid) {
        if (idToProjectMap.containsKey(pid)) {
            return idToProjectMap.get(pid);
        }
        return null;
    }

    public void addProject(ProjectItem project) {
        urlToProjectMap.put(project.getUrl(), project);
        idToProjectMap.put(project.getPid(), project);
    }
}
