package mchehab.com.customlistviewimage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by muhammadchehab on 10/29/17.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Person> listPersons;

    public ListViewAdapter(Context context, List<Person> listPersons){
        this.context = context;
        this.listPersons = listPersons;
    }

    @Override
    public int getCount() {
        return listPersons.size();
    }

    @Override
    public Object getItem(int position) {
        return listPersons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_listview, null);

            viewHolder = new ViewHolder();

            viewHolder.imageViewProfilePic = convertView.findViewById(R.id.imageViewProfilePic);
            viewHolder.textViewName = convertView.findViewById(R.id.textViewName);
            viewHolder.textViewDescription = convertView.findViewById(R.id.textViewDescription);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Person person = listPersons.get(position);

        viewHolder.textViewName.setText(person.getFirstName() + " " + person.getLastName());
        viewHolder.textViewDescription.setText(person.getDescription());
        viewHolder.imageViewProfilePic.setImageDrawable(getImageDrawable(person.getImageName()));

        return convertView;
    }

    private Drawable getImageDrawable(String imageName){
        int id  = context.getResources().getIdentifier(imageName, "drawable",
                context.getPackageName());
        return context.getResources().getDrawable(id);
    }

    class ViewHolder{
        ImageView imageViewProfilePic;
        TextView textViewName;
        TextView textViewDescription;
    }

}