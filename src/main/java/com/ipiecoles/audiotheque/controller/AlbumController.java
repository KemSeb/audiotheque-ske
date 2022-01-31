package com.ipiecoles.audiotheque.controller;

import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.repository.AlbumRepository;
import com.ipiecoles.audiotheque.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/albums")
public class AlbumController {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AlbumRepository albumRepository;

    //Ajoute un album en base
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Album addAlbum(
            @RequestBody Album album
    ) {
        // Si Artiste inconnu
        if (!artistRepository.existsById(album.getArtist().getId())) {
            throw new EntityNotFoundException("L'artiste " + album.getArtist().getName() + " n'est pas présent dans cette base");
        }
        //  Si l'album est déjà présent en base pour cet artiste
        if (albumRepository.existsByArtist(album.getArtist()) && albumRepository.existsByTitle(album.getTitle())) {
            throw new EntityExistsException("L'album " + album.getTitle() +
                    " existe déjà pour l'artiste " + album.getArtist().getName());
        }
        albumRepository.save(album);
        return album;
    }

    //Suppression d'un album
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(
            @PathVariable Long id
    ) {
        //  Si l'album est inexistant
        if (!albumRepository.existsById(id)) {
            throw new EntityNotFoundException("L'album avec l'identifiant " + id + " n'existe pas.");
        }
        albumRepository.deleteById(id);
    }
}
