package com.company.project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "share_links")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShareLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_link_id", nullable = false, unique = true)
    private Long id;

    private String shareLink;

    private boolean isActive;



    public ShareLink(String link, boolean isActive) {
        this.shareLink = link;
        this.isActive = isActive;
    }

    public ShareLink(String link) {
        this(link, true);
    }
}
