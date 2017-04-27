package com.java.pellestore.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Country entity.
 */
public class CountryDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String country_name;

    @NotNull
    private String country_code;

    @NotNull
    private String region_code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CountryDTO countryDTO = (CountryDTO) o;

        if ( ! Objects.equals(id, countryDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CountryDTO{" +
            "id=" + id +
            ", country_name='" + country_name + "'" +
            ", country_code='" + country_code + "'" +
            ", region_code='" + region_code + "'" +
            '}';
    }
}
