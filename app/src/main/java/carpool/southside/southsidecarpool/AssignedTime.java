package carpool.southside.southsidecarpool;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import java.util.List;

public class AssignedTime implements ParentObject{
    private String shiftTime;
    private List<Object> shiftList;
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
    public boolean addShift(Shift item){
        shiftList.add(item);
        return true;
    }
    public Object getShift(int position){
        return shiftList.get(position);
    }
}
