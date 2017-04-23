package com.java.pellestore.web.rest;

import com.java.pellestore.ThepellestoreApp;

import com.java.pellestore.domain.Menu;
import com.java.pellestore.repository.MenuRepository;
import com.java.pellestore.service.MenuService;
import com.java.pellestore.repository.search.MenuSearchRepository;
import com.java.pellestore.service.dto.MenuDTO;
import com.java.pellestore.service.mapper.MenuMapper;
import com.java.pellestore.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MenuResource REST controller.
 *
 * @see MenuResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThepellestoreApp.class)
public class MenuResourceIntTest {

    private static final String DEFAULT_MENU_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MENU_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PARENT_ID = 1;
    private static final Integer UPDATED_PARENT_ID = 2;

    private static final Boolean DEFAULT_ACTIVE_FLAG = false;
    private static final Boolean UPDATED_ACTIVE_FLAG = true;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuSearchRepository menuSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMenuMockMvc;

    private Menu menu;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MenuResource menuResource = new MenuResource(menuService);
        this.restMenuMockMvc = MockMvcBuilders.standaloneSetup(menuResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createEntity(EntityManager em) {
        Menu menu = new Menu()
            .menu_name(DEFAULT_MENU_NAME)
            .description(DEFAULT_DESCRIPTION)
            .state_name(DEFAULT_STATE_NAME)
            .parent_id(DEFAULT_PARENT_ID)
            .active_flag(DEFAULT_ACTIVE_FLAG);
        return menu;
    }

    @Before
    public void initTest() {
        menuSearchRepository.deleteAll();
        menu = createEntity(em);
    }

    @Test
    @Transactional
    public void createMenu() throws Exception {
        int databaseSizeBeforeCreate = menuRepository.findAll().size();

        // Create the Menu
        MenuDTO menuDTO = menuMapper.menuToMenuDTO(menu);
        restMenuMockMvc.perform(post("/api/menus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuDTO)))
            .andExpect(status().isCreated());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate + 1);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getMenu_name()).isEqualTo(DEFAULT_MENU_NAME);
        assertThat(testMenu.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMenu.getState_name()).isEqualTo(DEFAULT_STATE_NAME);
        assertThat(testMenu.getParent_id()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testMenu.isActive_flag()).isEqualTo(DEFAULT_ACTIVE_FLAG);

        // Validate the Menu in Elasticsearch
        Menu menuEs = menuSearchRepository.findOne(testMenu.getId());
        assertThat(menuEs).isEqualToComparingFieldByField(testMenu);
    }

    @Test
    @Transactional
    public void createMenuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = menuRepository.findAll().size();

        // Create the Menu with an existing ID
        menu.setId(1L);
        MenuDTO menuDTO = menuMapper.menuToMenuDTO(menu);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuMockMvc.perform(post("/api/menus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMenus() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList
        restMenuMockMvc.perform(get("/api/menus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menu.getId().intValue())))
            .andExpect(jsonPath("$.[*].menu_name").value(hasItem(DEFAULT_MENU_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].state_name").value(hasItem(DEFAULT_STATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].parent_id").value(hasItem(DEFAULT_PARENT_ID)))
            .andExpect(jsonPath("$.[*].active_flag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())));
    }

    @Test
    @Transactional
    public void getMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get the menu
        restMenuMockMvc.perform(get("/api/menus/{id}", menu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(menu.getId().intValue()))
            .andExpect(jsonPath("$.menu_name").value(DEFAULT_MENU_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.state_name").value(DEFAULT_STATE_NAME.toString()))
            .andExpect(jsonPath("$.parent_id").value(DEFAULT_PARENT_ID))
            .andExpect(jsonPath("$.active_flag").value(DEFAULT_ACTIVE_FLAG.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMenu() throws Exception {
        // Get the menu
        restMenuMockMvc.perform(get("/api/menus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);
        menuSearchRepository.save(menu);
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu
        Menu updatedMenu = menuRepository.findOne(menu.getId());
        updatedMenu
            .menu_name(UPDATED_MENU_NAME)
            .description(UPDATED_DESCRIPTION)
            .state_name(UPDATED_STATE_NAME)
            .parent_id(UPDATED_PARENT_ID)
            .active_flag(UPDATED_ACTIVE_FLAG);
        MenuDTO menuDTO = menuMapper.menuToMenuDTO(updatedMenu);

        restMenuMockMvc.perform(put("/api/menus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuDTO)))
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getMenu_name()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testMenu.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMenu.getState_name()).isEqualTo(UPDATED_STATE_NAME);
        assertThat(testMenu.getParent_id()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testMenu.isActive_flag()).isEqualTo(UPDATED_ACTIVE_FLAG);

        // Validate the Menu in Elasticsearch
        Menu menuEs = menuSearchRepository.findOne(testMenu.getId());
        assertThat(menuEs).isEqualToComparingFieldByField(testMenu);
    }

    @Test
    @Transactional
    public void updateNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Create the Menu
        MenuDTO menuDTO = menuMapper.menuToMenuDTO(menu);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMenuMockMvc.perform(put("/api/menus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(menuDTO)))
            .andExpect(status().isCreated());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);
        menuSearchRepository.save(menu);
        int databaseSizeBeforeDelete = menuRepository.findAll().size();

        // Get the menu
        restMenuMockMvc.perform(delete("/api/menus/{id}", menu.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean menuExistsInEs = menuSearchRepository.exists(menu.getId());
        assertThat(menuExistsInEs).isFalse();

        // Validate the database is empty
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);
        menuSearchRepository.save(menu);

        // Search the menu
        restMenuMockMvc.perform(get("/api/_search/menus?query=id:" + menu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menu.getId().intValue())))
            .andExpect(jsonPath("$.[*].menu_name").value(hasItem(DEFAULT_MENU_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].state_name").value(hasItem(DEFAULT_STATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].parent_id").value(hasItem(DEFAULT_PARENT_ID)))
            .andExpect(jsonPath("$.[*].active_flag").value(hasItem(DEFAULT_ACTIVE_FLAG.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Menu.class);
    }
}
