package com.java.pellestore.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Province.
 */
@Entity
@Table(name = "province")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "province")
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "province_name", nullable = false)
    private String province_name;

    @ManyToOne(optional = false)
    @NotNull
    private Country country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public Province province_name(String province_name) {
        this.province_name = province_name;
        return this;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public Country getCountry() {
        return country;
    }

    public Province country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Province province = (Province) o;
        if (province.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, province.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Province{" +
            "id=" + id +
            ", province_name='" + province_name + "'" +
            '}';
    }
}
