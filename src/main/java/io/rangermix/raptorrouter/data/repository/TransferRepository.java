package io.rangermix.raptorrouter.data.repository;

import io.rangermix.raptorrouter.data.model.Transfer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends MongoRepository<Transfer, String> {
}
