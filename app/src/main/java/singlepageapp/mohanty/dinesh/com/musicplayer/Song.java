package singlepageapp.mohanty.dinesh.com.musicplayer;

public class Song {

    private String mSongName;
    private String mSinger;
    int imageId;
    int songId;

    public Song(String name , String singer , int id , int idsong)
    {
        mSongName = name;
        mSinger = singer;
        imageId = id;
        songId = idsong;

    }

    public int getImageId()
    {
        return imageId;
    }
    public String getSongName()
    {
        return mSongName;
    }

    public String getSinger()
    {
        return mSinger;
    }
    public int getSongId()
    {
        return songId;
    }


}
