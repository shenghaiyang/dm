package com.shenghaiyang.dm.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by shenghaiyang on 2016/7/25.
 */
@Root(name = "metadata", strict = false)
public class Dependency {

    @Element(name = "groupId")
    private String groupId;
    @Element(name = "artifactId")
    private String artifactId;
    @Element(name = "version")
    private String version;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "compile '" +
                groupId +
                ':' +
                artifactId +
                ':' +
                version + "\'";
    }
}
