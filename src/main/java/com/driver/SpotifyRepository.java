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
        //artistAlbumMap.put(new Artist("Fake"),new ArrayList<>());
        albumSongMap = new HashMap<>();
        //albumSongMap.put(new Album("Fake"),new ArrayList<>());
        playlistSongMap = new HashMap<>();
        //playlistSongMap.put(new Playlist("Fake"),new ArrayList<>());
        playlistListenerMap = new HashMap<>();
        //playlistListenerMap.put(new Playlist("Fake"),new ArrayList<>());
        creatorPlaylistMap = new HashMap<>();
        //creatorPlaylistMap.put(new User("Fake","1234567890"),new Playlist("Fake"));
        userPlaylistMap = new HashMap<>();
        //userPlaylistMap.put(new User("Fake","1234567890"),new ArrayList<>());
        songLikeMap = new HashMap<>();
        //songLikeMap.put(new Song("Fake",5),new ArrayList<>());

        users = new ArrayList<>();
        //users.add(new User("Fake","1234567890"));
        songs = new ArrayList<>();
        //songs.add(new Song("Fake",5));
        playlists = new ArrayList<>();
        //playlists.add(new Playlist("Fake"));
        albums = new ArrayList<>();
        //albums.add(new Album("Fake"));
        artists = new ArrayList<>();
        //artists.add(new Artist("Fake"));

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
        }else{
            return p;
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
        }else{
            return p;
        }

        if(playlistSongMap.containsKey(p)){

        }else{
            List<Song> temp = new ArrayList<>();
            for(Song x: songs){
                if(songTitles.contains(x.getTitle())){
                    temp.add(x);
                }
            }
            playlistSongMap.put(p,temp);
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

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        //Find the playlist with given title and add user as listener of that playlist and update user accordingly
        //If the user is creater or already a listener, do nothing
        //If the user does not exist, throw "User does not exist" exception
        //If the playlist does not exists, throw "Playlist does not exist" exception
        // Return the playlist after updating
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

        Playlist p = null;
        for(Playlist x: playlists){
            if(x.getTitle().equals(playlistTitle)){
                p=x;
                break;
            }
        }
        if(p==null){
            throw new Exception("Playlist does not exist");
        }
        boolean isP = false;
        for(User x:playlistListenerMap.get(p)){
            if(x==u){
                isP=true;
                break;
            }
        }
        if(!isP){
            playlistListenerMap.get(p).add(u);
        }

        isP = false;
        if(!userPlaylistMap.containsKey(u)){
            List<Playlist> temp = new ArrayList<>();
            temp.add(p);
            userPlaylistMap.put(u,temp);
            return p;
        }
        for(Playlist x: userPlaylistMap.get(u)){
            if(x==p){
                isP=true;
                break;
            }
        }
        if(!isP){
            userPlaylistMap.get(u).add(p);
        }
        return p;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User u = null;
        Song s = null;
        Artist a = null;
        for(User x: users){
            if(x.getMobile().equals(mobile)){
                u=x;
                break;
            }
        }
        //If the user does not exist, throw "User does not exist" exception
        //If the song does not exist, throw "Song does not exist" exception
        if(u==null){
            throw new Exception("User does not exist");
        }

        for(Song x: songs){
            if(x.getTitle().equals(songTitle)){
                s=x;
                break;
            }
        }

        if(s==null){
            throw new Exception("Song does not exist");
        }
        if(songLikeMap.containsKey(s)){
            for(User x:songLikeMap.get(s)){
                if(x==u){
                    return s;
                }
            }
            s.setLikes(s.getLikes()+1);
            songLikeMap.get(s).add(u);
        }else{
            s.setLikes(s.getLikes()+1);
            List<User> temp = new ArrayList<>();
            temp.add(u);
            songLikeMap.put(s,temp);
        }

        Album al = null;
        Artist ar = null;

        for(Album x : albumSongMap.keySet()){
            for(Song temp : albumSongMap.get(x)){
                if(temp==s){
                    al=x;
                    break;
                }
            }
            if(al!=null){
                break;
            }
        }
        if(al==null){
            return s;
        }
        for(Artist x: artistAlbumMap.keySet()){
            for(Album temp : artistAlbumMap.get(x)){
                if(temp==al){
                    ar=x;
                    break;
                }
            }
            if(ar!=null){
                break;
            }
        }

        if(ar!=null){
            ar.setLikes(ar.getLikes()+1);
        }

        return s;

    }

    public String mostPopularArtist() {
        int max = Integer.MIN_VALUE;
        String ans = "";
        for(Artist x : artists){
            if(x.getLikes()>max){
                max=x.getLikes();
                ans=x.getName();
            }
        }
        return ans;
    }

    public String mostPopularSong() {
        int max = Integer.MIN_VALUE;
        String ans = "";
        for(Song x:songs){
            if(x.getLikes()>max){
                max=x.getLikes();
                ans=x.getTitle();
            }
        }
        return ans;
    }
}
