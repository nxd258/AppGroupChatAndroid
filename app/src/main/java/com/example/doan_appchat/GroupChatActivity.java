package com.example.doan_appchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private ImageButton sendimgaebutton;
    private EditText usermessage;
    private ScrollView scrollView;
    private TextView displaytextmessage;

    private String currentGroupName,currentUserName,currentUserID,currentDate,currentTime;
    private FirebaseAuth mauth;

    private DatabaseReference RootRef,GroupNameRef,GroupMessageRefKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        // Lấy tên nhóm từ Intent và khởi tạo đối tượng Firebase
        currentGroupName=getIntent().getExtras().get("groupname").toString();
        mauth=FirebaseAuth.getInstance();
        currentUserID=mauth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef=FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);


        // Thiết lập giao diện người dùng
        mtoolbar=findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(currentGroupName);

        sendimgaebutton=findViewById(R.id.send_message_button);
        usermessage=findViewById(R.id.input_group_messages);
        scrollView=findViewById(R.id.my_scroll_view);
        displaytextmessage=findViewById(R.id.group_chat_text_display);

        // Lấy thông tin người dùng và thiết lập sự kiện khi nút gửi tin nhắn được nhấn
        GetUserInfo();

        sendimgaebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedMessageInfoToDatabase();
                usermessage.setText("");
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Thiết lập sự kiện lắng nghe cho tin nhắn trong nhóm
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    // Phương thức lưu thông tin tin nhắn vào cơ sở dữ liệu
    private void SavedMessageInfoToDatabase() {
        // Lấy nội dung tin nhắn từ trường nhập liệu
        String inputmessage=usermessage.getText().toString();
        String messagekey=GroupNameRef.push().getKey();
        if(TextUtils.isEmpty(inputmessage))
        {
            Toast.makeText(this,"Please write a message...",Toast.LENGTH_SHORT).show();// Thông báo nếu nội dung tin nhắn trống
        }
        else
        {
            // Nếu có nội dung tin nhắn, lấy thời gian hiện tại
            Calendar calendar =Calendar.getInstance();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            currentDate=simpleDateFormat.format(calendar.getTime());

            SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("hh:mm a");
            currentTime=simpleDateFormat1.format(calendar.getTime());

            // Tạo một HashMap để lưu thông tin của tin nhắn
            HashMap<String,Object> groupmessagekey= new HashMap<>();
            GroupNameRef.updateChildren(groupmessagekey);

            // Tạo key cho tin nhắn trong nhóm và chuyển đến node đó
            GroupMessageRefKey=GroupNameRef.child(messagekey);

            // Tạo HashMap để lưu thông tin chi tiết của tin nhắn
            HashMap<String,Object> messageInfoMap=new HashMap<>();

            // Đặt thông tin của tin nhắn vào HashMap
            messageInfoMap.put("name",currentUserName);
            messageInfoMap.put("message",inputmessage);
            messageInfoMap.put("date",currentDate);
            messageInfoMap.put("time",currentTime);

            GroupMessageRefKey.updateChildren(messageInfoMap);

        }
    }
    // Phương thức lấy thông tin người dùng hiện tại
    private void GetUserInfo() {
        // Lấy thông tin người dùng từ database theo ID người dùng hiện tại
        RootRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Kiểm tra xem dữ liệu có tồn tại hay không
                if(dataSnapshot.exists())
                {
                    // Lấy giá trị tên người dùng từ dữ liệu và gán vào biến currentUserName
                    currentUserName=dataSnapshot.child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // Phương thức hiển thị tin nhắn trong ScrollView
    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {
            String chatdate=(String)((DataSnapshot)iterator.next()).getValue();
            String chatmessage=(String)((DataSnapshot)iterator.next()).getValue();
            String chatname=(String)((DataSnapshot)iterator.next()).getValue();
            String chattime=(String)((DataSnapshot)iterator.next()).getValue();


            displaytextmessage.append(chatname+" :\n"+ chatmessage+"\n"+chattime+"   "+chatdate+"\n\n");

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}