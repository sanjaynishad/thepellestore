package com.java.pellestore.service.impl;

import com.java.pellestore.service.ProvinceService;
import com.java.pellestore.domain.Province;
import com.java.pellestore.repository.ProvinceRepository;
import com.java.pellestore.repository.search.ProvinceSearchRepository;
import com.java.pellestore.service.dto.ProvinceDTO;
import com.java.pellestore.service.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Province.
 */
@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService{

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);
    
    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    private final ProvinceSearchRepository provinceSearchRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper, ProvinceSearchRepository provinceSearchRepository) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
        this.provinceSearchRepository = provinceSearchRepository;
    }

    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        Province province = provinceMapper.provinceDTOToProvince(provinceDTO);
        province = provinceRepository.save(province);
        ProvinceDTO result = provinceMapper.provinceToProvinceDTO(province);
        provinceSearchRepository.save(province);
        return result;
    }

    /**
     *  Get all the provinces.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProvinceDTO> findAll() {
        log.debug("Request to get all Provinces");
        List<ProvinceDTO> result = provinceRepository.findAll().stream()
            .map(provinceMapper::provinceToProvinceDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one province by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProvinceDTO findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        Province province = provinceRepository.findOne(id);
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);
        return provinceDTO;
    }

    /**
     *  Delete the  province by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.delete(id);
        provinceSearchRepository.delete(id);
    }

    /**
     * Search for the province corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProvinceDTO> search(String query) {
        log.debug("Request to search Provinces for query {}", query);
        return StreamSupport
            .stream(provinceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(provinceMapper::provinceToProvinceDTO)
            .collect(Collectors.toList());
    }
}
