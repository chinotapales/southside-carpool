package carpool.southside.southsidecarpool;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.ExponentialBackOff;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private SignInButton loginButton;
    private EditText passcode;
    private TextInputLayout inputLayoutPasscode;
    private GoogleAccountCredential mCredential;
    private final int REQUEST_PERMISSION_CODE = 5678;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { "https://www.googleapis.com/auth/spreadsheets" };
    private static final String TAG = "LOGIN";
    private DatabaseOpenHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DatabaseOpenHelper(this);
        dbHelper.initPasscode();
        passcode = (EditText) findViewById(R.id.enter_passcode);
        passcode.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        inputLayoutPasscode = (TextInputLayout) findViewById(R.id.enter_layout_passcode);
        loginButton = (SignInButton) findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(submitForm()){
                    getResultsFromApi();
                }
            }
        });
        loginButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_LIGHT);
        requestPermissions();
        mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
        if(EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)){
            Log.d(TAG, PREF_ACCOUNT_NAME);
            SharedPreferences settings = getSharedPreferences("Settings", 0);
            String accountName = settings.getString(PREF_ACCOUNT_NAME, null);
            if(accountName != null){
                mCredential.setSelectedAccountName(accountName);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("accountName", accountName);
                startActivity(intent);
                finish();
            }
        }
    }
    private boolean submitForm(){
        if(passcode.getText().toString().trim().isEmpty()){
            inputLayoutPasscode.setError("Enter a Passcode");
            return false;
        }
        else if(passcode.getText().length() != 6){
            inputLayoutPasscode.setError("Passcode Must be 6 Digits Only");
            return false;
        }
        String code = passcode.getText().toString().trim();
        boolean success = dbHelper.isPasscodeCorrect(code);
        if(!success){
            inputLayoutPasscode.setError("Incorrect Passcode");
            return false;
        }
        else{
            return true;
        }
    }
    private void getResultsFromApi(){
        if(!isGooglePlayServicesAvailable()){
            acquireGooglePlayServices();
        }
        else if(mCredential.getSelectedAccountName() == null){
            chooseAccount();
        }
        else if(!isDeviceOnline()){
            Toast.makeText(LoginActivity.this, "No Network Connection", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "getResultsFromApi: No Network Connection");
        }
        else{
            new MakeRequestTask(mCredential,"getDirectory", this).execute();
            new MakeRequestTask(mCredential,"getAnnouncements", this).execute();
            new MakeRequestTask(mCredential,"getShifts", this).execute();
        }
    }
    private boolean isDeviceOnline(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private boolean isGooglePlayServicesAvailable(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }
    private void acquireGooglePlayServices(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if(apiAvailability.isUserResolvableError(connectionStatusCode)){
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(LoginActivity.this, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
    private static HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer){
        return new HttpRequestInitializer(){
            @Override
            public void initialize(HttpRequest httpRequest) throws java.io.IOException{
                requestInitializer.initialize(httpRequest);
                httpRequest.setReadTimeout(380000);
            }
        };
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms){
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms){
    }
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount(){
        if(EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)){
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if(accountName != null){
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            }
            else{
                startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
        }
        else{
            EasyPermissions.requestPermissions(this, "This app needs to access your Google account (via Contacts).", REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if(resultCode != RESULT_OK){
                    Log.d(TAG, "Please Install Google Play Services ");
                }
                else{
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if(resultCode == RESULT_OK && data != null && data.getExtras() != null){
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if(accountName != null){
                        SharedPreferences settings = getSharedPreferences("Settings",0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if(resultCode == RESULT_OK){
                    getResultsFromApi();
                }
                break;
        }
    }
    private void setDirectory(List<String> directoryResult){
        if(directoryResult != null){
            dbHelper = new DatabaseOpenHelper(this);
            dbHelper.deleteAllPeople();
            String name, number, university;
            for(int i = 0; i < directoryResult.size(); i += 3){
                name = directoryResult.get(i);
                number = directoryResult.get(i + 1).toString();
                university = directoryResult.get(i + 2);
                Person person = new Person(name, number, university, 0, 0);
                dbHelper.insertPerson(person);
                Log.i(TAG, "Added new person " + name);
            }
        }
    }
    private void setShifts(List<String> shiftsResult){
        if(shiftsResult != null){
            dbHelper = new DatabaseOpenHelper(this);
            dbHelper.deleteAllShifts();
            String day, type, time, provider;
            for(int i = 0; i < shiftsResult.size(); i += 4){
                day = shiftsResult.get(i);
                type = shiftsResult.get(i + 1);
                time = shiftsResult.get(i + 2);
                provider = shiftsResult.get(i + 3);
                Shift shift = new Shift(day, type, time, provider);
                dbHelper.insertShift(shift);
                Log.i(TAG, "Added new shift by " + provider);
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    private void requestPermissions(){
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            int hasSMSPermission = checkSelfPermission(Manifest.permission.SEND_SMS);
            int hasAccountPermission = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            List<String> permissions = new ArrayList<>();
            if(hasCallPermission != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.CALL_PHONE);
            }
            if(hasSMSPermission != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.SEND_SMS);
            }
            if(hasAccountPermission != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.GET_ACCOUNTS);
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
                        else if(permissions[i].equals(Manifest.permission.GET_ACCOUNTS)){
                            deniedPermissions.add("Accounts");
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

