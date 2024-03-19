package edu.java.domain.jpa.dao;

import edu.java.domain.jpa.model.StackOverflowLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStackOverflowRepository extends JpaRepository<StackOverflowLink, Long> {
}
