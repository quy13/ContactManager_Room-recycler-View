package com.example.contactmanager_room;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.contactmanager_room.adapter.RecyclerViewAdapter;
import com.example.contactmanager_room.model.Contact;
import com.example.contactmanager_room.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {

    private static final String TAG = "Clicked";
    public static final String CONTACT_ID = "contact_id";
    //    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Contact> contactList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup the recycler view
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //creating the view model
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this
                .getApplication())
                .create(ContactViewModel.class);



        //getting the data from the contact view model
        //& observe when the data is change/update
        contactViewModel.getAllContact().observe(this, contacts -> {

                //      setup adapter
                //change to list instead of live data because we are getting a list
                recyclerViewAdapter = new RecyclerViewAdapter(contacts,MainActivity.this,this);
                recyclerView.setAdapter(recyclerViewAdapter);

        });



        //setting the FloatingActionButton to navigate to NewContact Activity
        FloatingActionButton fab =findViewById(R.id.add_contact_fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);

//          this method is deprecated won't use
//          startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
            activityResultLauncher.launch(intent);
        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
//            Log.d("TAG", "onActivityResult: " + data.getStringExtra(NewContact.NAME_REPLY));
//            Log.d("TAG", "onActivityResult: " + data.getStringExtra(NewContact.OCCUPATION_REPLY));
//        }
//    }


    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK){
                    Intent intent = result.getData();
                    assert intent != null;

                    String name = intent.getStringExtra(NewContact.NAME_REPLY);
                    String occupation = intent.getStringExtra(NewContact.OCCUPATION_REPLY);

                    Contact contact = new Contact(name, occupation);
                    ContactViewModel.insert(contact);
                }
            });

    @Override
    public void onContactClick(int position) {
        Contact contact = Objects.requireNonNull(contactViewModel.allContact.getValue()).get(position);
        Log.d(TAG, "onContactClick: " + contact.getId());

        Intent intent = new Intent(MainActivity.this,NewContact.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);

    }

}