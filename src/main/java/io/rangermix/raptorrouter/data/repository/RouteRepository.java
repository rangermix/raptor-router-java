package io.rangermix.raptorrouter.data.repository;

import io.rangermix.raptorrouter.data.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends MongoRepository<Route, String> {
}
