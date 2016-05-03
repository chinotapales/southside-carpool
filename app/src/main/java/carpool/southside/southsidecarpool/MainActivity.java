package carpool.southside.southsidecarpool;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private final int REQUEST_PERMISSION_CODE = 5678;
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
        ahBottomNavigationItems.add(new AHBottomNavigationItem("To School", R.drawable.school_bound));
        ahBottomNavigationItems.add(new AHBottomNavigationItem("Back Home", R.drawable.home_bound));
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.addItems(ahBottomNavigationItems);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setAccentColor(Color.parseColor("#4CAF50"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setCurrentItem(0);
        final ScheduleFragment schedule = new ScheduleFragment();
        final DirectoryFragment directory = new DirectoryFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.current_frame, directory, directory.getClass().getName())
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
                    if(previousPosition == 2 || previousPosition == 1){
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
                    previousPosition = 1;
                }
                else if(position == 2){
                    if(previousPosition == 1|| previousPosition == 2){
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
                    previousPosition = 2;
                }
            }
        });
        requestPermissions();
    }
    private void requestPermissions(){
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            int hasSMSPermission = checkSelfPermission(Manifest.permission.SEND_SMS);
            List<String> permissions = new ArrayList<>();
            if(hasCallPermission != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.CALL_PHONE);
            }
            if(hasSMSPermission != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.SEND_SMS);
            }
            if(!permissions.isEmpty()){
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_PERMISSION_CODE);
            }
        }
        else return;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        String message = "";
        List<String> deniedPermissions = new ArrayList<>();
        switch(requestCode){
            case REQUEST_PERMISSION_CODE:{
                for(int i = 0; i < permissions.length; i++){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    }
                    else if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                        if(permissions[i].equals(Manifest.permission.SEND_SMS)){
                            deniedPermissions.add("SMS");
                        }
                        else if(permissions[i].equals(Manifest.permission.CALL_PHONE)){
                            deniedPermissions.add("Phone");
                        }
                    }
                }
                for(int i = 0; i < deniedPermissions.size(); i++){
                    message += deniedPermissions.get(i) + ", ";
                }
                if(message.length() > 0){
                    message = message.substring(0, message.length() - 2) + " Denied - Features May Not Work";
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
            }
            break;
            default:{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
