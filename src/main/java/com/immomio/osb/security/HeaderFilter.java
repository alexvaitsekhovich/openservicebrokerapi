package com.immomio.osb.security;

import com.immomio.osb.specification.request.apiversion.ApiFormatException;
import com.immomio.osb.specification.request.apiversion.ApiVersion;
import com.immomio.osb.specification.request.http.OsbHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String apiVersionHeader = request.getHeader(OsbHeaders.API_VERSION_HEADER);

        if (apiVersionHeader == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        ApiVersion providedApiVersion;

        try {
            providedApiVersion = new ApiVersion(apiVersionHeader);
        } catch (ApiFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (providedApiVersion.compareTo(ApiVersionConfig.MIN_API_VERSION) < 0) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            response.getOutputStream().println("{\"error\":\"Minimal required API version: " + ApiVersionConfig.MIN_API_VERSION + "\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}
