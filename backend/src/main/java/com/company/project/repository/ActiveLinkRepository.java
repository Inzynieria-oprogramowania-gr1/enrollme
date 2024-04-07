package com.company.project.repository;

import com.company.project.entity.ShareLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveLinkRepository extends JpaRepository<ShareLink, Long> {
}
