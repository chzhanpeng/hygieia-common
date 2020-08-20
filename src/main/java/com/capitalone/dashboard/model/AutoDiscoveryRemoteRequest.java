package com.capitalone.dashboard.model;

import java.util.List;

public class AutoDiscoveryRemoteRequest extends AutoDiscovery {

    private String autoDiscoveryId;

    public AutoDiscoveryRemoteRequest() {}

    public AutoDiscoveryRemoteRequest(AutoDiscoveryMetaData metaData, List<AutoDiscoveredEntry> codeRepoEntries,
                                      List<AutoDiscoveredEntry> buildEntries, List<AutoDiscoveredEntry> securityScanEntries,
                                      List<AutoDiscoveredEntry> deploymentEntries, List<AutoDiscoveredEntry> libraryScanEntries,
                                      List<AutoDiscoveredEntry> functionalTestEntries, List<AutoDiscoveredEntry> artifactEntries,
                                      List<AutoDiscoveredEntry> staticCodeEntries, List<AutoDiscoveredEntry> featureEntries,
                                      List<AutoDiscoveredEntry> performanceTestEntries, String autoDiscoveryId) {
        super(metaData, codeRepoEntries, buildEntries, securityScanEntries, deploymentEntries, libraryScanEntries,
                functionalTestEntries, artifactEntries, staticCodeEntries, featureEntries,performanceTestEntries);
        setAutoDiscoveryId(autoDiscoveryId);
    }

    public String getAutoDiscoveryId() { return autoDiscoveryId; }

    public void setAutoDiscoveryId(String autoDiscoveryId) { this.autoDiscoveryId = autoDiscoveryId; }

}
