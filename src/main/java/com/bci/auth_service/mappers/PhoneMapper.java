package com.bci.auth_service.mappers;

import com.bci.auth_service.controllers.login.dtos.PhoneResponse;
import com.bci.auth_service.entities.PhoneEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    @Mapping(source = "number", target = "number")
    @Mapping(source = "cityCode", target = "citycode")
    @Mapping(source = "countryCode", target = "contrycode")
    PhoneResponse phoneEntityToPhoneResponse(PhoneEntity phoneEntity);

}
