package mchehab.com.customlistviewimage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int RESULT_ADD = 101;
    private final int RESULT_EDIT = 102;

    private ListView listView;
    private ListViewAdapter listViewAdapter;

    private List<Person> listPerson;
    private List<Person> listCompleteData;

    private ProgressBar progressBar;
    private AsyncTaskWait asyncTaskWait;

    private FloatingActionButton floatingActionButtonAdd;

    private MenuItem menuItemSearch;
    private MenuItem menuItemDelete;

    private int personEditPosition;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            addMoreItems();
            Log.d("asyncstatus", "status = " + asyncTaskWait.getStatus().name());
        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("result");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd);

        listView = findViewById(R.id.listView);
        listCompleteData = readListFromFile();
        listPerson = new ArrayList<>(listCompleteData.subList(0, 10));

        listViewAdapter = new ListViewAdapter(this, listPerson);
        listView.setAdapter(listViewAdapter);
        setListViewFooter();
        setListOnScrollListener();
        setListOnLongPressListener();
        setFloatingActionButtonAddListener();
        setListViewOnItemClickListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            Person person = Parcels.unwrap(bundle.getParcelable("person"));
            if(requestCode == RESULT_ADD){
                listViewAdapter.addItem(person);
                listViewAdapter.notifyDataSetChanged();
            }else if(requestCode == RESULT_EDIT){
                listViewAdapter.setItem(personEditPosition, person);
                listViewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);

        menuItemSearch = menu.findItem(R.id.action_search);
        menuItemDelete = menu.findItem(R.id.action_delete);

        menuItemDelete.setVisible(false);

        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBar.setVisibility(View.GONE);
                listViewAdapter.filter(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            listViewAdapter.filter("");
            return false;
        });

        menuItemDelete.setOnMenuItemClickListener(item -> {
            listViewAdapter.removeSelectedPersons();
            listViewAdapter.notifyDataSetChanged();
            if(listViewAdapter.getCount() <= 5){
                loadData();
            }
            showDeleteMenu(false);
            return true;
        });

        return true;
    }

    private void setListViewOnItemClickListener(){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Person person = (Person)listViewAdapter.getItem(position);
            Intent intent = new Intent(this, ActivityAdd.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("person", Parcels.wrap(person));
            intent.putExtras(bundle);
            personEditPosition = position;
            startActivityForResult(intent, RESULT_EDIT);
        });
    }

    private void showDeleteMenu(boolean show){
        menuItemDelete.setVisible(show);
        menuItemSearch.setVisible(!show);
    }

    private void setFloatingActionButtonAddListener(){
        floatingActionButtonAdd.setOnClickListener(e -> {
            Intent intent = new Intent(this, ActivityAdd.class);
            startActivityForResult(intent, RESULT_ADD);
        });
    }

    private void setListViewFooter(){
        View view = LayoutInflater.from(this).inflate(R.layout.footer_listview_progressbar, null);
        progressBar = view.findViewById(R.id.progressBar);
        listView.addFooterView(progressBar);
    }

    private void setListOnScrollListener(){
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE && listView.getLastVisiblePosition() ==
                        listPerson.size()){
                    loadData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });
    }

    private void loadData(){
        if(asyncTaskWait == null || asyncTaskWait.getStatus() != AsyncTask.Status
                .RUNNING){
            progressBar.setVisibility(View.VISIBLE);
            asyncTaskWait = new AsyncTaskWait(new WeakReference<Context>(MainActivity
                    .this));
            asyncTaskWait.execute();
        }
    }

    private void setListOnLongPressListener(){
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            listViewAdapter.handleLongPress(position, view);
            if(listViewAdapter.getListPersonsSelected().size() > 0){
                showDeleteMenu(true);
            }else{
                showDeleteMenu(false);
            }
            return true;
        });
    }

    private void addMoreItems(){
        int size = listPerson.size();
        for(int i=1;i<=10;i++){
            if((size + i) < listCompleteData.size()){
                listViewAdapter.addItem(listCompleteData.get(size + i));
            }
        }
        listViewAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    private List<Person> readListFromFile(){
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets()
                    .open("persons.txt")));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            Gson gson = new Gson();
            return gson.fromJson(stringBuilder.toString(), new TypeToken<List<Person>>(){}
            .getType());
        }catch (IOException exception){
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }
}