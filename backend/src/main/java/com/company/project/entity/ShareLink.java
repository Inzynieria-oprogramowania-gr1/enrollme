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

    @Enumerated(EnumType.ORDINAL)
    private EnrolmentState state;


    public ShareLink(String link, EnrolmentState state) {
        this.shareLink = link;
        this.state = state;
    }

    public ShareLink(String link) {
        this(link, EnrolmentState.ACTIVE);
    }
}
