package com.ipiecoles.audiotheque.repository;

import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    Boolean existsByArtist(Artist artist);

    Boolean existsByTitle(String title);

    List<Album> findAllByArtistId(Long id);
}