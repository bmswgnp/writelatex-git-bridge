package uk.ac.ic.wlgitbridge.data.filestore;

import uk.ac.ic.wlgitbridge.data.model.Snapshot;
import uk.ac.ic.wlgitbridge.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Winston on 14/11/14.
 */
public class GitDirectoryContents {

    private final List<RawFile> files;
    private final File gitDirectory;
    private final String userName;
    private final String userEmail;
    private final String commitMessage;
    private final Date when;

    public GitDirectoryContents(List<RawFile> files, File rootGitDirectory, String projectName, Snapshot snapshot) {
        this.files = files;
        gitDirectory = new File(rootGitDirectory, projectName);
        userName = snapshot.getUserName();
        userEmail = snapshot.getUserEmail();
        commitMessage = snapshot.getComment();
        when = snapshot.getCreatedAt();
    }

    public void write() throws IOException {
        Util.deleteInDirectoryApartFrom(gitDirectory, ".git");
        for (RawFile fileNode : files) {
            fileNode.writeToDisk(gitDirectory);
        }
    }

    public File getDirectory() {
        return gitDirectory;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public Date getWhen() {
        return when;
    }

}
