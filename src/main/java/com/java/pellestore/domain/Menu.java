package com.java.pellestore.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Menu.
 */
@Entity
@Table(name = "menu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "menu")
public class Menu extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_name")
    private String menu_name;

    @Column(name = "description")
    private String description;

    @Column(name = "state_name")
    private String state_name;

    @Column(name = "parent_id")
    private Integer parent_id;

    @Column(name = "active_flag")
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

    public Menu menu_name(String menu_name) {
        this.menu_name = menu_name;
        return this;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getDescription() {
        return description;
    }

    public Menu description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState_name() {
        return state_name;
    }

    public Menu state_name(String state_name) {
        this.state_name = state_name;
        return this;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public Menu parent_id(Integer parent_id) {
        this.parent_id = parent_id;
        return this;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public Boolean isActive_flag() {
        return active_flag;
    }

    public Menu active_flag(Boolean active_flag) {
        this.active_flag = active_flag;
        return this;
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
        Menu menu = (Menu) o;
        if (menu.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Menu{" +
            "id=" + id +
            ", menu_name='" + menu_name + "'" +
            ", description='" + description + "'" +
            ", state_name='" + state_name + "'" +
            ", parent_id='" + parent_id + "'" +
            ", active_flag='" + active_flag + "'" +
            '}';
    }
}
