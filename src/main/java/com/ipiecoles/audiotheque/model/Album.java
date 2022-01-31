package com.ipiecoles.audiotheque.model;

import javax.persistence.*;

@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


    @OneToOne
    @JoinColumn(name = "artistId")
    private Artist artist;

    public Long getId() {
        return id;
    }

    //GETTER AND SETTER

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}

