package com.company.project.repository;

import com.company.project.entity.ShareLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActiveLinkRepository extends JpaRepository<ShareLink, Long> {
}
