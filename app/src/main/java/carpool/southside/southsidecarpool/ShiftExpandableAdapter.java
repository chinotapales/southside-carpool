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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.List;

public class ShiftExpandableAdapter extends ExpandableRecyclerAdapter<ShiftParentViewHolder, ShiftChildViewHolder>{
    public ShiftExpandableAdapter(Context context, List<ParentObject> parentItemList){
        super(context, parentItemList);
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
        shiftChildViewHolder.shiftNumber.setText(shift.getShiftPersonNumber());
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
        final Context context = v.getContext();
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
        final Dialog mBottomSheetDialog = new Dialog(v.getContext(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        heartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        heartRidersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        copyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO Snackbar over Bottom Navigation
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(shift.getShiftPersonNumber());
                Toast.makeText(v.getContext(), "Number Successfully Copied", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });
        callButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String number = "tel:" + shift.getShiftPersonNumber();
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
                String number = "smsto:" + shift.getShiftPersonNumber();
                String name[] = shift.getShiftProvider().split(" ");
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(number));
                intent.putExtra("sms_body", "Hey " + name[0] + ", ");
                context.startActivity(intent);
                mBottomSheetDialog.dismiss();
            }
        });
    }
}
