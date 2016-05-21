package com.cn.fit.ui.patient.myres.mycustomer;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ActivityMyCustomer extends ActivityBasic {

    private ListView list;
    private Button send;
    private EditText edit;
    private List<MessageVo> meList = new ArrayList<MessageVo>();
    private MessageAdapter messageAdapter = new MessageAdapter(this, meList);
    ;
    private TextView title;

    /**
     * 2015-4-9
     *
     * @author kuangtiecheng
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_res_my_customer);
        title = (TextView) findViewById(R.id.middle_tv);
        title.setText("我的客服");
        initWidget();
        list.setAdapter(messageAdapter);
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit.getText().toString();
                String sendContent;
                SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss");
                String time = df.format(new Date()).toString();
                if (content != null
                        && (sendContent = content.trim().replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "")
                        .replaceAll("\f", "")) != "") {
//					if(toggle.isChecked())
//					{
                    meList.add(new MessageVo(MessageVo.MESSAGE_FROM, sendContent, time));
//					}
//					else
//					{
                    meList.add(new MessageVo(MessageVo.MESSAGE_TO, sendContent, time));
//					}

                    messageAdapter.notifyDataSetChanged();
                }
                edit.setText("");
            }
        });
    }

    public void initWidget() {
        list = (ListView) findViewById(R.id.list);
        send = (Button) findViewById(R.id.send);
        edit = (EditText) findViewById(R.id.edit);
    }

}

	

