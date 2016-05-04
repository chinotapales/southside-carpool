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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{
    private List<Person> people;
    public PersonAdapter(List<Person> people){
        this.people = people;
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView personName, personNumber;
        RelativeLayout personContainer;
        MaterialLetterIcon personImage;
        public PersonViewHolder(View itemView){
            super(itemView);
            personName = (TextView) itemView.findViewById(R.id.directory_name);
            personNumber = (TextView) itemView.findViewById(R.id.directory_number);
            personImage = (MaterialLetterIcon) itemView.findViewById(R.id.directory_image);
            personContainer = (RelativeLayout) itemView.findViewById(R.id.directory_container);
        }
    }
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.directory_item, parent, false);
        PersonViewHolder viewHolder = new PersonViewHolder(view);
        return viewHolder;
    }
    public void onBindViewHolder(final PersonViewHolder holder, final int position){
        final int i = position;
        holder.personName.setText(people.get(position).getPersonName());
        holder.personNumber.setText(people.get(position).getPersonNumber());
        holder.personImage.setShapeColor(Color.parseColor("#4CAF50"));
        holder.personImage.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
        holder.personImage.setLetterColor(Color.parseColor("#FFFFFF"));
        holder.personImage.setLetterSize(20);
        holder.personImage.setInitials(true);
        holder.personImage.setInitialsNumber(2);
        holder.personImage.setLetter(people.get(position).getPersonName());
        holder.personContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openBottomSheet(v, i);
            }
        });
        holder.personContainer.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(people.get(position).getPersonNumber());
                Toast.makeText(v.getContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    public int getItemCount(){
        return people.size();
    }
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
    public String getIntentNumber(int position){
        return people.get(position).getPersonNumber();
    }
    public String getPersonName(int position){
        return people.get(position).getPersonName();
    }
    public void openBottomSheet(View v, int position){
        final int i = position;
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
        bottomImage.setLetter(getPersonName(position));
        TextView bottomName = (TextView) view.findViewById(R.id.name_bottom);
        bottomName.setText(getPersonName(position));
        RelativeLayout heartButton = (RelativeLayout) view.findViewById(R.id.heart_bottom);
        RelativeLayout heartRidersButton = (RelativeLayout) view.findViewById(R.id.heart_riders_bottom);
        RelativeLayout copyButton = (RelativeLayout) view.findViewById(R.id.copy_bottom);
        RelativeLayout callButton = (RelativeLayout) view.findViewById(R.id.call_bottom);
        TextView callText = (TextView) view.findViewById(R.id.call_bottom_text);
        callText.setText("Call " + getPersonName(position));
        RelativeLayout messageButton = (RelativeLayout) view.findViewById(R.id.message_bottom);
        TextView messageText = (TextView) view.findViewById(R.id.message_bottom_text);
        messageText.setText("Message " + getPersonName(position));
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
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(getIntentNumber(i));
                Toast.makeText(v.getContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });
        callButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String number = "tel:" + getIntentNumber(i);
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
                String number = "smsto:" + getIntentNumber(i);
                String name[] = getPersonName(i).split(" ");
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(number));
                intent.putExtra("sms_body", "Hey " + name[0] + ", ");
                context.startActivity(intent);
                mBottomSheetDialog.dismiss();
            }
        });
    }
}
