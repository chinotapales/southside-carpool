package carpool.southside.southsidecarpool;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import java.util.ArrayList;
import java.util.List;

public class AssignedTime implements ParentObject{
    private String shiftTime;
    private List<Object> shiftList = new ArrayList<>();
    public AssignedTime(){
    }
    public AssignedTime(String shiftTime){
        this.shiftTime = shiftTime;
    }
    public AssignedTime(String shiftTime, List<Object> shiftList){
        this.shiftTime = shiftTime;
        this.shiftList = shiftList;
    }
    @Override
    public List<Object> getChildObjectList(){
        return shiftList;
    }
    @Override
    public void setChildObjectList(List<Object> list){
        shiftList = list;
    }
    public String getShiftTime(){
        return shiftTime;
    }
    public void setShiftTime(String shiftTime){
        this.shiftTime = shiftTime;
    }
    public Object getShift(int position){
        return shiftList.get(position);
    }
    public void addShift(Object item){
        shiftList.add(item);
    }
}
