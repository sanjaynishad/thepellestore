package com.java.pellestore.repository.search;

import com.java.pellestore.domain.Province;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Province entity.
 */
public interface ProvinceSearchRepository extends ElasticsearchRepository<Province, Long> {
}
