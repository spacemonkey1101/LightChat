package com.example.lightchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    EditText chatEditText ;
    ListView chatListView ;

    ArrayList<String> messages = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    String activeUser = "";
    public void sendChat(View view)
    {
        final ParseObject message = new ParseObject("Message");//for a new class called messages
        message.put("sender" , ParseUser.getCurrentUser().getUsername());
        message.put("recipient" , activeUser);
        message.put("message" , chatEditText.getText().toString());



        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    messages.add(chatEditText.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    //making the text dissapear after we sent it
                    chatEditText.setText("");

                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

         chatEditText = findViewById(R.id.chatEditText);
         chatListView = findViewById(R.id.chatListView);

        Intent intent =getIntent(); // getting intent from UserListActivity
        activeUser = intent.getStringExtra("username");//getting the username of the clicked user
        setTitle("Chat with " + activeUser);

        arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1 , messages);

        chatListView.setAdapter(arrayAdapter);

        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("sender" , ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient", activeUser);

        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo( "recipient", ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("sender" , activeUser);

        //Combining queries into a list
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();

        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);//or queries in parseserver
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null)
                {
                    if (objects.size() >0)
                    {
                        messages.clear();
                        for (ParseObject message : objects)
                        {
                            String messageContent = message.getString("message");
                            if (  !message.getString("sender").equals(  ParseUser.getCurrentUser().getUsername()   ) )
                            {
                                //  append > at the beginning if its from the other user
                                messageContent = " > " + messageContent;

                            }
                            messages.add(messageContent);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }
}
