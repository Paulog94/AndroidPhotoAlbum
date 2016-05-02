package group53.android53;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Albumlist extends AppCompatActivity {

    private static final int  ADD_REQUEST_CODE = 1;
    private static final int  EDIT_REQUEST_CODE = 2;

    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private Button btnAdd;
    private Button btnEdit;
    private Button btnDelete;
    private ListView listView;
    ArrayAdapter<Album> adapter;

    static int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumlist);

        listView = (ListView) findViewById(R.id.listView);
        load();

        adapter = new ArrayAdapter<Album>(this,R.layout.support_simple_spinner_dropdown_item,AlbumList);
        listView.setAdapter(adapter);
    }

    public void Edit(View view){
        Log.d("Edit","Index: "+listView.getCheckedItemPosition());

        if(listView.getCheckedItemPosition() >= 0){
            Intent intent = new Intent(getApplicationContext(), EditAlbum.class);
            intent.putExtra("AlbumList", AlbumList);
            String ogName = AlbumList.get(listView.getCheckedItemPosition()).getName();
            intent.putExtra("AlbumName", ogName);
            int i = listView.getCheckedItemPosition();
            intent.putExtra("index",i);
            startActivityForResult(intent, EDIT_REQUEST_CODE);
        }
        else
            Toast.makeText(getApplicationContext(),"No Album Selected",Toast.LENGTH_LONG).show();


    }

    public void Add(View v){
        Intent intent = new Intent(getApplicationContext(),AddAlbum.class);
        intent.putExtra("AlbumList",AlbumList);
        startActivityForResult(intent,ADD_REQUEST_CODE);
    }

    public void Delete(View view){
        if(!AlbumList.isEmpty()) {
            AlbumList.remove(listView.getCheckedItemPosition());
            adapter.notifyDataSetChanged();
            store();
        }
        else{
            Toast.makeText(getApplicationContext(),"No Album Selected",Toast.LENGTH_LONG).show();
        }
    }

    public void Open(View view){

        if(listView.getCheckedItemPosition() >= 0){
            Intent intent = new Intent(getApplicationContext(), ImageGallery.class);
            intent.putExtra("AlbumList", AlbumList);
            int i = listView.getCheckedItemPosition();
            intent.putExtra("index",i);
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(),"No Album Selected",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Album a = (Album) data.getSerializableExtra("New Album");
                AlbumList.add(a);
                adapter.notifyDataSetChanged();
                store();
                Log.d("Add Result","Ok");
            }
        }
        else if(requestCode == EDIT_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                String n = data.getStringExtra("NewName");
                int i = data.getIntExtra("index",0);
                AlbumList.get(i).setName(n);
                adapter.notifyDataSetChanged();
                store();
            }
        }
    }

    public void store() {
        try {
            FileOutputStream fos = this.getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AlbumList);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        FileInputStream fis = null;
        try {
            fis = this.getApplicationContext().openFileInput(filename);

            ObjectInputStream is = null;
            try {
                is = new ObjectInputStream(fis);

                try {
                    AlbumList = (ArrayList<Album>) is.readObject();
                    is.close();
                    fis.close();
                } catch (ClassNotFoundException e) {
                    AlbumList = new ArrayList<Album>();
                }
            } catch (IOException e) {
                AlbumList = new ArrayList<Album>();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}