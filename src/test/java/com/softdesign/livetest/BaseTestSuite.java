package com.softdesign.livetest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.mockito.MockitoAnnotations.openMocks;

public class BaseTestSuite {

    protected AutoCloseable closeable;

    @BeforeEach
    public void init() { closeable = openMocks(this);}

    @BeforeAll
    public static void setUp() {
        loadTemplates("com.softdesign.livetest.fixture");
    }

    @AfterEach
    public void closeService() throws Exception {
        closeable.close();
    }
}
