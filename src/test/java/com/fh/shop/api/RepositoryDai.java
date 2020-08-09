package com.fh.shop.api;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryDai extends MongoRepository<User,String> {
}
