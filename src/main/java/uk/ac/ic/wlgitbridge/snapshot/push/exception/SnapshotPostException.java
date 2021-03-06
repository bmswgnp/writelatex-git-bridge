package uk.ac.ic.wlgitbridge.snapshot.push.exception;

import com.google.gson.JsonElement;
import uk.ac.ic.wlgitbridge.snapshot.base.JSONSource;

import java.util.List;

/**
 * Created by Winston on 16/11/14.
 */
public abstract class SnapshotPostException extends Exception implements JSONSource {

    public SnapshotPostException() {

    }

    public SnapshotPostException(JsonElement jsonElement) {
        fromJSON(jsonElement);
    }

    public abstract String getMessage();
    public abstract List<String> getDescriptionLines();

}
