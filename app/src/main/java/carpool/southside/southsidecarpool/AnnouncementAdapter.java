package carpool.southside.southsidecarpool;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>{
    private DatabaseOpenHelper dbHelper;
    private Context context;
    private List<Announcement> announcements;
    public AnnouncementAdapter(Context context, List<Announcement> announcements){
        this.context = context;
        this.announcements = announcements;
        dbHelper = new DatabaseOpenHelper(context);
    }
    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout container;
        TextView announcementName, announcementDate, announcementMessage;
        MaterialLetterIcon announcementImage;
        public AnnouncementViewHolder(View itemView){
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.announcement_container);
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
        final int index = position;
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
        holder.container.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openBottomSheet(v, index);
            }
        });
    }
    public int getItemCount(){
        return announcements.size();
    }
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void openBottomSheet(View v, final int position){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottom_sheet, null);
        MaterialLetterIcon bottomImage = (MaterialLetterIcon) view.findViewById(R.id.image_bottom);
        bottomImage.setShapeColor(Color.parseColor("#4CAF50"));
        bottomImage.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
        bottomImage.setLetterColor(Color.parseColor("#FFFFFF"));
        bottomImage.setLetterSize(18);
        bottomImage.setInitials(true);
        bottomImage.setInitialsNumber(2);
        bottomImage.setLetter(announcements.get(position).getAnnouncementName());
        TextView bottomName = (TextView) view.findViewById(R.id.name_bottom);
        bottomName.setText(announcements.get(position).getAnnouncementName());
        RelativeLayout heartButton = (RelativeLayout) view.findViewById(R.id.heart_bottom);
        RelativeLayout heartRidersButton = (RelativeLayout) view.findViewById(R.id.heart_riders_bottom);
        RelativeLayout copyButton = (RelativeLayout) view.findViewById(R.id.copy_bottom);
        RelativeLayout callButton = (RelativeLayout) view.findViewById(R.id.call_bottom);
        TextView callText = (TextView) view.findViewById(R.id.call_bottom_text);
        callText.setText("Call " + announcements.get(position).getAnnouncementName());
        RelativeLayout messageButton = (RelativeLayout) view.findViewById(R.id.message_bottom);
        TextView messageText = (TextView) view.findViewById(R.id.message_bottom_text);
        messageText.setText("Message " + announcements.get(position).getAnnouncementName());
        ImageView heartImage = (ImageView) view.findViewById(R.id.heart_bottom_icon);
        TextView heartText = (TextView) view.findViewById(R.id.heart_bottom_text);
        ImageView heartRiderImage = (ImageView) view.findViewById(R.id.heart_riders_bottom_icon);
        TextView heartRiderText = (TextView) view.findViewById(R.id.heart_riders_bottom_text);
        final boolean isFavorited = dbHelper.getIsFavoritedByName(announcements.get(position).getAnnouncementName());
        final boolean isRiderFavorited = dbHelper.getIsRiderFavoritedByName(announcements.get(position).getAnnouncementName());
        if(!isFavorited){
            heartImage.setImageResource(R.drawable.heart_bottom);
            heartText.setText("Add to My Providers");
        }
        else{
            heartImage.setImageResource(R.drawable.unheart_bottom);
            heartText.setText("Remove from My Providers");
        }
        if(!isRiderFavorited){
            heartRiderImage.setImageResource(R.drawable.heart_bottom);
            heartRiderText.setText("Add to My Riders");
        }
        else{
            heartRiderImage.setImageResource(R.drawable.unheart_bottom);
            heartRiderText.setText("Remove from My Riders");
        }
        final Dialog mBottomSheetDialog = new Dialog(context, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        heartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isFavorited){
                    dbHelper.updateFavoriteProviderByName(announcements.get(position).getAnnouncementName(), 1);
                    Toast.makeText(v.getContext(), announcements.get(position).getAnnouncementName() + " Added to My Providers", Toast.LENGTH_SHORT).show();
                }
                else{
                    dbHelper.updateFavoriteProviderByName(announcements.get(position).getAnnouncementName(), 0);
                    Toast.makeText(v.getContext(), announcements.get(position).getAnnouncementName() + " Removed from My Providers", Toast.LENGTH_SHORT).show();
                }
                mBottomSheetDialog.dismiss();
            }
        });
        heartRidersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isRiderFavorited){
                    dbHelper.updateFavoriteRiderByName(announcements.get(position).getAnnouncementName(), 1);
                    Toast.makeText(v.getContext(), announcements.get(position).getAnnouncementName() + " Added to My Riders", Toast.LENGTH_SHORT).show();
                }
                else{
                    dbHelper.updateFavoriteRiderByName(announcements.get(position).getAnnouncementName(), 0);
                    Toast.makeText(v.getContext(), announcements.get(position).getAnnouncementName() + " Removed from My Riders", Toast.LENGTH_SHORT).show();
                }
                mBottomSheetDialog.dismiss();
            }
        });
        copyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(dbHelper.getNumberFromName(announcements.get(position).getAnnouncementName()));
                Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });
        callButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String number = "tel:" + dbHelper.getNumberFromName(announcements.get(position).getAnnouncementName());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse(number));
                context.startActivity(intent);
                mBottomSheetDialog.dismiss();
            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "smsto:" + dbHelper.getNumberFromName(announcements.get(position).getAnnouncementName());
                String name[] = announcements.get(position).getAnnouncementName().split(" ");
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(number));
                intent.putExtra("sms_body", "Hey " + name[0] + ", ");
                context.startActivity(intent);
                mBottomSheetDialog.dismiss();
            }
        });
    }
}
