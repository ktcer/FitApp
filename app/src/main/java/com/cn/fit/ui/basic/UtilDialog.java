package com.cn.fit.ui.basic;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.fit.R;
import com.cn.fit.util.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UtilDialog extends Dialog implements DialogInterface.OnClickListener {

    protected ProgressDialog progDialog;

    protected CustomDialog.Builder dialog;

    protected Handler handler = new Handler();

    private Context context;

    private ImageView diaIcon;
    private TextView diaTitle;
    private TextView diaTextView;
    private Button btnDialogClose;
    private Button btnDialogCustom;
    private ListView diaListView;
    private SimpleAdapter adapter;

    private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    private IfaceClick otherBtnClick;

    public UtilDialog(Context cont) {
        super(cont);
        context = cont;
//		progDialog = new ProgressDialog(context);
//		progDialog.setIcon(R.drawable.icon_normal);
//		progDialog.setCancelable(true);

        dialog = new CustomDialog.Builder(context);
        dialog.setPositiveButton("确认", this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_window);

        diaIcon = (ImageView) this.findViewById(R.id.dia_icon);
        diaTitle = (TextView) this.findViewById(R.id.dia_title);

        diaTextView = (TextView) this.findViewById(R.id.diaTextView);
        diaListView = (ListView) this.findViewById(R.id.diaListView);
        btnDialogClose = (Button) this.findViewById(R.id.btnDialogClose);
        btnDialogCustom = (Button) this.findViewById(R.id.btnDialogCustom);

        adapter = new SimpleAdapter(context, list, R.layout.dialog_message,
                new String[]{"name", "message"},
                new int[]{R.id.dia_name, R.id.dia_message});

        btnDialogClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnDialogCustom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != otherBtnClick) {
                    otherBtnClick.onClick();
                }
            }
        });
    }

    public void dismiss() {
        if (null != progDialog && progDialog.isShowing()) {
            progDialog.dismiss();
        }
    }

    private RunDialog runDialog = new RunDialog();

    class RunDialog implements Runnable {
        String dialogTitle;
        String dialogMessage;
        int dialogIcon;

        @Override
        public void run() {
            dialog.setTitle(dialogTitle);
            dialog.setMessage(dialogMessage);
            if (null != progDialog && progDialog.isShowing()) {
                progDialog.dismiss();
            }
            dialog.create().show();
        }
    }

    public void showAlertDialog(String title, String message, int drawable) {
        runDialog.dialogTitle = title;
        runDialog.dialogMessage = message;
        runDialog.dialogIcon = drawable;
        handler.post(runDialog);
    }

    public void showAlertDialog(String title, String message) {
        runDialog.dialogTitle = title;
        runDialog.dialogMessage = message;
        runDialog.dialogIcon = -1;
        handler.post(runDialog);
    }

    private RunProgress runProgress = new RunProgress();

    class RunProgress implements Runnable {
        String progressTitle;
        String progressMessage;

        @Override
        public void run() {
            if (null != progDialog) {
                progDialog.dismiss();
                progDialog = null;
            }
            progDialog = new ProgressDialog(context);
//			progDialog.setIcon(R.drawable.icon);
            progDialog.setCancelable(true);

            progDialog.setTitle(progressTitle);
            progDialog.setMessage(progressMessage);
            progDialog.show();
        }
    }

    public void showProgressDialog(String title, String message) {
        runProgress.progressTitle = title;
        runProgress.progressMessage = message;
        handler.post(runProgress);
    }

    public void showProgressDialog(String message, Runnable run) {
        showProgressDialog("加载中", message, run);
    }

    public void showProgressDialog(String title, String message, Runnable run) {
        runProgress.progressTitle = title;
        runProgress.progressMessage = message;
        handler.post(runProgress);
        new ProgressThread(run).start();
    }

    /**
     * @author kuangtiecheng
     */
    class ProgressThread extends Thread {
        private Runnable runThread;

        ProgressThread(Runnable thread) {
            runThread = thread;
        }

        public void run() {
            try {
                sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runThread.run();
        }
    }

    public void showProgressDialog(String title, String message, Runnable run, long delayedTime) {
        runProgress.progressTitle = title;
        runProgress.progressMessage = message;
        handler.post(runProgress);
        new Handler().postDelayed(run, delayedTime);
    }

    private RunToast runShortToast = new RunToast(true);
    private RunToast runLongToast = new RunToast(false);

    class RunToast implements Runnable {
        boolean isLongTime = false;
        String message;

        RunToast(boolean isLong) {
            isLongTime = isLong;
        }

        @Override
        public void run() {
            if (isLongTime) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showToastDialog(String message) {
        runShortToast.message = message;
        handler.post(runShortToast);
    }

    public void showToastDialogLongTime(String message) {
        runLongToast.message = message;
        handler.post(runLongToast);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }


    /**
     * @param d Drawable
     */
    public void setCustomIcon(Drawable d) {
        diaIcon.setBackgroundDrawable(d);
    }

    /**
     * @param d int
     */
    public void setCustomIcon(int d) {
        diaIcon.setBackgroundResource(d);
    }

    /**
     * @param s
     */
    public void setCustomTitle(String s) {
        diaTitle.setText(s);
    }

    /**
     * @param content String
     */
    public void setContent(String content, IfaceClick click, String btnName) {
        diaListView.setVisibility(View.GONE);
        btnDialogCustom.setVisibility(View.VISIBLE);
        diaTextView.setText(content);
        btnDialogCustom.setText(btnName);
        otherBtnClick = click;
    }

    /**
     * @param content DialogContent[]
     */
    public void setContent(DialogContent[] content) {
        diaTextView.setVisibility(View.GONE);
        btnDialogCustom.setVisibility(View.GONE);
        diaListView.setVisibility(View.VISIBLE);
        list.clear();
        for (DialogContent dc : content) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", dc.name);
            map.put("message", dc.value);
            list.add(map);
        }
        diaListView.setAdapter(adapter);
    }

    /**
     * @param content List<DialogContent>
     */
    public void setContent(List<DialogContent> content) {
        list.clear();
        for (DialogContent dc : content) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", dc.name);
            map.put("message", dc.value);
            list.add(map);
        }
        diaListView.setAdapter(adapter);
        btnDialogCustom.setVisibility(View.GONE);
    }

    /**
     * @param content DialogContent[]
     * @param click   IfaceClick
     * @param btnName String
     */
    public void setContent(DialogContent[] content, IfaceClick click, String btnName) {
        list.clear();
        for (DialogContent dc : content) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", dc.name);
            map.put("message", dc.value);
            list.add(map);
        }
        diaListView.setAdapter(adapter);
        btnDialogCustom.setVisibility(View.VISIBLE);
        btnDialogCustom.setText(btnName);
        otherBtnClick = click;
    }

    /**
     * @param content List<DialogContent>
     * @param click   IfaceClick
     * @param btnName String
     */
    public void setContent(List<DialogContent> content, IfaceClick click, String btnName) {
        list.clear();
        for (DialogContent dc : content) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", dc.name);
            map.put("message", dc.value);
            list.add(map);
        }
        diaListView.setAdapter(adapter);
        btnDialogCustom.setVisibility(View.VISIBLE);
        btnDialogCustom.setText(btnName);
        otherBtnClick = click;
    }

    /**
     * @author kuangtiecheng
     */
    public static class DialogContent {
        String name;
        String value;
    }

    /**
     * @author kuangtiecheng
     */
    public static interface IfaceClick {

        /**
         *
         */
        public void onClick();
    }
}