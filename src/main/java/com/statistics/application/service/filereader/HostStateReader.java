package com.statistics.application.service.filereader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.statistics.domain.CloudInstance;
import com.statistics.domain.Host;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

public class HostStateReader implements FileReaderBase {

    private static final String HOST_STATE_LINE_SEPARATOR = ",";
    private static final int HOST_LINE_LENGTH = 3;

    private Path path;
    private List<CloudInstance> cloudInstances;

    public HostStateReader(Path path, List<CloudInstance> cloudInstances) {
        this.path = path;
        this.cloudInstances = cloudInstances;
    }

    @Override
    public ImmutableList<Host> process() {
        return getCloudHosts();
    }

    private ImmutableList<Host> getCloudHosts() {
        try {
            return FluentIterable.from(Files.readAllLines(this.path))
                    .transform(line -> createCloudHost(line, this.cloudInstances))
                    .toList();
        } catch (IOException e) {
            String message = "error reading file: " + this.path + " ";
            System.out.println(message + e);
            throw new HostStateReaderException(message, e);

        }
    }

    private Host createCloudHost(String line, List<CloudInstance> cloudInstances) {

        String[] cloudHost = line.split(HOST_STATE_LINE_SEPARATOR);


        if (cloudHost.length != HOST_LINE_LENGTH
                ) {
            String message = String.format("error in file: %s line: %s is not formatted correctly", path, line);
            throw new HostStateReaderException(message);
        }

        String id = cloudHost[0];
        String numberOfSlots = cloudHost[1];
        String dataCentreId = cloudHost[2];

        List<CloudInstance> cloudInstancesByHostId = findCloudInstancesByHostId(id, cloudInstances);

        return Host.from(id, Integer.valueOf(numberOfSlots), dataCentreId, cloudInstancesByHostId);
    }

    private List<CloudInstance> findCloudInstancesByHostId(String hostId, List<CloudInstance> cloudInstances) {
        return FluentIterable.from(cloudInstances).filter(cloudInstance -> {
            return cloudInstance.getHostId().equals(hostId);
        }).toList();
    }
}
