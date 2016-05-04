package cmu.math.epanes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cmu.math.epanes.model.ProjectItem;

/**
 * created by Tianqi Wen on 05/01/2016
 */
public class DBAdapter implements ProjectCRUD, ConstQuery {
    private SQLiteDatabase db;
    private DBOpenHelper conn;

    public DBAdapter(Context context) {
        conn = new DBOpenHelper(context, DATABASE, null, 1);
    }

    public void open() throws SQLException {
        db = conn.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public void insertProject(ProjectItem proj) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ProjectItem oldProj = null;
        Cursor cursor = db.query("history", null, "pid=" + proj.getPid(), null, null, null, null);

        ContentValues args = new ContentValues();
        args.put("title", proj.getTitle());
        args.put("url", proj.getUrl());
        args.put("created", proj.getTime());

        if (cursor.moveToNext()) {
            // project exists, update the old one
            db.update("history", args, "pid=" + proj.getPid(), null);
        } else {
            // insert new project
            args.put("pid", proj.getPid());
            db.insert("history", null, args);
        }
        close();
    }

    public ProjectItem readProjectById(int pid) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ProjectItem proj = null;
        Cursor cursor = db.query("history", null, "pid=" + pid, null, null, null, null);
        if (cursor.moveToNext()) {
            try {
                proj = new ProjectItem(pid, cursor.getString(1), cursor.getString(2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        close();
        return proj;
    }

    public List<ProjectItem> readHistory(int size) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<ProjectItem> projList = new ArrayList<ProjectItem>();
        Cursor cursor = db.query("history", null, null, null, null, null, "created DESC", Integer.toString(size));
        while (cursor.moveToNext()) {
            try {
                projList.add(new ProjectItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        close();
        return projList;
    }

    public ProjectItem readLastProject() {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ProjectItem proj = null;
        Cursor cursor = db.query("history", null, null, null, null, null, "created DESC", "1");
        if (cursor.moveToNext()) {
            proj = new ProjectItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        }
        close();
        return proj;
    }
}
