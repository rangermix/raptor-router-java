package io.rangermix.raptorrouter.data.repository;

import io.rangermix.raptorrouter.data.model.RoutePattern;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutePatternRepository extends MongoRepository<RoutePattern, String> {
}
