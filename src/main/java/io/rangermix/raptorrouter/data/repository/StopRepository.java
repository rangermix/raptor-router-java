package io.rangermix.raptorrouter.data.repository;

import io.rangermix.raptorrouter.data.model.Stop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends MongoRepository<Stop, String> {
}
