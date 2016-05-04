package cmu.math.epanes.database;

/**
 * Created by STuotuo.Wen on 2016/4/14.
 */
public interface ConstQuery {
    String DATABASE = "Epanes";

    String CREATE_HISTORY_TABLE = "CREATE TABLE history" +
            "(pid INTEGER PRIMARY KEY," +
            "title VARCHAR(40)," +
            "url TEXT," +
            "created INTEGER);";
}
