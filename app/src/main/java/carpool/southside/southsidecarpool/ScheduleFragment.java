package carpool.southside.southsidecarpool;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import info.hoang8f.android.segmented.SegmentedGroup;

public class ScheduleFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private String type;
    private SegmentedGroup segmentedDays;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.schedule_view, container, false);
        type = getArguments().getString("trip_id");
        System.out.println("ID Passed: " + type);
        segmentedDays = (SegmentedGroup) v.findViewById(R.id.segmented_days);
        segmentedDays.setOnCheckedChangeListener(this);
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
}
