package carpool.southside.southsidecarpool;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>{
    private List<Announcement> announcements;
    public AnnouncementAdapter(List<Announcement> announcements){
        this.announcements = announcements;
    }
    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder{
        TextView announcementName, announcementDate, announcementMessage;
        MaterialLetterIcon announcementImage;
        public AnnouncementViewHolder(View itemView){
            super(itemView);
            announcementName = (TextView) itemView.findViewById(R.id.announcement_name);
            announcementDate = (TextView) itemView.findViewById(R.id.announcement_timestamp);
            announcementMessage = (TextView) itemView.findViewById(R.id.announcement_text);
            announcementImage = (MaterialLetterIcon) itemView.findViewById(R.id.announcement_image);
        }
    }
    public AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item, parent, false);
        AnnouncementViewHolder viewHolder = new AnnouncementViewHolder(view);
        return viewHolder;
    }
    public void onBindViewHolder(AnnouncementViewHolder holder, int position){
        holder.announcementName.setText(announcements.get(position).getAnnouncementName());
        holder.announcementDate.setText(announcements.get(position).getAnnouncementDate());
        holder.announcementMessage.setText(announcements.get(position).getAnnouncementMessage());
        holder.announcementImage.setShapeColor(Color.parseColor("#4CAF50"));
        holder.announcementImage.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
        holder.announcementImage.setLetterColor(Color.parseColor("#FFFFFF"));
        holder.announcementImage.setLetterSize(20);
        holder.announcementImage.setInitials(true);
        holder.announcementImage.setInitialsNumber(2);
        holder.announcementImage.setLetter(announcements.get(position).getAnnouncementName());
    }
    public int getItemCount(){
        return announcements.size();
    }
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
}
