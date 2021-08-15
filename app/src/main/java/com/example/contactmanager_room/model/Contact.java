package com.example.contactmanager_room.model;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//this annotation will make the compiler recognized
// this class as an Entity or a table
// |
// v
@Entity(tableName = "contact_table")
public class Contact {
    //this column will have a primary key & will auto generate it
    @PrimaryKey(autoGenerate = true)
    private int id;

    public Contact() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    //column name
    @ColumnInfo(name = "name")
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name = "occupation")
    private String occupation;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOccupation() {
        return occupation;
    }

    //NonNull will make it guaranteed to not null
    public Contact(@NonNull String name,@NonNull String occupation) {
        this.name = name;
        this.occupation = occupation;
    }
}
