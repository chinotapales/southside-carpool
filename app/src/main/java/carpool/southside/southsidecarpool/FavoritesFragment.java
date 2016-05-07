package carpool.southside.southsidecarpool;

import android.content.Intent;
import android.database.Cursor;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import info.hoang8f.android.segmented.SegmentedGroup;

public class FavoritesFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private int previous = 0;
    private RecyclerView rvFavorites;
    private PersonCursorAdapter personAdapter;
    private DatabaseOpenHelper dbHelper;
    private SegmentedGroup segmentedFavorites;
    private SwipeRefreshLayout dSwipeRefreshLayout;
    private Paint p = new Paint();
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.favorite_view, container, false);
        segmentedFavorites = (SegmentedGroup) v.findViewById(R.id.segmented_favorites);
        segmentedFavorites.setOnCheckedChangeListener(this);
        dSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.favorite_swipe_refresh_layout);
        dSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        dbHelper = new DatabaseOpenHelper(v.getContext());
        rvFavorites = (RecyclerView) v.findViewById(R.id.favorite_recycler_view);
        personAdapter = new PersonCursorAdapter(v.getContext(), dbHelper.getAllPeopleByFavProviders(), 1);
        rvFavorites.setAdapter(personAdapter);
        dSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                //TODO Place Refresh Code Here
            }
        });
        rvFavorites.setLayoutManager(new LinearLayoutManager(v.getContext()));
        initSwipe();
        return v;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId){
        switch(checkedId){
            case R.id.my_providers_button:
                personAdapter.swapCursor(dbHelper.getAllPeopleByFavProviders());
                previous = 0;
                break;
            case R.id.my_riders_button:
                personAdapter.swapCursor(dbHelper.getAllPeopleByFavRiders());
                previous = 1;
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
        itemTouchHelper.attachToRecyclerView(rvFavorites);
    }
    @Override
    public void onResume(){
        super.onResume();
        Cursor cursor;
        switch(previous){
            case 0: cursor = dbHelper.getAllPeopleByFavProviders();
                    personAdapter.swapCursor(cursor);
                    break;
            case 1: cursor = dbHelper.getAllPeopleByFavRiders();
                    personAdapter.swapCursor(cursor);
                    break;
        }
        initSwipe();
    }
}
