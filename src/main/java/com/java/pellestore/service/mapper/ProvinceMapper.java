package com.java.pellestore.service.mapper;

import com.java.pellestore.domain.*;
import com.java.pellestore.service.dto.ProvinceDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Province and its DTO ProvinceDTO.
 */
@Mapper(componentModel = "spring", uses = {CountryMapper.class, })
public interface ProvinceMapper {

    @Mapping(source = "country.id", target = "countryId")
    ProvinceDTO provinceToProvinceDTO(Province province);

    List<ProvinceDTO> provincesToProvinceDTOs(List<Province> provinces);

    @Mapping(source = "countryId", target = "country")
    Province provinceDTOToProvince(ProvinceDTO provinceDTO);

    List<Province> provinceDTOsToProvinces(List<ProvinceDTO> provinceDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Province provinceFromId(Long id) {
        if (id == null) {
            return null;
        }
        Province province = new Province();
        province.setId(id);
        return province;
    }
    

}
