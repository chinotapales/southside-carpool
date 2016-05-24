package carpool.southside.southsidecarpool;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private Button loginButton;
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private Button mCallApiButton;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Apps Script Execution API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { "https://www.googleapis.com/auth/spreadsheets" };
    private static final String TAG = "LOGIN";
    private List<String> directoryResult = null;
    private DatabaseOpenHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button)findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResultsFromApi();
            }
        });
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        if (!EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)){
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }
        }
    }


    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Toast.makeText(LoginActivity.this, "No network connection available", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "getResultsFromApi: No network connection available");
        } else {
            new MakeRequestTask(mCredential,"getDirectory").execute();
            new MakeRequestTask(mCredential,"getShifts").execute();
        }


    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                LoginActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.script.Script mService = null;
        private Exception mLastError = null;
        private String request = null;

        public MakeRequestTask(GoogleAccountCredential credential, String request) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.script.Script.Builder(
                    transport, jsonFactory, setHttpTimeout(credential))
                    .setApplicationName("Google Apps Script Execution API Android Quickstart")
                    .build();
            this.request = request;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                Log.d(TAG, "doInBackground: Start request");
               return getDataFromApi(request);
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi(String functionName)
                throws IOException, GoogleAuthException {
            String scriptId = "MvQzBIvgJGSM9HZKAHcraIMiusy43pzuj";

            List<String> resultList;

            // Create an execution request object.
            ExecutionRequest request = new ExecutionRequest()
                    .setFunction(functionName);
            Log.d(TAG, "Function set. Requesting for execution");
            // Make the request.
            Operation op =
                    mService.scripts().run(scriptId, request).execute();
            Log.d(TAG, "Function Executed.");
            // Print results of request.
            if (op.getError() != null) {
                Log.d(TAG, "Script Error!");
                throw new IOException(getScriptError(op));
            }
            if (op.getResponse() != null &&
                    op.getResponse().get("result") != null) {
                Log.d(TAG, "Script returned result!");
                resultList=
                        (List<String>) (op.getResponse().get("result"));
                Log.d(TAG, resultList.toString());
                return resultList;
            }
            return null;
        }

        private String getScriptError(Operation op) {
            if (op.getError() == null) {
                return null;
            }

            Map<String, Object> detail = op.getError().getDetails().get(0);
            List<Map<String, Object>> stacktrace =
                    (List<Map<String, Object>>)detail.get("scriptStackTraceElements");

            java.lang.StringBuilder sb =
                    new StringBuilder("\nScript error message: ");
            sb.append(detail.get("errorMessage"));

            if (stacktrace != null) {
                // There may not be a stacktrace if the script didn't start
                // executing.
                sb.append("\nScript error stacktrace:");
                for (Map<String, Object> elem : stacktrace) {
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
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(List<String> output) {
            Log.d(TAG, request);
            if (output == null) {
                Log.d(TAG, "onPostExecute: returned result is null");

            } else {
               // output.add(0, "Data retrieved using the Google Apps Script Execution API:");
                Log.d(TAG,output.toString());
                if(request.contains("getDirectory")){
                    setDirectory(output);
                }
                else if(request.contains("getShifts")){
                    setShifts(output);
                }


            }
        }


        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            LoginActivity.REQUEST_AUTHORIZATION);
                } else {
                    Log.d(TAG, "The following error occoured: " + mLastError.getMessage());
                    try {
                        mCredential.getGoogleAccountManager().invalidateAuthToken(mCredential.getToken());
                        mCredential = GoogleAccountCredential.usingOAuth2(
                                getApplicationContext(), Arrays.asList(SCOPES))
                                .setBackOff(new ExponentialBackOff());
                        Toast.makeText(LoginActivity.this,"Refreshing Token Try Again.", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        Log.w(TAG, e.getMessage());
                    } catch (GoogleAuthException e) {
                        Log.w(TAG, e.getMessage());
                    }
                }
            } else {
                Log.d(TAG, "onCancelled: request cancelled");
            }
        }
    }

    private static HttpRequestInitializer setHttpTimeout(
            final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest)
                    throws java.io.IOException {
                requestInitializer.initialize(httpRequest);
                httpRequest.setReadTimeout(380000);
            }
        };
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.d(TAG, "Please Install Google Play Services ");

                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    private void setDirectory(List<String> directoryResult) {
        if (directoryResult != null) {
            dbHelper = new DatabaseOpenHelper(this);
            dbHelper.deleteAllPeople();
            String name, number, university;
            for (int i = 0; i < directoryResult.size(); i += 3) {
                name = directoryResult.get(i);
                number = directoryResult.get(i + 1).toString();
                university = directoryResult.get(i + 2);
                if(!name.equals("")) {
                    Person person = new Person(name, number, university, 0, 0);
                    dbHelper.insertPerson(person);
                }
                Log.i(TAG, "added new person " + name);
            }
        }
    }
    private void setShifts(List<String> shiftsResult) {
        if (shiftsResult != null) {
            dbHelper = new DatabaseOpenHelper(this);
            dbHelper.deleteAllShifts();
            String day, type, time, provider;
            for (int i = 0; i < shiftsResult.size(); i += 4) {
                day = shiftsResult.get(i);
                type = shiftsResult.get(i + 1);
                time = shiftsResult.get(i + 2);
                provider = shiftsResult.get(i + 3);
                Shift shift = new Shift(day, type, time, provider);
                dbHelper.insertShift(shift);
                Log.i(TAG, "added new shift by " + provider);
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}

