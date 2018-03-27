package com.github.bkhablenko.secrets.management.config;

import com.github.bkhablenko.secrets.management.domain.repository.CredentialsRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.vault.config.EnvironmentVaultConfiguration;
import org.springframework.vault.repository.configuration.EnableVaultRepositories;

@Configuration
@Import(EnvironmentVaultConfiguration.class)
@EnableVaultRepositories(basePackageClasses = CredentialsRepository.class)
public class VaultConfig {
}
