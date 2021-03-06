package com.hackdtu.healthhistory.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.adapter.CustomList;
import com.hackdtu.healthhistory.model.Diseases;
import com.hackdtu.healthhistory.model.DiseasesHistory;
import com.hackdtu.healthhistory.model.User;
import com.hackdtu.healthhistory.model.UserHistoryList;
import com.hackdtu.healthhistory.network.NetworkCall;
import com.hackdtu.healthhistory.network.NetworkCall2;
import com.hackdtu.healthhistory.network.NetworkCall3;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiseaseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_list);
        new ShowList().execute();
    }

    public class ShowList extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... objects) {
            SuperPrefs superPrefs=new SuperPrefs(DiseaseListActivity.this);

            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("adhaar_card",superPrefs.getString("adhaar_card"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NetworkCall2 networkCall=new NetworkCall2();
            try {
                String response=networkCall.run(Constants.DISEASE_LIST_URL);
                return response;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            if(jsonResponse==null)
            {
                // Net not present
                Log.e("onPostExecute: ","no response" );
            }
            else
            {
                ArrayList<Diseases> diseasesList=new ArrayList<>();
                Gson gson=new GsonBuilder().create();
                try {
                    JSONArray jsonArray=new JSONArray(jsonResponse);


                    for(int i=0;i<jsonArray.length();i++)
                    {
                        Diseases object=gson.fromJson(jsonArray.getString(i),Diseases.class);
                        diseasesList.add(object);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("onPostExecute: ",jsonResponse );
                CustomList adapter=new CustomList(DiseaseListActivity.this,diseasesList);
                ListView listView=(ListView)findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }

            /*
            super.onPostExecute(jsonResponse);
            if(jsonResponse==null)
            {
                // Net not present
                Log.e("onPostExecute: ", "No response");
            }
            else
            {
                Gson gson=new GsonBuilder().create();
                DiseasesHistory diseasesHistory=gson.fromJson(jsonResponse,DiseasesHistory.class);
                ArrayList<Diseases> diseasesArrayList=new ArrayList<>(diseasesHistory.getDiseasesList().size());
                CustomList adapter=new CustomList(DiseaseListActivity.this,diseasesArrayList);
                ListView listView=(ListView)findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }
            Log.e("onPostExecute: ", jsonResponse);*/
        }
    }
}
