package com.company.project.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.entity.ShareLink;

@Mapper(componentModel = "spring")
public interface ShareLinkMapper {
    @Mapping(source = "link", target = "shareLink")
    ShareLink mapToShareLink(ShareLinkDto shareLinkDto);
    
    @InheritInverseConfiguration
    ShareLinkDto mapToShareLinkDto(ShareLink shareLink);
}
