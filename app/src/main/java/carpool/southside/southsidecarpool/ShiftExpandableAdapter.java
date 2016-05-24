package carpool.southside.southsidecarpool;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import java.util.List;

public class ShiftExpandableAdapter extends ExpandableRecyclerAdapter<ShiftExpandableAdapter.ShiftParentViewHolder, ShiftExpandableAdapter.ShiftChildViewHolder>{
    private Context context;
    private DatabaseOpenHelper dbHelper;
    public ShiftExpandableAdapter(Context context, List<ParentObject> parentItemList){
        super(context, parentItemList);
        this.context = context;
        dbHelper = new DatabaseOpenHelper(context);
    }
    public class ShiftParentViewHolder extends ParentViewHolder{
        public RelativeLayout scheduleParentContainer;
        public TextView scheduleParentName;
        public ImageView expandButton;
        public ShiftParentViewHolder(View itemView){
            super(itemView);
            scheduleParentContainer = (RelativeLayout) itemView.findViewById(R.id.schedule_parent_container);
            scheduleParentName = (TextView) itemView.findViewById(R.id.schedule_parent_name);
            expandButton = (ImageView) itemView.findViewById(R.id.schedule_expand_button);
        }
    }
    public class ShiftChildViewHolder extends ChildViewHolder{
        public TextView shiftName, shiftNumber;
        RelativeLayout shiftContainer;
        MaterialLetterIcon shiftImage;
        public ShiftChildViewHolder(View itemView){
            super(itemView);
            shiftName = (TextView) itemView.findViewById(R.id.directory_name);
            shiftNumber = (TextView) itemView.findViewById(R.id.directory_number);
            shiftImage = (MaterialLetterIcon) itemView.findViewById(R.id.directory_image);
            shiftContainer = (RelativeLayout) itemView.findViewById(R.id.directory_container);
        }
    }
    @Override
    public ShiftParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.schedule_parent_item, viewGroup, false);
        return new ShiftParentViewHolder(view);
    }
    @Override
    public ShiftChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.directory_item, viewGroup, false);
        return new ShiftChildViewHolder(view);
    }
    @Override
    public void onBindParentViewHolder(ShiftParentViewHolder parentViewHolder, int position, Object parentListItem) {
        AssignedTime aTime = (AssignedTime) parentListItem;
        parentViewHolder.scheduleParentName.setText(aTime.getShiftTime());
    }
    @Override
    public void onBindChildViewHolder(ShiftChildViewHolder shiftChildViewHolder, int i, Object o){
        final Shift shift = (Shift) o;
        shiftChildViewHolder.shiftName.setText(shift.getShiftProvider());
        shiftChildViewHolder.shiftNumber.setText(dbHelper.getNumberFromName(shift.getShiftProvider()));
        shiftChildViewHolder.shiftImage.setShapeColor(Color.parseColor("#4CAF50"));
        shiftChildViewHolder.shiftImage.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
        shiftChildViewHolder.shiftImage.setLetterColor(Color.parseColor("#FFFFFF"));
        shiftChildViewHolder.shiftImage.setLetterSize(20);
        shiftChildViewHolder.shiftImage.setInitials(true);
        shiftChildViewHolder.shiftImage.setInitialsNumber(2);
        shiftChildViewHolder.shiftImage.setLetter(shift.getShiftProvider());
        shiftChildViewHolder.shiftContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openBottomSheet(v, shift);
            }
        });
    }
    public void openBottomSheet(View v, Shift s){
        final Shift shift = s;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottom_sheet, null);
        MaterialLetterIcon bottomImage = (MaterialLetterIcon) view.findViewById(R.id.image_bottom);
        bottomImage.setShapeColor(Color.parseColor("#4CAF50"));
        bottomImage.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
        bottomImage.setLetterColor(Color.parseColor("#FFFFFF"));
        bottomImage.setLetterSize(18);
        bottomImage.setInitials(true);
        bottomImage.setInitialsNumber(2);
        bottomImage.setLetter(shift.getShiftProvider());
        TextView bottomName = (TextView) view.findViewById(R.id.name_bottom);
        bottomName.setText(shift.getShiftProvider());
        RelativeLayout heartButton = (RelativeLayout) view.findViewById(R.id.heart_bottom);
        RelativeLayout heartRidersButton = (RelativeLayout) view.findViewById(R.id.heart_riders_bottom);
        RelativeLayout copyButton = (RelativeLayout) view.findViewById(R.id.copy_bottom);
        RelativeLayout callButton = (RelativeLayout) view.findViewById(R.id.call_bottom);
        TextView callText = (TextView) view.findViewById(R.id.call_bottom_text);
        callText.setText("Call " + shift.getShiftProvider());
        RelativeLayout messageButton = (RelativeLayout) view.findViewById(R.id.message_bottom);
        TextView messageText = (TextView) view.findViewById(R.id.message_bottom_text);
        messageText.setText("Message " + shift.getShiftProvider());
        ImageView heartImage = (ImageView) view.findViewById(R.id.heart_bottom_icon);
        TextView heartText = (TextView) view.findViewById(R.id.heart_bottom_text);
        ImageView heartRiderImage = (ImageView) view.findViewById(R.id.heart_riders_bottom_icon);
        TextView heartRiderText = (TextView) view.findViewById(R.id.heart_riders_bottom_text);
        final boolean isFavorited = dbHelper.getIsFavoritedByName(shift.getShiftProvider());
        final boolean isRiderFavorited = dbHelper.getIsRiderFavoritedByName(shift.getShiftProvider());
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
                    dbHelper.updateFavoriteProviderByName(shift.getShiftProvider(), 1);
                    Toast.makeText(v.getContext(), shift.getShiftProvider() + " Added to My Providers", Toast.LENGTH_SHORT).show();
                }
                else{
                    dbHelper.updateFavoriteProviderByName(shift.getShiftProvider(), 0);
                    Toast.makeText(v.getContext(), shift.getShiftProvider() + " Removed from My Providers", Toast.LENGTH_SHORT).show();
                }
                mBottomSheetDialog.dismiss();
            }
        });
        heartRidersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isRiderFavorited){
                    dbHelper.updateFavoriteRiderByName(shift.getShiftProvider(), 1);
                    Toast.makeText(v.getContext(), shift.getShiftProvider() + " Added to My Riders", Toast.LENGTH_SHORT).show();
                }
                else{
                    dbHelper.updateFavoriteRiderByName(shift.getShiftProvider(), 0);
                    Toast.makeText(v.getContext(), shift.getShiftProvider() + " Removed from My Riders", Toast.LENGTH_SHORT).show();
                }
                mBottomSheetDialog.dismiss();
            }
        });
        copyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(dbHelper.getNumberFromName(shift.getShiftProvider()));
                Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });
        callButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String number = "tel:" + dbHelper.getNumberFromName(shift.getShiftProvider());
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
                String number = "smsto:" + dbHelper.getNumberFromName(shift.getShiftProvider());
                String name[] = shift.getShiftProvider().split(" ");
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(number));
                intent.putExtra("sms_body", "Hey " + name[0] + ", ");
                context.startActivity(intent);
                mBottomSheetDialog.dismiss();
            }
        });
    }
}
