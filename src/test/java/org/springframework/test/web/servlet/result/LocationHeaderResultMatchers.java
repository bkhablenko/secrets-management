package org.springframework.test.web.servlet.result;

import org.hamcrest.Matchers;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

public class LocationHeaderResultMatchers {

    LocationHeaderResultMatchers() {
    }

    public ResultMatcher endsWith(String suffix) {
        return result -> header().string(HttpHeaders.LOCATION, Matchers.endsWith(suffix)).match(result);
    }
}
