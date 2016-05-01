package carpool.southside.southsidecarpool;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import info.hoang8f.android.segmented.SegmentedGroup;

public class DirectoryFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private SegmentedGroup segmentedSchool;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.directory_view, container, false);
        segmentedSchool = (SegmentedGroup) v.findViewById(R.id.segmented_directory);
        segmentedSchool.setOnCheckedChangeListener(this);
        return v;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.dlsu_button:
                Toast.makeText(getActivity(), "DLSU Students", Toast.LENGTH_SHORT).show();
                break;
            case R.id.csb_button:
                Toast.makeText(getActivity(), "CSB Students", Toast.LENGTH_SHORT).show();
                break;
            case R.id.both_button:
                Toast.makeText(getActivity(), "Both", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }
}
