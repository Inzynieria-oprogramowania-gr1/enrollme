package com.company.project.mapper;

import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.entity.ShareLink;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShareLinkMapper {
    @Mapping(source = "link", target = "shareLink")
    @Mapping(source = "state", target = "state")
    ShareLink mapToShareLink(ShareLinkDto shareLinkDto);

    @InheritInverseConfiguration
    ShareLinkDto mapToShareLinkDto(ShareLink shareLink);
}
