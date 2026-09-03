package com.vianavitor.ecommerce_tech.repositories.aux;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface ReadOnlyInterface<T, ID> extends Repository<T, ID> {

    List<T> findAll();

    boolean existsById(ID id);

    Optional<T> findById(ID id);
}
