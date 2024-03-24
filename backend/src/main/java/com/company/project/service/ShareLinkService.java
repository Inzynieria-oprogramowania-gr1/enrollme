package com.company.project.service;

import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.entity.EnrolmentState;
import com.company.project.entity.ShareLink;
import com.company.project.exception.implementations.ConflictException;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.mapper.ShareLinkMapper;
import com.company.project.repository.ActiveLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShareLinkService {
    private final ActiveLinkRepository activeLinkRepository;
    private final ShareLinkMapper shareLinkMapper;


    public ShareLinkDto createShareLink() {
        if (activeLinkRepository
                .findAll()
                .stream()
                .findFirst()
                .isPresent()) {
            throw new ConflictException("Link already created");
        }
        ShareLink savedLink = activeLinkRepository.save(new ShareLink("/students/timetable"));
        return shareLinkMapper.mapToShareLinkDto(savedLink);
    }

    public ShareLinkDto updateShareLink(EnrolmentState state) {
        ShareLink link = activeLinkRepository
                .findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Share link not created"));
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
                .map(shareLinkMapper::mapToShareLinkDto
                );
    }
}
