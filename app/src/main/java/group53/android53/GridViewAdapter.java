package group53.android53;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Paulo Garcia and Joshua Cross
 */
public class GridViewAdapter extends ArrayAdapter<Photo> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Photo> data = new ArrayList<Photo>();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Photo> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    /**
     * Overrides Array Adapter getView
     * to fit specifications for App
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        //Shows tags with Photos
        Photo item = data.get(position);
        String tags= "tags: ";
        for(tag t: item.getTags()){
            tags+="#"+t.getValue()+" ";
        }

        //Sets message displayed as the
        //Photo's caption and it's tags
        String msg = item.getCaption()+" "+tags;
        holder.imageTitle.setText(msg);
        holder.image.setImageBitmap(item.getImage());
        return row;
    }

    /**
     * Creates a View Holder for a Text View
     * and an Image View
     */
    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}