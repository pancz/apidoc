package com.zhaoyun.docmanager.core.apiparse.entity;

/**
 * Created by user on 2016/8/9 9:31.
 * 未来不确定才更值得前行
 */
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;

    public Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String toPom() {
        String pom = "<dependency>\n" +
                "    <groupId>%s</groupId>\n" +
                "    <artifactId>%s</artifactId>\n" +
                "\t<version>%s</version>\n" +
                "</dependency>";
        return String.format(pom, this.groupId, this.artifactId, this.version);
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

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
}
