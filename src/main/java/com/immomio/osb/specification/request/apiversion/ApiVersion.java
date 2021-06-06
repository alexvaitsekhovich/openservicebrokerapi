package com.immomio.osb.specification.request.apiversion;

import lombok.Getter;

@Getter
public class ApiVersion implements Comparable<ApiVersion> {

    private final int major;
    private final int minor;

    public ApiVersion(String version) throws ApiFormatException {
        String[] versionParts = version.split("\\.");

        if (versionParts.length != 2) {
            throw new ApiFormatException();
        }

        try {
            major = Integer.parseInt(versionParts[0]);
            minor = Integer.parseInt(versionParts[1]);
        } catch (NumberFormatException ex) {
            throw new ApiFormatException();
        }
    }

    @Override
    public int compareTo(ApiVersion otherVersion) {
        if (major == otherVersion.getMajor()) {
            return Integer.compare(minor, otherVersion.getMinor());
        }

        return Integer.compare(major, otherVersion.getMajor());
    }

    @Override
    public String toString() {
        return major + "." + minor;
    }
}
