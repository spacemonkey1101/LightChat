package com.example.lightchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    ListView listView ;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("User List");

        listView = findViewById(R.id.userListView);

        //what happens when we click on the users in the list ---> we are setting that up ,,,,,the chat activity
        //when the user clicks we jump to the chat activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext() , ChatActivity.class);
                intent.putExtra("username" , arrayList.get(position));
                startActivity(intent);
            }
        });

        arrayList.clear();


        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 , arrayList);

        listView.setAdapter(arrayAdapter);

        ParseQuery<ParseUser> parseQuery =  ParseUser.getQuery();
        //display the username of every user except the current user
        parseQuery.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null)
                {
                    if (objects.size() > 0)
                    {
                        for (ParseUser user : objects)
                        {
                            arrayList.add(user.getUsername());
                        }

                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}
