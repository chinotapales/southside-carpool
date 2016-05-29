package carpool.southside.southsidecarpool;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private int previousPosition = 0;
    private Toolbar toolbar;
    private AHBottomNavigation bottomNavigation;
    private ArrayList<AHBottomNavigationItem> ahBottomNavigationItems = new ArrayList<>();
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
}
