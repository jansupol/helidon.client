package org.example;

import io.helidon.jersey.connector.HelidonConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

public class ConnectorTest extends JerseyTest {

    @Path("basic")
    public static class BasicResource {
        @Path("getquery")
        @GET
        public String getQuery(@QueryParam("first") String first, @QueryParam("second") String second) {
            return first + second;
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(BasicResource.class)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
    }

    @Override
    protected void configureClient(ClientConfig config) {
        super.configureClient(config);
        config.connectorProvider(new HelidonConnectorProvider());
    }

    @Test
    public void queryGetTest() {
        try (Response response = target("basic").path("getquery")
                .queryParam("first", "hello")
                .queryParam("second", "world")
                .request().get()) {
            Assert.assertEquals(200, response.getStatus());
            Assert.assertEquals("helloworld", response.readEntity(String.class));
        }
    }
}
