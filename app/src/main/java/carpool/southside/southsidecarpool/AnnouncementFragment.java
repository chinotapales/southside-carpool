package carpool.southside.southsidecarpool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementFragment extends Fragment{
    private RecyclerView rvAnnouncements;
    private AnnouncementAdapter announcementAdapter;
    private SwipeRefreshLayout aSwipeRefreshLayout;
    private TextView emptyView;
    private static final String TAG = "ANNOUNCEMENT FRAGMENT";
    private DatabaseOpenHelper db;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.announcement_view, container, false);
        aSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.announcement_swipe_refresh_layout);
        aSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        emptyView = (TextView) v.findViewById(R.id.empty_announcements_view);
        rvAnnouncements = (RecyclerView) v.findViewById(R.id.announcement_recycler_view);
        List<Announcement> announcements = new ArrayList<>();
        announcementAdapter = new AnnouncementAdapter(getContext(), announcements);
        rvAnnouncements.setAdapter(announcementAdapter);
        aSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                ((MainActivity)getActivity()).updateData(getActivity(),aSwipeRefreshLayout);
            }
        });
        rvAnnouncements.setLayoutManager(new LinearLayoutManager(v.getContext()));
        checkEmpty();
        return v;
    }
    private void checkEmpty(){
        if(announcementAdapter.getItemCount() > 0){
            rvAnnouncements.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        else{
            rvAnnouncements.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        List<Announcement> announcements;
        db = new DatabaseOpenHelper(getContext());
        announcements = db.getArrayListAnnouncements();announcementAdapter = new AnnouncementAdapter(getContext(), announcements);
        rvAnnouncements.setAdapter(announcementAdapter);
        checkEmpty();
    }
}
