package com.immomio.osb.specification.request.apiversion;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiVersionTest {

    private static Stream<Arguments> provideApiVersions() {
        try {
            ApiVersion apiVersion = new ApiVersion("2.15");
            return Stream.of(
                    Arguments.of(apiVersion, new ApiVersion("2.15"), 0),
                    Arguments.of(apiVersion, new ApiVersion("2.10"), 1),
                    Arguments.of(apiVersion, new ApiVersion("2.20"), -1),
                    Arguments.of(apiVersion, new ApiVersion("1.15"), 1),
                    Arguments.of(apiVersion, new ApiVersion("1.10"), 1),
                    Arguments.of(apiVersion, new ApiVersion("1.20"), 1),
                    Arguments.of(apiVersion, new ApiVersion("3.15"), -1),
                    Arguments.of(apiVersion, new ApiVersion("3.10"), -1),
                    Arguments.of(apiVersion, new ApiVersion("3.20"), -1)
            );
        } catch (ApiFormatException e) {
            e.printStackTrace();
        }

        return null;
    }

    @ParameterizedTest
    @MethodSource("provideApiVersions")
    void compareTo(ApiVersion thisApi, ApiVersion anotherApi, int comparisonResult) {
        assertEquals(thisApi.compareTo(anotherApi), comparisonResult);
    }
}