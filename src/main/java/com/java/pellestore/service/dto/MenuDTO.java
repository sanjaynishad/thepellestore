package com.java.pellestore.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Menu entity.
 */
public class MenuDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String menu_name;

    private String description;

    private String state_name;

    private Integer parent_id;

    private Boolean active_flag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }
    public Boolean getActive_flag() {
        return active_flag;
    }

    public void setActive_flag(Boolean active_flag) {
        this.active_flag = active_flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MenuDTO menuDTO = (MenuDTO) o;

        if ( ! Objects.equals(id, menuDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MenuDTO{" +
            "id=" + id +
            ", menu_name='" + menu_name + "'" +
            ", description='" + description + "'" +
            ", state_name='" + state_name + "'" +
            ", parent_id='" + parent_id + "'" +
            ", active_flag='" + active_flag + "'" +
            '}';
    }
}
