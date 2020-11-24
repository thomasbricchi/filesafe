package com.thomas.filesafe.service;

import com.thomas.filesafe.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentsRepository extends JpaRepository<Attachment, Long> {
}
