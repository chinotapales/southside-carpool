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
    private String day;
    private String type;
    private SegmentedGroup segmentedDays;
    private RecyclerView rSchedule;
    private ShiftExpandableAdapter sAdapter;
    private SwipeRefreshLayout dSwipeRefreshLayout;
    private DatabaseOpenHelper dbHelper;
    private ArrayList<Object> shifts;
    private ArrayList<ParentObject> times;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.schedule_view, container, false);
        type = getArguments().getString("trip_id");
        segmentedDays = (SegmentedGroup) v.findViewById(R.id.segmented_days);
        segmentedDays.setOnCheckedChangeListener(this);
        dSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.schedule_swipe_refresh_layout);
        dSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        rSchedule = (RecyclerView) v.findViewById(R.id.schedule_recycler_view);
        dbHelper = new DatabaseOpenHelper(v.getContext());
        day = "Monday";
        initData(day, type);
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
        initData(day, type);
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
                day = "Monday";
                initData(day, type);
                break;
            case R.id.tuesday_button:
                day = "Tuesday";
                initData(day, type);
                break;
            case R.id.wednesday_button:
                day = "Wednesday";
                initData(day, type);
                break;
            case R.id.thursday_button:
                day = "Thursday";
                initData(day, type);
                break;
            case R.id.friday_button:
                day = "Friday";
                initData(day, type);
                break;
            case R.id.saturday_button:
                day = "Saturday";
                initData(day, type);
                break;
            default:
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        initData(day, type);
        dSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                //TODO Place Refresh Code Here
            }
        });
        rSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public void initData(String day, String type){
        if(times != null){
            times.clear();
        }
        if(shifts != null){
            shifts.clear();
        }
        shifts = dbHelper.getArrayListShifts(day, type);
        times = dbHelper.getAssignedTimesByDayAndType(day, type);
        for(int i = 0; i < times.size(); i++){
            AssignedTime a = (AssignedTime) times.get(i);
            for(int j = 0; j < shifts.size(); j++){
                Shift s = (Shift) shifts.get(j);
                if(s.getShiftTime().equals(a.getShiftTime())){
                    a.addShift(s);
                    times.set(i, a);
                }
            }
        }
        sAdapter = new ShiftExpandableAdapter(getContext(), times);
        sAdapter.setCustomParentAnimationViewId(R.id.schedule_expand_button);
        sAdapter.setParentClickableViewAnimationDefaultDuration();
        sAdapter.setParentAndIconExpandOnClick(true);
        rSchedule.setAdapter(sAdapter);
    }
}
