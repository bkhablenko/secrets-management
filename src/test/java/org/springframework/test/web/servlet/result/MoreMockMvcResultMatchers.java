package org.springframework.test.web.servlet.result;

public class MoreMockMvcResultMatchers extends MockMvcResultMatchers {

    private MoreMockMvcResultMatchers() {
        throw new UnsupportedOperationException();
    }

    public static LocationHeaderResultMatchers locationHeader() {
        return new LocationHeaderResultMatchers();
    }
}
