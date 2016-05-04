package carpool.southside.southsidecarpool;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.schedule_view, container, false);
        type = getArguments().getString("trip_id");
        System.out.println("ID Passed: " + type);
        segmentedDays = (SegmentedGroup) v.findViewById(R.id.segmented_days);
        segmentedDays.setOnCheckedChangeListener(this);
        rSchedule = (RecyclerView) v.findViewById(R.id.schedule_recycler_view);
        //Testing Purposes
        ArrayList<Object> shifts = new ArrayList<>();
        shifts.add(new Shift("Monday", "ToSchool", "9:00 AM", "Briana Buencamino", new Person("Briana Buencamino", "09175524466", "CSB", 0, 0)));
        shifts.add(new Shift("Monday", "ToSchool", "9:00 AM", "Erika Mison", new Person("Erika Mison", "09175524466", "CSB", 0, 0)));
        shifts.add(new Shift("Monday", "ToSchool", "9:00 AM", "Chino Tapales", new Person("Chino Tapales", "09175524466", "DLSU", 0, 0)));
        ArrayList<ParentObject> aTimes = new ArrayList<>();
        aTimes.add(new AssignedTime("7:30 AM", shifts));
        aTimes.add(new AssignedTime("8:00 AM", shifts));
        aTimes.add(new AssignedTime("9:15 AM", shifts));
        sAdapter = new ShiftExpandableAdapter(v.getContext(), aTimes);
        sAdapter.setCustomParentAnimationViewId(R.id.schedule_expand_button);
        sAdapter.setParentClickableViewAnimationDefaultDuration();
        sAdapter.setParentAndIconExpandOnClick(true);
        rSchedule.setAdapter(sAdapter);
        rSchedule.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return v;
    }
    public void setType(String type){
        this.type = type;
        System.out.println("ID Changed: " + this.type);
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
        ArrayList<Object> shifts = new ArrayList<>();
        shifts.add(new Shift("Monday", "ToSchool", "9:00 AM", "Briana Buencamino", new Person("Briana Buencamino", "09175524466", "CSB", 0, 0)));
        shifts.add(new Shift("Monday", "ToSchool", "9:00 AM", "Erika Mison", new Person("Erika Mison", "09175524466", "CSB", 0, 0)));
        shifts.add(new Shift("Monday", "ToSchool", "9:00 AM", "Chino Tapales", new Person("Chino Tapales", "09175524466", "DLSU", 0, 0)));
        ArrayList<ParentObject> aTimes = new ArrayList<>();
        aTimes.add(new AssignedTime("7:30 AM", shifts));
        aTimes.add(new AssignedTime("8:00 AM", shifts));
        aTimes.add(new AssignedTime("9:15 AM", shifts));
        sAdapter = new ShiftExpandableAdapter(getContext(), aTimes);
        sAdapter.setCustomParentAnimationViewId(R.id.schedule_expand_button);
        sAdapter.setParentClickableViewAnimationDefaultDuration();
        sAdapter.setParentAndIconExpandOnClick(true);
        rSchedule.setAdapter(sAdapter);
        rSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
