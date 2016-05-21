package com.cn.fit.ui.patient.main.mynurse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.ui.basic.ActivityBasic;
import com.cn.fit.ui.patient.myres.mycustomer.MessageAdapter;
import com.cn.fit.ui.patient.myres.mycustomer.MessageVo;

/**
 * 咨询列表点进去的历史咨询详情
 *
 * @author kuangtiecheng
 */
public class AdvisiryHistory extends ActivityBasic {

    private ListView list;
    private Button send;
    private List<MessageVo> meList = new ArrayList<MessageVo>();
    private MessageAdapter messageAdapter = new MessageAdapter(this, meList);
    ;
    private TextView title;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlyalistview);
        title = (TextView) findViewById(R.id.middle_tv);
        title.setText("对话详情");
        initWidget();
        advisoryHistory();
        myAdvisory();
        send.setVisibility(View.VISIBLE);
        send.setRight(50);
        send.setText("我要咨询");
        list.setAdapter(messageAdapter);

    }

    /**
     * 我要咨询
     */

    private void myAdvisory() {
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AdvisiryHistory.this, PicAdvisiry.class);
                startActivity(intent);

            }
        });

    }

    /**
     * 咨询历史
     */
    private void advisoryHistory() {
        String contentAnswer[] = {"回复:如果是甲状腺肿大，你现在服用的优甲乐就有一定缩小甲状腺的作用，但剂量可以增加一点。", "回复：很多人锁骨都有凹陷啊。做这个对慢甲炎影响不大，但前提是甲功正常。请放心诊疗。"};
        String contentQuestion[] = {"问： 慢甲炎10多年了，注射过地塞米松，现脖子两侧肌肉肿大突出了，跟注射有关吗？怎么治疗？可以消吗,谢谢", "问：再请问脖子下面锁骨有凹陷，可以在美容科做锁骨凹陷填充吗，对慢甲炎有影响吗，谢谢 "};
        String sendContent;
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss");
        String time = df.format(new Date()).toString();
        for (int i = 0; i < contentQuestion.length; i++) {
            if (contentQuestion[i] != null
                    && (sendContent = contentQuestion[i].trim().replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "")
                    .replaceAll("\f", "")) != "") {
//				if(toggle.isChecked())
//				{
                meList.add(new MessageVo(MessageVo.MESSAGE_TO, contentQuestion[i], time));
                meList.add(new MessageVo(MessageVo.MESSAGE_FROM, contentAnswer[i], time));
//				}
//				else
//				{

//				}

                messageAdapter.notifyDataSetChanged();
            }
        }

    }

    public void initWidget() {
        list = (ListView) findViewById(R.id.list_pic);
        send = (Button) findViewById(R.id.send_pic);
    }

}

	

