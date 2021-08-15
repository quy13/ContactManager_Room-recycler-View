package com.example.contactmanager_room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactmanager_room.model.Contact;
import com.example.contactmanager_room.model.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NewContact extends AppCompatActivity {

    private int  contactId = 0;
    private Boolean isEdit = false;
    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION_REPLY = "occupation_reply";
    private EditText enterName;
    private EditText enterOccupation;
    private Button   saveInfoButton;
    private Button   deleteButton;
    private Button   updateButton;

    private ContactViewModel contactViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        enterName = findViewById(R.id.enter_name_text);
        enterOccupation = findViewById(R.id.enter_occupation_text);
        saveInfoButton = findViewById(R.id.save_button);

        //creating the view model
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(
                NewContact.this
                        .getApplication())
                .create(ContactViewModel.class);

        if(getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);

            contactViewModel.get(contactId).observe(this, contact -> {
                if(contact != null) {
                    enterName.setText(contact.getName());
                    enterOccupation.setText(contact.getOccupation());
                }
            });
            isEdit = true;

        }

        saveInfoButton.setOnClickListener(v -> {

            Intent replyIntent = new Intent();

            //checking if any of the EditText is empty
            if(!TextUtils.isEmpty(enterName.getText())
                    && !TextUtils.isEmpty(enterOccupation.getText())) {

                String name       = enterName.getText().toString();
                String occupation = enterOccupation.getText().toString();

                //put data inside Intent
                replyIntent.putExtra(NAME_REPLY, name);
                replyIntent.putExtra(OCCUPATION_REPLY, occupation);
                setResult(RESULT_OK, replyIntent);

                //Contact contact = new Contact(name, occupation);

                //insert data
                //ContactViewModel.insert(contact);

            }else {
//              Toast.makeText(this,R.string.empty,Toast.LENGTH_SHORT)
//                        .show();
                setResult(RESULT_CANCELED, replyIntent);
            }
            finish();
        });

        //update button
        updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(v -> { edit(false); });
        //delete button
        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> { edit(true); });

        if(isEdit){
            saveInfoButton.setVisibility(View.GONE);
        }else {
            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }


    }

    private void edit(Boolean isDelete) {
        String name = enterName.getText().toString();
        String occupation = enterOccupation.getText().toString();

        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(occupation)){
            Snackbar.make(enterName, R.string.empty,Snackbar.LENGTH_SHORT)
                    .show();
        }else {
            Contact contact = new Contact();
            contact.setId(contactId);
            contact.setName(name);
            contact.setOccupation(occupation);
            if(isDelete)
                ContactViewModel.delete(contact);
            else
                ContactViewModel.update(contact);
            finish();
        }
    }
}