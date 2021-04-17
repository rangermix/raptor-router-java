package io.rangermix.raptorrouter.data.repository;

import io.rangermix.raptorrouter.data.model.Agency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyRepository extends MongoRepository<Agency, String> {
}
