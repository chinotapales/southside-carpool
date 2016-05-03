package carpool.southside.southsidecarpool;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

public class ShiftParentViewHolder extends ParentViewHolder{
    public RelativeLayout scheduleParentContainer;
    public TextView scheduleParentName;
    public ImageView expandButton;
    public ShiftParentViewHolder(View itemView){
        super(itemView);
        scheduleParentContainer = (RelativeLayout) itemView.findViewById(R.id.schedule_parent_container);
        scheduleParentName = (TextView) itemView.findViewById(R.id.schedule_parent_name);
        expandButton = (ImageView) itemView.findViewById(R.id.schedule_expand_button);
    }
}