package com.cn.fit.ui.patient.main.mynurse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.myres.mycustomer.MessageAdapter;
import com.cn.fit.ui.patient.myres.mycustomer.MessageVo;

/**
 * 图文咨询
 *
 * @author kuangtiecheng
 */
public class PicAdvisiry extends ActivityBasic {

    private ListView list;
    private Button send;
    private EditText edit;
    private List<MessageVo> meList = new ArrayList<MessageVo>();
    private MessageAdapter messageAdapter = new MessageAdapter(this, meList);
    ;
    private TextView title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_advisiry);
        title = (TextView) findViewById(R.id.middle_tv);
        title.setText("图文咨询");
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
                        && (sendContent = content.trim().replaceAll("\r", "")
                        .replaceAll("\t", "").replaceAll("\n", "")
                        .replaceAll("\f", "")) != "") {
                    // if(toggle.isChecked())
                    // {
                    meList.add(new MessageVo(MessageVo.MESSAGE_TO, sendContent,
                            time));

                    // }
                    // else
                    // {
                    meList.add(new MessageVo(MessageVo.MESSAGE_FROM,
                            sendContent, time));
                    // }

                    messageAdapter.notifyDataSetChanged();
                }
                edit.setText("");
            }
        });
    }

    public void initWidget() {
        list = (ListView) findViewById(R.id.list_pic);
        send = (Button) findViewById(R.id.send_pic);
        edit = (EditText) findViewById(R.id.edit_pic);
    }

}
