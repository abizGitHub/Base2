package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.ChatAdapter;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Message;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by abiz on 5/26/2019.
 */

public class MessageAct extends BaseActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private ImageButton sendBtn;
    private ChatAdapter adapter;
    private ArrayList<Message> chatHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initControls();
    }


    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);

        /*TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        //companionLabel.setText("My Buddy");
         */
        loadChatHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText.trim())) {
                    return;
                }
                Message chatMessage = new Message();
                chatMessage.setBody(messageText);
                chatMessage.setRegisterDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setType(Message.SENT);
                messageET.setText("");
                displayMessage(chatMessage);
                getDbHelper().insertMessage(chatMessage, Message.SENT);
            }
        });

    }

    private void loadChatHistory() {
        chatHistory = getDbHelper().getAllMessages();
        /*
        ChatMessage msg = new Message();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hi");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("How r u doing???");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);
        */
        adapter = new ChatAdapter(MessageAct.this, new ArrayList<Message>());
        messagesContainer.setAdapter(adapter);

        for (int i = 0; i < chatHistory.size(); i++) {
            Message message = chatHistory.get(i);
            displayMessage(message);
        }
    }


    public void displayMessage(Message message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }


    @Override
    protected void doStaredTasks() {
    }

    @Override
    protected ArrayAdapter getListAdapter() {
        return null;
    }

    @Override
    protected ArrayList<String> getGeneralTitles() {
        return null;
    }

    @Override
    protected ArrayList<GeneralModel> getGeneralList() {
        return null;
    }

    @Override
    public void onStarChanged(int position, boolean checked) {
    }
}
