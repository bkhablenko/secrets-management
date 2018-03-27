package com.github.bkhablenko.secrets.management.domain.repository;

import com.github.bkhablenko.secrets.management.domain.Credentials;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepository extends KeyValueRepository<Credentials, String> {
}
