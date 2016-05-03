package carpool.southside.southsidecarpool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import java.util.ArrayList;
import info.hoang8f.android.segmented.SegmentedGroup;

public class DirectoryFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private RecyclerView rvPeople;
    private PersonAdapter personAdapter;
    private SegmentedGroup segmentedSchool;
    private Paint p = new Paint();
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.directory_view, container, false);
        segmentedSchool = (SegmentedGroup) v.findViewById(R.id.segmented_directory);
        segmentedSchool.setOnCheckedChangeListener(this);
        rvPeople = (RecyclerView) v.findViewById(R.id.directory_recycler_view);
        //Testing Purposes
        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person("Briana Buencamino", "09175524466", "CSB", 0, 0));
        people.add(new Person("Erika Mison", "09175524466", "CSB", 0, 0));
        people.add(new Person("Chino Tapales", "09175524466", "DLSU", 0, 0));
        people.add(new Person("Briana Buencamino", "09175524466", "CSB", 0, 0));
        people.add(new Person("Erika Mison", "09175524466", "CSB", 0, 0));
        people.add(new Person("Chino Tapales", "09175524466", "DLSU", 0, 0));
        people.add(new Person("Briana Buencamino", "09175524466", "CSB", 0, 0));
        people.add(new Person("Erika Mison", "09175524466", "CSB", 0, 0));
        people.add(new Person("Chino Tapales", "09175524466", "DLSU", 0, 0));
        people.add(new Person("Briana Buencamino", "09175524466", "CSB", 0, 0));
        people.add(new Person("Erika Mison", "09175524466", "CSB", 0, 0));
        people.add(new Person("Chino Tapales", "09175524466", "DLSU", 0, 0));
        personAdapter = new PersonAdapter(people);
        rvPeople.setAdapter(personAdapter);
        rvPeople.setLayoutManager(new LinearLayoutManager(v.getContext()));
        initSwipe();
        return v;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId){
        switch (checkedId){
            case R.id.all_button:
                break;
            case R.id.dlsu_button:
                break;
            case R.id.csb_button:
                break;
            default:
        }
    }
    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if(direction == ItemTouchHelper.LEFT){
                    String number = "smsto:" + personAdapter.getIntentNumber(position);
                    String name[] = personAdapter.getPersonName(position).split(" ");
                    Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(number));
                    i.putExtra("sms_body", "Hey " + name[0] + ", ");
                    startActivity(i);
                }
                else{
                    String number = "tel:" + personAdapter.getIntentNumber(position);
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse(number));
                    startActivity(i);
                }
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if(dX > 0){
                        p.setColor(Color.parseColor("#4CAF50"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.call);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                    else{
                        p.setColor(Color.parseColor("#FFC107"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.message);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvPeople);
    }
    @Override
    public void onResume(){
        super.onResume();
        //Testing Purposes
        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person("Briana Buencamino", "09175524466", "CSB", 0, 0));
        people.add(new Person("Erika Mison", "09175524466", "CSB", 0, 0));
        people.add(new Person("Chino Tapales", "09175524466", "DLSU", 0, 0));
        people.add(new Person("Briana Buencamino", "09175524466", "CSB", 0, 0));
        people.add(new Person("Erika Mison", "09175524466", "CSB", 0, 0));
        people.add(new Person("Chino Tapales", "09175524466", "DLSU", 0, 0));
        people.add(new Person("Briana Buencamino", "09175524466", "CSB", 0, 0));
        people.add(new Person("Erika Mison", "09175524466", "CSB", 0, 0));
        people.add(new Person("Chino Tapales", "09175524466", "DLSU", 0, 0));
        people.add(new Person("Briana Buencamino", "09175524466", "CSB", 0, 0));
        people.add(new Person("Erika Mison", "09175524466", "CSB", 0, 0));
        people.add(new Person("Chino Tapales", "09175524466", "DLSU", 0, 0));
        personAdapter = new PersonAdapter(people);
        rvPeople.setAdapter(personAdapter);
        rvPeople.setLayoutManager(new LinearLayoutManager(getContext()));
        initSwipe();
    }
}
