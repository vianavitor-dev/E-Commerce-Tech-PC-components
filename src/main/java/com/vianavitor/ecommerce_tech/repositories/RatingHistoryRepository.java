package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.RatingHistory;
import com.vianavitor.ecommerce_tech.models.aux.RatingHistoryId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingHistoryRepository extends CrudRepository<RatingHistory, RatingHistoryId> {
}
