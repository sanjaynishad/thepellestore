package com.java.pellestore.service.impl;

import com.java.pellestore.service.MenuService;
import com.java.pellestore.domain.Menu;
import com.java.pellestore.repository.MenuRepository;
import com.java.pellestore.repository.search.MenuSearchRepository;
import com.java.pellestore.service.dto.MenuDTO;
import com.java.pellestore.service.mapper.MenuMapper;
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
 * Service Implementation for managing Menu.
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService{

    private final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);
    
    private final MenuRepository menuRepository;

    private final MenuMapper menuMapper;

    private final MenuSearchRepository menuSearchRepository;

    public MenuServiceImpl(MenuRepository menuRepository, MenuMapper menuMapper, MenuSearchRepository menuSearchRepository) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
        this.menuSearchRepository = menuSearchRepository;
    }

    /**
     * Save a menu.
     *
     * @param menuDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MenuDTO save(MenuDTO menuDTO) {
        log.debug("Request to save Menu : {}", menuDTO);
        Menu menu = menuMapper.menuDTOToMenu(menuDTO);
        menu = menuRepository.save(menu);
        MenuDTO result = menuMapper.menuToMenuDTO(menu);
        menuSearchRepository.save(menu);
        return result;
    }

    /**
     *  Get all the menus.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> findAll() {
        log.debug("Request to get all Menus");
        List<MenuDTO> result = menuRepository.findAll().stream()
            .map(menuMapper::menuToMenuDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one menu by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MenuDTO findOne(Long id) {
        log.debug("Request to get Menu : {}", id);
        Menu menu = menuRepository.findOne(id);
        MenuDTO menuDTO = menuMapper.menuToMenuDTO(menu);
        return menuDTO;
    }

    /**
     *  Delete the  menu by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Menu : {}", id);
        menuRepository.delete(id);
        menuSearchRepository.delete(id);
    }

    /**
     * Search for the menu corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> search(String query) {
        log.debug("Request to search Menus for query {}", query);
        return StreamSupport
            .stream(menuSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(menuMapper::menuToMenuDTO)
            .collect(Collectors.toList());
    }
}
