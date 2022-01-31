package com.ipiecoles.audiotheque.repository;

import com.ipiecoles.audiotheque.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Boolean existsByNameIgnoreCase(String name);

    Page<Artist> findByNameIsContainingIgnoreCase(String name, Pageable pageable);
}