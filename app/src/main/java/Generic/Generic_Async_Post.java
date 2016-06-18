package Generic;

/**
 * Created by kuush on 6/17/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import Enum.TaskType;
import Abstract.AsyncTaskListener;
import HTTP.HttpManager;

public class Generic_Async_Post extends AsyncTask<String,Void ,String> {


    String outputStr;
    ProgressDialog dialog;
    Context context;
    AsyncTaskListener taskListener;
    TaskType taskType;

    public Generic_Async_Post(Context context, AsyncTaskListener taskListener, TaskType taskType){
        this.context = context;
        this.taskListener = taskListener;
        this.taskType = taskType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Loading", "Connecting to Server .. Please Wait", true);
        dialog.setCancelable(false);
    }

    @Override
    protected String doInBackground(String... params) {
        String Data_From_Server = null;
        HttpManager http_manager = null;
        try{
            http_manager = new HttpManager();
            if(params[0].equalsIgnoreCase("getParkingTransaction_JSON")){
                Data_From_Server = http_manager.PostData_Vehicle_IN(params);
                return Data_From_Server;
            }else if(params[0].equalsIgnoreCase("getParkingOut_JSON")){
                Data_From_Server = http_manager.PostData_Vehicle_OUT(params);
                return Data_From_Server;
            }else if(params[0].equalsIgnoreCase("getConfirmPayment_JSON")){
                Data_From_Server = http_manager.PostData_Vehicle_OUT_Confirm(params);
                return Data_From_Server;
            }else if(params[0].equalsIgnoreCase("getConfirmParkinStatus_JSON")){
                Data_From_Server = http_manager.PostData_Vehicle_OUT_Confirm(params);
                return Data_From_Server;
            }

            else{
                return "Error";
            }

        }catch(Exception e){
            return e.getLocalizedMessage().toString().trim();
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        taskListener.onTaskCompleted(result, taskType);
        dialog.dismiss();
    }



}