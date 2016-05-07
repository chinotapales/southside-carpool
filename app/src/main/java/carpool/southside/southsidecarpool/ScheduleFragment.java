package carpool.southside.southsidecarpool;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import java.util.ArrayList;
import info.hoang8f.android.segmented.SegmentedGroup;

public class ScheduleFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private String type;
    private SegmentedGroup segmentedDays;
    private RecyclerView rSchedule;
    private ShiftExpandableAdapter sAdapter;
    private SwipeRefreshLayout dSwipeRefreshLayout;
    private DatabaseOpenHelper dbHelper;
    private ArrayList<Object> shifts;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.schedule_view, container, false);
        type = getArguments().getString("trip_id");
        segmentedDays = (SegmentedGroup) v.findViewById(R.id.segmented_days);
        segmentedDays.setOnCheckedChangeListener(this);
        dSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.schedule_swipe_refresh_layout);
        dSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        rSchedule = (RecyclerView) v.findViewById(R.id.schedule_recycler_view);
        dbHelper = new DatabaseOpenHelper(v.getContext());
        if(shifts != null){
            shifts.clear();
        }
        shifts = dbHelper.getArrayListShifts(type);
        ArrayList<ParentObject> aTimes = new ArrayList<>();
        aTimes.add(new AssignedTime("7:30 AM", shifts));
        aTimes.add(new AssignedTime("8:00 AM", shifts));
        aTimes.add(new AssignedTime("9:15 AM", shifts));
        sAdapter = new ShiftExpandableAdapter(v.getContext(), aTimes);
        sAdapter.setCustomParentAnimationViewId(R.id.schedule_expand_button);
        sAdapter.setParentClickableViewAnimationDefaultDuration();
        sAdapter.setParentAndIconExpandOnClick(true);
        rSchedule.setAdapter(sAdapter);
        dSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                //TODO Place Refresh Code Here
            }
        });
        rSchedule.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return v;
    }
    public void setType(String type){
        this.type = type;
        System.out.println("ID Changed: " + this.type);
        if(shifts != null){
            shifts.clear();
        }
        shifts = dbHelper.getArrayListShifts(this.type);
        ArrayList<ParentObject> aTimes = new ArrayList<>();
        aTimes.add(new AssignedTime("7:30 AM", shifts));
        aTimes.add(new AssignedTime("8:00 AM", shifts));
        aTimes.add(new AssignedTime("9:15 AM", shifts));
        sAdapter = new ShiftExpandableAdapter(getContext(), aTimes);
        sAdapter.setCustomParentAnimationViewId(R.id.schedule_expand_button);
        sAdapter.setParentClickableViewAnimationDefaultDuration();
        sAdapter.setParentAndIconExpandOnClick(true);
        rSchedule.setAdapter(sAdapter);
        dSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                //TODO Place Refresh Code Here
            }
        });
        rSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId){
        switch(checkedId){
            case R.id.monday_button:
                break;
            case R.id.tuesday_button:
                break;
            case R.id.wednesday_button:
                break;
            case R.id.thursday_button:
                break;
            case R.id.friday_button:
                break;
            case R.id.saturday_button:
                break;
            default:
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        //Testing Purposes
        if(shifts != null){
            shifts.clear();
        }
        shifts = dbHelper.getArrayListShifts(type);
        ArrayList<ParentObject> aTimes = new ArrayList<>();
        aTimes.add(new AssignedTime("7:30 AM", shifts));
        aTimes.add(new AssignedTime("8:00 AM", shifts));
        aTimes.add(new AssignedTime("9:15 AM", shifts));
        sAdapter = new ShiftExpandableAdapter(getContext(), aTimes);
        sAdapter.setCustomParentAnimationViewId(R.id.schedule_expand_button);
        sAdapter.setParentClickableViewAnimationDefaultDuration();
        sAdapter.setParentAndIconExpandOnClick(true);
        rSchedule.setAdapter(sAdapter);
        dSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                //TODO Place Refresh Code Here
            }
        });
        rSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
