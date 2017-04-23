package com.java.pellestore.service.mapper;

import com.java.pellestore.domain.*;
import com.java.pellestore.service.dto.MenuDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Menu and its DTO MenuDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MenuMapper {

    MenuDTO menuToMenuDTO(Menu menu);

    List<MenuDTO> menusToMenuDTOs(List<Menu> menus);

    Menu menuDTOToMenu(MenuDTO menuDTO);

    List<Menu> menuDTOsToMenus(List<MenuDTO> menuDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Menu menuFromId(Long id) {
        if (id == null) {
            return null;
        }
        Menu menu = new Menu();
        menu.setId(id);
        return menu;
    }
    

}
