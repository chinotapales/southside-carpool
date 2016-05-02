package carpool.southside.southsidecarpool;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{
    private List<Person> people;
    public PersonAdapter(List<Person> people){
        this.people = people;
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView personName, personNumber;
        MaterialLetterIcon personImage;
        public PersonViewHolder(View itemView){
            super(itemView);
            personName = (TextView) itemView.findViewById(R.id.directory_name);
            personNumber = (TextView) itemView.findViewById(R.id.directory_number);
            personImage = (MaterialLetterIcon) itemView.findViewById(R.id.directory_image);
        }
    }
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.directory_item, parent, false);
        PersonViewHolder viewHolder = new PersonViewHolder(view);
        return viewHolder;
    }
    public void onBindViewHolder(PersonViewHolder holder, int position){
        holder.personName.setText(people.get(position).getPersonName());
        holder.personNumber.setText(people.get(position).getPersonNumber());
        holder.personImage.setShapeColor(Color.parseColor("#4CAF50"));
        holder.personImage.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
        holder.personImage.setLetterColor(Color.parseColor("#FFFFFF"));
        holder.personImage.setLetterSize(20);
        holder.personImage.setInitials(true);
        holder.personImage.setInitialsNumber(2);
        holder.personImage.setLetter(people.get(position).getPersonName());
    }
    public int getItemCount(){
        return people.size();
    }
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
    public String getIntentNumber(int position){
        return "tel:" + people.get(position).getPersonNumber();
    }
}
