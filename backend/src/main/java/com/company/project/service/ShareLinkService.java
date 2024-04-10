package com.company.project.service;

import com.company.project.algorithm.GroupingAlgorithm;
import com.company.project.dto.StudentDto;
import com.company.project.dto.timetable.ShareLinkDto;
import com.company.project.dto.timetable.TimetableDto;
import com.company.project.entity.EnrolmentState;
import com.company.project.entity.ShareLink;
import com.company.project.exception.implementations.ConflictException;
import com.company.project.exception.implementations.ForbiddenActionException;
import com.company.project.exception.implementations.ResourceNotFoundException;
import com.company.project.mapper.ShareLinkMapper;
import com.company.project.repository.ActiveLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShareLinkService {
    private final ActiveLinkRepository activeLinkRepository;
    private final ShareLinkMapper shareLinkMapper;
    private final StudentService studentService;


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

    public ShareLinkDto updateShareLink(EnrolmentState state, EnrollmentService enrollmentService) {

        if (state == EnrolmentState.RESULTS_READY) {
            throw new ForbiddenActionException("Cannot change state to - " + state);
        }


        ShareLink link = activeLinkRepository
                .findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Share link not created"));
        if (link.getState().equals(EnrolmentState.CALCULATING) ||
                link.getState().equals(EnrolmentState.RESULTS_READY)) {
            throw new ConflictException("Cannot change state of link");
        }
        link.setState(state);
        if (state.equals(EnrolmentState.CALCULATING)) {
            GroupingAlgorithm algorithm = new GroupingAlgorithm(studentService, enrollmentService);
            Map<TimetableDto, List<StudentDto>> tmp = algorithm.groupStudents();
            studentService.setResults(tmp);
            link.setState(EnrolmentState.RESULTS_READY);
        }
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

    public void removeAll(){
        activeLinkRepository.deleteAll();
    }
}
