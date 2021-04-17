package io.rangermix.raptorrouter.data.repository;

import io.rangermix.raptorrouter.data.model.StopTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopTimeRepository extends MongoRepository<StopTime, String> {
}
