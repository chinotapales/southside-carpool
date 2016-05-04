package carpool.southside.southsidecarpool;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.github.ivbaranov.mli.MaterialLetterIcon;

public class ShiftChildViewHolder extends ChildViewHolder{
    public TextView shiftName, shiftNumber;
    RelativeLayout shiftContainer;
    MaterialLetterIcon shiftImage;
    public ShiftChildViewHolder(View itemView){
        super(itemView);
        shiftName = (TextView) itemView.findViewById(R.id.directory_name);
        shiftNumber = (TextView) itemView.findViewById(R.id.directory_number);
        shiftImage = (MaterialLetterIcon) itemView.findViewById(R.id.directory_image);
        shiftContainer = (RelativeLayout) itemView.findViewById(R.id.directory_container);
    }
}
