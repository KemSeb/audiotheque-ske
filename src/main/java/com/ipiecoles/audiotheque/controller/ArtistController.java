package com.ipiecoles.audiotheque.controller;

import com.ipiecoles.audiotheque.exception.GlobalExceptionHandler;
import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import com.ipiecoles.audiotheque.repository.AlbumRepository;
import com.ipiecoles.audiotheque.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/artists")
public class ArtistController extends GlobalExceptionHandler {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AlbumRepository albumRepository;

    //  Permet de récupérer un artiste avec son iD
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public Artist getArtistById(@PathVariable Long id) {
        Optional<Artist> artist = artistRepository.findById(id);
        if (artist.isEmpty()) {
            throw new EntityNotFoundException("L'artiste avec l'identifiant " + id + " n'existe pas !");
        }
        return artist.get();
    }

    //Permet de récupérer une page d'artiste avec un nom partiel
    @RequestMapping(
            method = RequestMethod.GET,
            params = "name",
            produces = MediaType.APPLICATION_JSON_VALUE
    )


    public Page<Artist> getArtistByPartialNameIgnoreCase(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sortProperty", defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection
    ) {
        this.validatePageRequestArguments(page, size, sortProperty);
        PageRequest pageRequest = PageRequest.of(page, size, sortDirection, sortProperty);
        return artistRepository.findByNameIsContainingIgnoreCase(name, pageRequest);
    }

    //Permet de récupérer une page contenant 10 artistes, triés par ordre alphabétique
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public Page<Artist> getAllArtists(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sortProperty", defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection
    ) {
        this.validatePageRequestArguments(page, size, sortProperty);
        PageRequest pageRequest = PageRequest.of(page, size, sortDirection, sortProperty);
        return artistRepository.findAll(pageRequest);
    }


    // Ajoute un nouvel artiste dans en base
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Artist createArtist(
            @RequestBody Artist artist
    ) {
        if (artist.getId() != null && artistRepository.existsById(artist.getId()) || artistRepository.existsByNameIgnoreCase(artist.getName())) {
            throw new EntityExistsException("Cet artiste existe déjà dans la base.");
        }
        return artistRepository.save(artist);
    }

    //Mise à jour des informations d'un artiste grace a son ID
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Artist updateArtist(
            @PathVariable Long id,
            @RequestBody Artist artist
    ) {
        // Artiste avec id inconnu
        if (!artistRepository.existsById(id)) {
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'existe pas !");
        }
        // Valeurs nulles
        // Id de la requête != Id de l'artiste le corps de la requête
        if (artist.getName() == null || artist.getId() == null || !id.equals(artist.getId())) {
            throw new IllegalArgumentException(" La Requête est incorrecte");
        }
        artistRepository.save(artist);
        return artist;
    }

    //Suppression d'un artiste
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtist(
            @PathVariable Long id
    ) {
        // Artiste avec  un id inconnu
        if (!artistRepository.existsById(id)) {
            throw new EntityNotFoundException("L'artiste avec l'identifiant" + id + " n'a pas été trouvé !");
        }

        // Lorsque nous supprimons un artiste, nous supprimons également les albums qui lui sont associés.
        List<Album> albums = albumRepository.findAllByArtistId(id);
        albums.forEach(album -> albumRepository.delete(album));

        artistRepository.deleteById(id);
    }

    // Vérification des paramètres (PageRequest)
    private void validatePageRequestArguments(Integer page, Integer size, String sortProperty) {
        // Gestion des cas ou la Page et le size ont des valeurs négatives
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Le numéro de page ou le nombre d'éléments ne peut pas être inférieur à 0");
        }
        //  Gestion des cas ou le Size est supérieure à 25
        if (size > 25) {
            throw new IllegalArgumentException("La quantité d'artistes affichée ne peut pas être supérieure à 25.");
        }
        // sortPropertyName identifie la colonne triée
        if (Arrays.stream(Artist.class.getDeclaredFields())
                .map(Field::getName)
                .filter(s -> s.equals(sortProperty)).count() != 1) {
            throw new IllegalArgumentException("La propriété " + sortProperty + " n'existe pas.");
        }
        // Page inexistante
        Long nbrArtistes = artistRepository.count();
        if (size * page > nbrArtistes) {
            throw new IllegalArgumentException("La page demandée n'existe pas.");
        }
    }

}

