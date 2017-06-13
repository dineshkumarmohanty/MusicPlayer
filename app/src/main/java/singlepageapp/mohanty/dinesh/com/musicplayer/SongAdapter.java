package singlepageapp.mohanty.dinesh.com.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by debashish on 13-06-2017.
 */
public class SongAdapter  extends ArrayAdapter<Song> {


   public SongAdapter( Context context , ArrayList<Song> songs )
   {
      super(context , 0 , songs);

   }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
    Song song = getItem(position);

        TextView textView = (TextView) listItemView.findViewById(R.id.singer);
        textView.setText(song.getSinger());

        TextView textView1 = (TextView)listItemView.findViewById(R.id.song);
        textView1.setText(song.getSongName());
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.song_icon);
        imageView.setImageResource(song.getImageId());



        return listItemView;
    }



}

