package carpool.southside.southsidecarpool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
    private com.google.api.services.script.Script mService = null;
    private Exception mLastError = null;
    private String request = null;
    private static final String TAG = "MakeRequestTask";
    public GoogleAccountCredential mCredential;
    private Activity mActivity;
    private DatabaseOpenHelper dbHelper;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String[] SCOPES = {"https://www.googleapis.com/auth/spreadsheets"};
    public MakeRequestTask(GoogleAccountCredential credential, String request, Activity activity){
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.script.Script.Builder(transport, jsonFactory, setHttpTimeout(credential))
                .setApplicationName("Google Apps Script Execution API Android Quickstart")
                .build();
        this.request = request;
        mCredential = credential;
        mActivity = activity;
    }
    @Override
    protected List<String> doInBackground(Void... params){
        try{
            Log.d(TAG, "doInBackground: Start request");
            if(request.contains("updateShifts")){
                return getDataFromApi("getShifts");
            }
            return getDataFromApi(request);
        }
        catch(Exception e){
            mLastError = e;
            cancel(true);
            return null;
        }
    }
    private List<String> getDataFromApi(String functionName) throws IOException, GoogleAuthException{
        String scriptId = "MvQzBIvgJGSM9HZKAHcraIMiusy43pzuj";
        List<String> resultList;
        ExecutionRequest request = new ExecutionRequest().setFunction(functionName);
        Log.d(TAG, "Function set. Requesting for execution");
        Operation op = mService.scripts().run(scriptId, request).execute();
        Log.d(TAG, "Function Executed.");
        if(op.getError() != null){
            Log.d(TAG, "Script Error!");
            throw new IOException(getScriptError(op));
        }
        if(op.getResponse() != null && op.getResponse().get("result") != null){
            Log.d(TAG, "Script returned result!");
            resultList = (List<String>) (op.getResponse().get("result"));
            Log.d(TAG, resultList.toString());
            return resultList;
        }
        return null;
    }
    private String getScriptError(Operation op){
        if(op.getError() == null){
            return null;
        }
        Map<String, Object> detail = op.getError().getDetails().get(0);
        List<Map<String, Object>> stacktrace = (List<Map<String, Object>>) detail.get("scriptStackTraceElements");
        java.lang.StringBuilder sb = new StringBuilder("\nScript error message: ");
        sb.append(detail.get("errorMessage"));
        if(stacktrace != null){
            sb.append("\nScript error stacktrace:");
            for(Map<String, Object> elem : stacktrace){
                sb.append("\n  ");
                sb.append(elem.get("function"));
                sb.append(":");
                sb.append(elem.get("lineNumber"));
            }
        }
        sb.append("\n");
        return sb.toString();
    }
    @Override
    protected void onPreExecute(){
    }
    @Override
    protected void onPostExecute(List<String> output){
        Log.d(TAG, request);
        if(output == null){
            Log.d(TAG, "onPostExecute: returned result is null");
        }
        else {
            Log.d(TAG, output.toString());
            if(request.contains("getDirectory")){
                setDirectory(output);
            }
            else if(request.contains("getShifts")){
                setShifts(output);
            }
            else if(request.contains("updateShifts")){
                updateShifts(output);
            }
            else if(request.contains("getAnnouncements")){
                setAnnouncements(output);
            }
        }
    }
    @Override
    protected void onCancelled(){
        if(mLastError != null){
            if(mLastError instanceof GooglePlayServicesAvailabilityIOException){
                showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode());
            }
            else if(mLastError instanceof UserRecoverableAuthIOException){
                mActivity.startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(), LoginActivity.REQUEST_AUTHORIZATION);
            }
            else{
                Log.d(TAG, "The following error occoured: " + mLastError.getMessage());

                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           try{
                           mCredential.getGoogleAccountManager().invalidateAuthToken(mCredential.getToken());
                           mCredential = GoogleAccountCredential.usingOAuth2(mActivity.getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
                           Toast.makeText(mActivity, "Refreshing Token Try Again.", Toast.LENGTH_SHORT).show();
                           }
                           catch(IOException e){
                               Log.w(TAG, e.getMessage());
                           }
                           catch (GoogleAuthException e){
                               Log.w(TAG, e.getMessage());
                           }
                       }
                   }).start();

            }
        }
        else{
            Log.d(TAG, "onCancelled: request cancelled");
        }
    }
    private boolean isDeviceOnline(){
        ConnectivityManager connMgr = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private boolean isGooglePlayServicesAvailable(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(mActivity);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }
    private void acquireGooglePlayServices(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(mActivity);
        if(apiAvailability.isUserResolvableError(connectionStatusCode)){
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(mActivity, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
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
    private void setDirectory(List<String> directoryResult){
        if(directoryResult != null){
            dbHelper = new DatabaseOpenHelper(mActivity);
            dbHelper.deleteAllPeople();
            String name, number, university;
            for (int i = 0; i < directoryResult.size(); i += 3){
                name = directoryResult.get(i);
                number = directoryResult.get(i + 1).toString();
                university = directoryResult.get(i + 2);
                Person person = new Person(name, number, university, 0, 0);
                dbHelper.insertPerson(person);
            }

            Log.i(TAG, "Got data for directory");
        }
    }
    private void setShifts(List<String> shiftsResult){
        if(shiftsResult != null){
            dbHelper = new DatabaseOpenHelper(mActivity);
            dbHelper.deleteAllShifts();
            String day, type, time, provider;
            for(int i = 0; i < shiftsResult.size(); i += 4){
                day = shiftsResult.get(i);
                type = shiftsResult.get(i + 1);
                time = shiftsResult.get(i + 2);
                provider = shiftsResult.get(i + 3);
                Shift shift = new Shift(day, type, time, provider);
                dbHelper.insertShift(shift);
            }
            Log.i(TAG, "Setting Shifts");
            Intent intent = new Intent(mActivity, MainActivity.class);
            mActivity.startActivity(intent);
        }
    }
    private void updateShifts(List<String> shiftsResult){
        if(shiftsResult != null){
            dbHelper = new DatabaseOpenHelper(mActivity);
            dbHelper.deleteAllShifts();
            String day, type, time, provider;
            for(int i = 0; i < shiftsResult.size(); i += 4){
                day = shiftsResult.get(i);
                type = shiftsResult.get(i + 1);
                time = shiftsResult.get(i + 2);
                provider = shiftsResult.get(i + 3);
                Shift shift = new Shift(day, type, time, provider);
                dbHelper.insertShift(shift);
            }
            Log.i(TAG, "Updating Shifts");
        }
    }
    private void setAnnouncements(List<String> announcementResult){
        if(announcementResult != null){
            dbHelper = new DatabaseOpenHelper(mActivity);
            String name, date, message;
            dbHelper.deleteAllAnnouncements();
            for(int i=0; i<announcementResult.size(); i+=3){
                date = announcementResult.get(i);
                name = announcementResult.get(i+1);
                message = announcementResult.get(i+2);
                Announcement announcement = new Announcement(name, date, message);
                dbHelper.insertAnnouncement(announcement);
                Log.i(TAG,"Added new announcement by "+ name);
            }
        }
    }
}