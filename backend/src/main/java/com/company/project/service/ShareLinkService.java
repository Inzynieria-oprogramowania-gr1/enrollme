package com.company.project.service;

import java.io.IOException;
import java.io.NotActiveException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.entity.EnrolmentState;
import com.company.project.entity.ShareLink;
import com.company.project.exception.implementations.ConflictException;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.mapper.ShareLinkMapper;
import com.company.project.repository.ActiveLinkRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ShareLinkService {
    private final ActiveLinkRepository activeLinkRepository;
    private final ShareLinkMapper shareLinkMapper;


    public ShareLinkDto createShareLink(HttpServletRequest request) throws URISyntaxException {
        if(activeLinkRepository
        .findAll()
        .stream()
        .findFirst()
        .isPresent()){
            throw new ConflictException("Link already created");
        }
        String host = request.getRequestURL().toString();
        String link = host.substring(0, host.lastIndexOf(new URI(host).getPath()));

        ShareLink savedLink = activeLinkRepository.save(new ShareLink(link + "/students/timetable"));
        return shareLinkMapper.mapToShareLinkDto(savedLink);
    }

    public ShareLinkDto updateShareLink(EnrolmentState state){
        ShareLink link = activeLinkRepository
        .findAll()
        .stream()
        .findFirst()
        .orElseThrow(()-> new ResourceNotFoundException("Share link not created"));
        link.setState(state);
        activeLinkRepository.save(link);
        return shareLinkMapper.mapToShareLinkDto(link);
    }

    public Optional<ShareLinkDto> getShareLink() {
        // Change to findByTimetableUUID if sessions are present
        return activeLinkRepository
                .findAll()
                .stream()
                .findFirst()
                .map(e -> 
                    shareLinkMapper.mapToShareLinkDto(e)
                );
    }
}
