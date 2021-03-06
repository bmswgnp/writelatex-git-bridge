package uk.ac.ic.wlgitbridge.data.model.db;

import uk.ac.ic.wlgitbridge.data.model.db.sql.SQLiteWLDatabase;
import uk.ac.ic.wlgitbridge.util.Log;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Winston on 19/11/14.
 */
public class SqlitePersistentStore implements PersistentStore {

    private final SQLiteWLDatabase database;

    public SqlitePersistentStore(File rootGitDirectory) {
        try {
            database = new SQLiteWLDatabase(rootGitDirectory);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getProjectNames() {
        try {
            return database.getProjectNames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void setLatestVersionForProject(String project, int versionID) {
        try {
            database.setVersionIDForProject(project, versionID);
            Log.info("[{}] Wrote latest versionId: {}", project, versionID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getLatestVersionForProject(String project) {
        try {
            return database.getVersionIDForProjectName(project);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addURLIndexForProject(String projectName, String url, String path) {
        try {
            database.addURLIndex(projectName, url, path);
            Log.info("[{}] Wrote url index: {} -> {}", projectName, url, path);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFilesForProject(String project, String... files) {
        try {
            database.deleteFilesForProject(project, files);
            Log.info(
                    "[{}] Deleting from url index: {}",
                    project,
                    Arrays.toString(files)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getPathForURLInProject(String projectName, String url) {
        try {
            return database.getPathForURLInProject(projectName, url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
