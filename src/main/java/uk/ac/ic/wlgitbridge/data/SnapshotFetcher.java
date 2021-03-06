package uk.ac.ic.wlgitbridge.data;

import com.google.api.client.auth.oauth2.Credential;
import uk.ac.ic.wlgitbridge.snapshot.base.ForbiddenException;
import uk.ac.ic.wlgitbridge.snapshot.exception.FailedConnectionException;
import uk.ac.ic.wlgitbridge.snapshot.getdoc.GetDocRequest;
import uk.ac.ic.wlgitbridge.snapshot.getdoc.GetDocResult;
import uk.ac.ic.wlgitbridge.snapshot.getforversion.SnapshotData;
import uk.ac.ic.wlgitbridge.snapshot.getforversion.GetForVersionRequest;
import uk.ac.ic.wlgitbridge.snapshot.getsavedvers.GetSavedVersRequest;
import uk.ac.ic.wlgitbridge.snapshot.getsavedvers.SnapshotInfo;
import uk.ac.ic.wlgitbridge.snapshot.push.exception.SnapshotPostException;
import uk.ac.ic.wlgitbridge.data.model.Snapshot;

import java.util.*;

/**
 * Created by Winston on 07/11/14.
 */
public class SnapshotFetcher {

    public LinkedList<Snapshot> getSnapshotsForProjectAfterVersion(Credential oauth2, String projectName, int version) throws FailedConnectionException, SnapshotPostException, ForbiddenException {
        List<SnapshotInfo> snapshotInfos = getSnapshotInfosAfterVersion(oauth2, projectName, version);
        List<SnapshotData> snapshotDatas = getMatchingSnapshotData(oauth2, projectName, snapshotInfos);
        LinkedList<Snapshot> snapshots = combine(snapshotInfos, snapshotDatas);
        return snapshots;
    }

    private List<SnapshotInfo> getSnapshotInfosAfterVersion(Credential oauth2, String projectName, int version) throws FailedConnectionException, SnapshotPostException, ForbiddenException {
        SortedSet<SnapshotInfo> versions = new TreeSet<SnapshotInfo>();
        GetDocRequest getDoc = new GetDocRequest(oauth2, projectName);
        GetSavedVersRequest getSavedVers = new GetSavedVersRequest(oauth2, projectName);
        getDoc.request();
        getSavedVers.request();
        GetDocResult latestDoc = getDoc.getResult();
        int latest = latestDoc.getVersionID();
        if (latest > version) {
            for (SnapshotInfo snapshotInfo : getSavedVers.getResult().getSavedVers()) {
                if (snapshotInfo.getVersionId() > version) {
                    versions.add(snapshotInfo);
                }
            }
            versions.add(new SnapshotInfo(latest, latestDoc.getCreatedAt(), latestDoc.getName(), latestDoc.getEmail()));

        }
        return new LinkedList<SnapshotInfo>(versions);
    }

    private List<SnapshotData> getMatchingSnapshotData(Credential oauth2, String projectName, List<SnapshotInfo> snapshotInfos) throws FailedConnectionException, ForbiddenException {
        List<GetForVersionRequest> firedRequests = fireDataRequests(oauth2, projectName, snapshotInfos);
        List<SnapshotData> snapshotDataList = new LinkedList<SnapshotData>();
        for (GetForVersionRequest fired : firedRequests) {
            snapshotDataList.add(fired.getResult().getSnapshotData());
        }
        return snapshotDataList;
    }

    private List<GetForVersionRequest> fireDataRequests(Credential oauth2, String projectName, List<SnapshotInfo> snapshotInfos) {
        List<GetForVersionRequest> requests = new LinkedList<GetForVersionRequest>();
        for (SnapshotInfo snapshotInfo : snapshotInfos) {
            GetForVersionRequest request = new GetForVersionRequest(oauth2, projectName, snapshotInfo.getVersionId());
            requests.add(request);
            request.request();
        }
        return requests;
    }

    private LinkedList<Snapshot> combine(List<SnapshotInfo> snapshotInfos, List<SnapshotData> snapshotDatas) {
        LinkedList<Snapshot> snapshots = new LinkedList<Snapshot>();
        Iterator<SnapshotInfo> infos = snapshotInfos.iterator();
        Iterator<SnapshotData> datas = snapshotDatas.iterator();
        while (infos.hasNext()) {
            snapshots.add(new Snapshot(infos.next(), datas.next()));
        }
        return snapshots;
    }

}
