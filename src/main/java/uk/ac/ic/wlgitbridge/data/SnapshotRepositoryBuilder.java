package uk.ac.ic.wlgitbridge.data;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import uk.ac.ic.wlgitbridge.bridge.WLBridgedProject;
import uk.ac.ic.wlgitbridge.bridge.BridgeAPI;
import uk.ac.ic.wlgitbridge.util.Util;
import uk.ac.ic.wlgitbridge.snapshot.push.exception.InternalErrorException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Winston on 03/11/14.
 */
public class SnapshotRepositoryBuilder {

    private final BridgeAPI bridgeAPI;

    public SnapshotRepositoryBuilder(BridgeAPI bridgeAPI) {
        this.bridgeAPI = bridgeAPI;
    }

    public Repository getRepositoryWithNameAtRootDirectory(String name, File rootDirectory) throws RepositoryNotFoundException, ServiceMayNotContinueException {
        if (!bridgeAPI.repositoryExists(name)) {
            throw new RepositoryNotFoundException(name);
        }
        File repositoryDirectory = new File(rootDirectory, name);

        Repository repository = null;
        try {
            repository = new FileRepositoryBuilder().setWorkTree(repositoryDirectory).build();
            new WLBridgedProject(repository, name, bridgeAPI).buildRepository();
        } catch (IOException e) {
            Util.printStackTrace(e);
            throw new ServiceMayNotContinueException(new InternalErrorException().getDescriptionLines().get(0));
        }
        return repository;
    }

}