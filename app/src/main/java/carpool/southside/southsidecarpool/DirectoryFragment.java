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
import android.widget.Toast;
import java.util.ArrayList;
import info.hoang8f.android.segmented.SegmentedGroup;

public class DirectoryFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private RecyclerView rvPeople;
    private PersonAdapter personAdapter;
    private SegmentedGroup segmentedSchool;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.directory_view, container, false);
        segmentedSchool = (SegmentedGroup) v.findViewById(R.id.segmented_directory);
        segmentedSchool.setOnCheckedChangeListener(this);
        rvPeople = (RecyclerView) v.findViewById(R.id.directory_recycler_view);
        ArrayList<Person> people = new ArrayList<>();
        //Testing Purposes
        people.add(new Person("Chino Tapales", "09175524466", "DLSU"));
        people.add(new Person("Erika Mison", "09175524466", "CSB"));
        people.add(new Person("Briana Buencamino", "09175524466", "CSB"));
        personAdapter = new PersonAdapter(people);
        rvPeople.setAdapter(personAdapter);
        rvPeople.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return v;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId){
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
