package io.rangermix.raptorrouter.data.repository;

import io.rangermix.raptorrouter.data.model.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends MongoRepository<Service, String> {
}
