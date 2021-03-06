package uk.ac.ic.wlgitbridge.git.exception;

import com.google.gson.JsonElement;
import uk.ac.ic.wlgitbridge.snapshot.push.exception.SnapshotPostException;
import uk.ac.ic.wlgitbridge.util.Util;

import java.util.Arrays;
import java.util.List;

public class SizeLimitExceededException extends SnapshotPostException {

    private static String path = null;

    public SizeLimitExceededException(String path) {
        super();
        this.path = path;
    }

    @Override
    public String getMessage() {
        return "file too big";
    }

    @Override
    public List<String> getDescriptionLines() {
        String filename = path != null ? "File '" + path + "' is" : "There's a file";
        return Arrays.asList(
            filename + " too large to push to " + Util.getServiceName() + " via git",
            "the recommended maximum file size is 15MiB"
        );
    }

    @Override
    public void fromJSON(JsonElement json) {

    }
}
