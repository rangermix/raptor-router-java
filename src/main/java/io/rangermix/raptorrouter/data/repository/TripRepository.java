package io.rangermix.raptorrouter.data.repository;

import io.rangermix.raptorrouter.data.model.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends MongoRepository<Trip, String> {
}
