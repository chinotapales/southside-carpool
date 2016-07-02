package carpool.southside.southsidecarpool;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;

import java.util.ArrayList;
import java.util.Arrays;
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
        //Testing Purposes
        //announcements.add(new Announcement(0, "Chino Tapales", "May 19, 2016", "If you are 115 and have been in carpool for TWO TERMS, you are considered an OLD carpooler and only have to provide ONCE a week :) If you wish to provide twice a week, feel free to :) Any extra shifts will greatly be appreciated! Again deadline for sign up is TOMORROW @11:59PM. Thank you!"));
        //announcements.add(new Announcement(1, "Ed Pertierra", "May 22, 2016", "The carpool schedule will be uploaded here later at 6PM. Please take note.\nThank you!"));
        //announcements.add(new Announcement(2, "Chino Tapales", "May 22, 2016", "Good evening everyone! As of now, there are missing shifts but don't worry! There are still people that haven't signed up yet and are still adjusting. We will also be contacting some providers to change their shifts to balance the schedule in the coming days. Carpool officially begins tomorrow. Good luck finding rides :)"));
        announcementAdapter = new AnnouncementAdapter(getContext(), announcements);
        rvAnnouncements.setAdapter(announcementAdapter);
        aSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                ((MainActivity)getActivity()).updateData(getActivity());
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
        List<Announcement> announcements = new ArrayList<>();
        db = new DatabaseOpenHelper(getContext());
        announcements = db.getArrayListAnnouncements();
        //Testing Purposes
        //announcements.add(new Announcement(0, "Chino Tapales", "May 19, 2016", "If you are 115 and have been in carpool for TWO TERMS, you are considered an OLD carpooler and only have to provide ONCE a week :) If you wish to provide twice a week, feel free to :) Any extra shifts will greatly be appreciated! Again deadline for sign up is TOMORROW @11:59PM. Thank you!"));
        //announcements.add(new Announcement(1, "Ed Pertierra", "May 22, 2016", "The carpool schedule will be uploaded here later at 6PM. Please take note.\nThank you!"));
        //announcements.add(new Announcement(2, "Chino Tapales", "May 22, 2016", "Good evening everyone! As of now, there are missing shifts but don't worry! There are still people that haven't signed up yet and are still adjusting. We will also be contacting some providers to change their shifts to balance the schedule in the coming days. Carpool officially begins tomorrow. Good luck finding rides :)"));

        announcementAdapter = new AnnouncementAdapter(getContext(), announcements);
        rvAnnouncements.setAdapter(announcementAdapter);
        checkEmpty();
    }

}
