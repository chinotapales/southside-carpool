package carpool.southside.southsidecarpool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity{
    private int previousPosition = 0;
    private Toolbar toolbar;
    private AHBottomNavigation bottomNavigation;
    private ArrayList<AHBottomNavigationItem> ahBottomNavigationItems = new ArrayList<>();
    private static final String TAG = "MAIN";
    private static final String[] SCOPES = { "https://www.googleapis.com/auth/spreadsheets" };
    private static final String PREF_ACCOUNT_NAME = "accountName";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        ahBottomNavigationItems.add(new AHBottomNavigationItem("People", R.drawable.directory));
        ahBottomNavigationItems.add(new AHBottomNavigationItem("Favorites", R.drawable.favorites));
        ahBottomNavigationItems.add(new AHBottomNavigationItem("Announcements", R.drawable.announcements));
        ahBottomNavigationItems.add(new AHBottomNavigationItem("To School", R.drawable.school_bound));
        ahBottomNavigationItems.add(new AHBottomNavigationItem("Back Home", R.drawable.home_bound));
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.addItems(ahBottomNavigationItems);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setAccentColor(Color.parseColor("#4CAF50"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setCurrentItem(2);
        final ScheduleFragment schedule = new ScheduleFragment();
        final DirectoryFragment directory = new DirectoryFragment();
        final FavoritesFragment favorites = new FavoritesFragment();
        final AnnouncementFragment announcements = new AnnouncementFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.current_frame, announcements, announcements.getClass().getName())
                .commit();
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position, boolean wasSelected){
                if(position == 0){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.current_frame, directory, directory.getClass().getName())
                            .commit();
                    previousPosition = 0;
                }
                else if(position == 1){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.current_frame, favorites, favorites.getClass().getName())
                            .commit();
                    previousPosition = 1;
                }
                else if(position == 2){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.current_frame, announcements, announcements.getClass().getName())
                            .commit();
                    previousPosition = 2;
                }
                else if(position == 3){
                    if(previousPosition == 4 || previousPosition == 3){
                        schedule.setType("ToSchool");
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putString("trip_id", "ToSchool");
                        schedule.setArguments(bundle);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.current_frame, schedule, schedule.getClass().getName())
                                .commit();
                    }
                    previousPosition = 3;
                }
                else if(position == 4){
                    if(previousPosition == 3|| previousPosition == 4){
                        schedule.setType("BackHome");
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putString("trip_id", "BackHome");
                        schedule.setArguments(bundle);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.current_frame, schedule, schedule.getClass().getName())
                                .commit();
                    }
                    previousPosition = 4;
                }
            }
        });

    }
    public void updateData(Activity activity, final SwipeRefreshLayout swipeRefreshLayout){
                SharedPreferences settings = activity.getSharedPreferences("Settings", 0);
                String accountName = settings.getString(PREF_ACCOUNT_NAME, null);
                GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(activity.getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
                mCredential.setSelectedAccountName(accountName);
                if(!isDeviceOnline(activity)){
                    Toast.makeText(activity, "No network connection available", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "getResultsFromApi: No network connection available");
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    final DatabaseOpenHelper db = new DatabaseOpenHelper(activity.getApplicationContext());
                    final ArrayList<String> riders = db.getArrayListFavoriteRiders();
                    Log.i(TAG, "fave riders size: "+riders.size());
                    final ArrayList<String> providers = db.getArrayListFavoriteProviders();
                    Log.i(TAG, "fave providers size: "+providers.size());
                    new MakeRequestTask(mCredential, "getDirectory", activity).execute();
                    new MakeRequestTask(mCredential, "getAnnouncements", activity).execute();
                    new MakeRequestTask(mCredential, "updateShifts", activity).execute();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            db.updateFavorites(riders, providers);
                        }
                    }, 5000);
                }
    }
    private boolean isDeviceOnline(Activity activity){
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}