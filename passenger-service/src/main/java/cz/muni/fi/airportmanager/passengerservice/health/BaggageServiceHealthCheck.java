package cz.muni.fi.airportmanager.passengerservice.health;

import cz.muni.fi.airportmanager.passengerservice.client.BaggageClientResource;
import io.smallrye.health.api.AsyncHealthCheck;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Readiness
@ApplicationScoped
public class BaggageServiceHealthCheck implements AsyncHealthCheck {

    @RestClient
    BaggageClientResource baggageClientResource;

    @Override
    public Uni<HealthCheckResponse> call() {
        // TODO call the baggage service readinessCheck and see if response.getStatus() is HealthCheckResponse.Status.UP. decide accordingly
        // TODO don't forget to handle the case when the call fails
        return baggageClientResource.readinessCheck()
                .onItem().transform(response -> {
                    if (response.getStatus() == HealthCheckResponse.Status.DOWN) {
                        return HealthCheckResponse.down("Baggage service is not ready");
                    } else {
                        return HealthCheckResponse.up("Baggage service is ready");
                    }
                })
                .onFailure().recoverWithItem(HealthCheckResponse.down("Baggage service is not ready"));
    }
}