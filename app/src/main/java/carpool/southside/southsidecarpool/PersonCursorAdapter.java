package carpool.southside.southsidecarpool;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class PersonCursorAdapter extends CursorRecyclerViewAdapter<PersonCursorAdapter.PersonViewHolder>{
    private DatabaseOpenHelper dbHelper;
    private Context context;
    private Cursor c;
    private int type;
    public PersonCursorAdapter(Context context, Cursor cursor, int type){
        super(context, cursor);
        this.context = context;
        this.type = type;
        dbHelper = new DatabaseOpenHelper(context);
        c = cursor;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.directory_item, parent, false);
        PersonViewHolder viewHolder = new PersonViewHolder(view);
        return viewHolder;
    }
    public void onBindViewHolder(PersonViewHolder holder, Cursor cursor){
        final int position = cursor.getPosition();
        c = cursor;
        holder.personName.setText(cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
        holder.personNumber.setText(cursor.getString(cursor.getColumnIndex(Person.COL_NUMBER)));
        holder.personImage.setShapeColor(Color.parseColor("#4CAF50"));
        holder.personImage.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
        holder.personImage.setLetterColor(Color.parseColor("#FFFFFF"));
        holder.personImage.setLetterSize(20);
        holder.personImage.setInitials(true);
        holder.personImage.setInitialsNumber(2);
        holder.personImage.setLetter(cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
        holder.personContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openBottomSheet(v, c, position);
            }
        });
        holder.personContainer.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(getCursor().getString(getCursor().getColumnIndex(Person.COL_NUMBER)));
                Toast.makeText(v.getContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    public String getIntentNumber(int position){
        c.moveToPosition(position);
        return c.getString(c.getColumnIndex(Person.COL_NUMBER));
    }
    public String getPersonName(int position){
        c.moveToPosition(position);
        return c.getString(c.getColumnIndex(Person.COL_NAME));
    }
    public void openBottomSheet(View v, final Cursor cursor, final int position){
        cursor.moveToPosition(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottom_sheet, null);
        MaterialLetterIcon bottomImage = (MaterialLetterIcon) view.findViewById(R.id.image_bottom);
        bottomImage.setShapeColor(Color.parseColor("#4CAF50"));
        bottomImage.setShapeType(MaterialLetterIcon.Shape.CIRCLE);
        bottomImage.setLetterColor(Color.parseColor("#FFFFFF"));
        bottomImage.setLetterSize(18);
        bottomImage.setInitials(true);
        bottomImage.setInitialsNumber(2);
        bottomImage.setLetter(cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
        TextView bottomName = (TextView) view.findViewById(R.id.name_bottom);
        bottomName.setText(cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
        RelativeLayout heartButton = (RelativeLayout) view.findViewById(R.id.heart_bottom);
        RelativeLayout heartRidersButton = (RelativeLayout) view.findViewById(R.id.heart_riders_bottom);
        RelativeLayout copyButton = (RelativeLayout) view.findViewById(R.id.copy_bottom);
        RelativeLayout callButton = (RelativeLayout) view.findViewById(R.id.call_bottom);
        TextView callText = (TextView) view.findViewById(R.id.call_bottom_text);
        callText.setText("Call " + cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
        RelativeLayout messageButton = (RelativeLayout) view.findViewById(R.id.message_bottom);
        TextView messageText = (TextView) view.findViewById(R.id.message_bottom_text);
        messageText.setText("Message " + cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
        ImageView heartImage = (ImageView) view.findViewById(R.id.heart_bottom_icon);
        TextView heartText = (TextView) view.findViewById(R.id.heart_bottom_text);
        ImageView heartRiderImage = (ImageView) view.findViewById(R.id.heart_riders_bottom_icon);
        TextView heartRiderText = (TextView) view.findViewById(R.id.heart_riders_bottom_text);
        final boolean isFavorited = dbHelper.getIsFavorited(cursor.getInt(cursor.getColumnIndex(Person.COL_ID)));
        final boolean isRiderFavorited = dbHelper.getIsRiderFavorited(cursor.getInt(cursor.getColumnIndex(Person.COL_ID)));
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
        final Dialog mBottomSheetDialog = new Dialog(v.getContext(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        heartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isFavorited){
                    dbHelper.updateFavoriteProvider(cursor.getInt(cursor.getColumnIndex(Person.COL_ID)), 1);
                }
                else{
                    dbHelper.updateFavoriteProvider(cursor.getInt(cursor.getColumnIndex(Person.COL_ID)), 0);
                }
                if(type == 1){
                    swapCursor(dbHelper.getAllPeopleByFavProviders());
                }
                mBottomSheetDialog.dismiss();
            }
        });
        heartRidersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isRiderFavorited){
                    dbHelper.updateFavoriteRider(cursor.getInt(cursor.getColumnIndex(Person.COL_ID)), 1);
                }
                else{
                    dbHelper.updateFavoriteRider(cursor.getInt(cursor.getColumnIndex(Person.COL_ID)), 0);
                }
                if(type == 1){
                    swapCursor(dbHelper.getAllPeopleByFavRiders());
                }
                mBottomSheetDialog.dismiss();
            }
        });
        copyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(cursor.getString(cursor.getColumnIndex(Person.COL_NUMBER)));
                Toast.makeText(v.getContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });
        callButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String number = "tel:" + cursor.getString(cursor.getColumnIndex(Person.COL_NUMBER));
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
                String number = "smsto:" + cursor.getString(cursor.getColumnIndex(Person.COL_NUMBER));
                String name[] = cursor.getString(cursor.getColumnIndex(Person.COL_NAME)).split(" ");
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(number));
                intent.putExtra("sms_body", "Hey " + name[0] + ", ");
                context.startActivity(intent);
                mBottomSheetDialog.dismiss();
            }
        });
    }
}
