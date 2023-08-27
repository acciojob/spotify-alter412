package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User u = new User(name,mobile);
        for(User x: users){
            if(x.getMobile().equals(mobile)&&x.getName().equals(name)){
                return x;
            }
        }
        users.add(u);
        return u;
    }

    public Artist createArtist(String name) {
        Artist a = new Artist(name);
        for(Artist x:artists){
            if(x.getName().equals(name)){
                return x;
            }
        }
        artists.add(a);
        return a;
    }

    public Album createAlbum(String title, String artistName) {
        Album a = new Album(title);
        Artist t = createArtist(artistName);
        boolean isAlbumPresent = false;

        for(Album x: albums){
            if(x.getTitle().equals(title)){
                isAlbumPresent=true;
                a=x;
                break;
            }
        }
        if(!isAlbumPresent){
            albums.add(a);
        }

        if(artistAlbumMap.containsKey(t)){
            List<Album> temp = artistAlbumMap.get(t);
            boolean isP = false;
            for(Album x:temp){
                if(x.getTitle().equals(title)){
                    isP = true;
                    break;
                }
            }
            if(!isP){
                temp.add(a);
            }
        }else{
            List<Album> temp = new ArrayList<>();
            temp.add(a);
            artistAlbumMap.put(t,temp);
        }
        return a;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean isAlbumPresent = false;
        Album album=null;
        for(Album x:albums){
            if(x.getTitle().equals(albumName)){
                isAlbumPresent = true;
                album=x;
                break;
            }
        }
        if(!isAlbumPresent){
            throw new Exception("Album does not exist");
        }
        Song s = new Song(title,length);
        boolean isSongPresent = false;
        for(Song x: songs){
            if(x.getTitle().equals(title)){
                isSongPresent=true;
                s=x;
                break;
            }
        }
        if(!isSongPresent){
            songs.add(s);
        }
        if(albumSongMap.containsKey(album)){
            List<Song> l = albumSongMap.get(album);
            boolean isP = false;
            for(Song x: l){
                if(x.getTitle().equals(title)){
                    isP=true;
                    break;
                }
            }
            if(!isP){
                l.add(s);
            }

        }else{
            List<Song> l = new ArrayList<>();
            l.add(s);
            albumSongMap.put(album,l);
        }
        return s;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User u = null;
        for(User x: users){
            if(x.getMobile().equals(mobile)){
                u=x;
                break;
            }
        }
        if(u==null){
            throw new Exception("User does not exist");
        }

        Playlist p = new Playlist(title);
        boolean isP = false;
        for(Playlist x : playlists){
            if(x.getTitle().equals(title)){
                p=x;
                isP=true;
            }
        }
        if(!isP){
            playlists.add(p);
        }

        if(playlistSongMap.containsKey(p)){

        }else{
            List<Song> l = new ArrayList<>();
            for(Song x: songs){
                if(x.getLength()==length){
                    l.add(x);
                }
            }
            playlistSongMap.put(p,l);
        }

        if(playlistListenerMap.containsKey(p)){
            List<User> temp = playlistListenerMap.get(p);
            boolean isUser = false;
            for(User x: temp){
                if(x==u){
                    isUser=true;
                    break;
                }
            }
            if(!isUser){
                temp.add(u);
            }

        }else{
            List<User> temp = new ArrayList<>();
            temp.add(u);
            playlistListenerMap.put(p,temp);
        }

        if(userPlaylistMap.containsKey(u)){
            List<Playlist> temp = userPlaylistMap.get(u);
            boolean alreadyAdded = false;
            for(Playlist x: temp){
                if(x==p){
                    alreadyAdded=true;
                    break;
                }
            }
            if(!alreadyAdded){
                temp.add(p);
            }
        }else{
            List<Playlist> temp = new ArrayList<>();
            temp.add(p);
            userPlaylistMap.put(u,temp);
        }

        return p;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

    }

    public String mostPopularArtist() {
    }

    public String mostPopularSong() {
    }
}
