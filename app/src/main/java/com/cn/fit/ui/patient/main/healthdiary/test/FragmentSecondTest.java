package com.cn.fit.ui.patient.main.healthdiary.test;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.fit.R;
import com.cn.fit.model.healthdiarytest.BeanOption;
import com.cn.fit.model.healthdiarytest.BeanQuestion;
import com.cn.fit.model.healthdiarytest.BeanWenjuan;
import com.cn.fit.util.FButton;
import com.cn.fit.util.progressfragment.ProgressListFragment;
import com.cn.fit.util.radioandcheckbox.widget.RadioButton;
import com.cn.fit.util.superpicker.CustomNumberPicker;
import com.cn.fit.util.superpicker.CustomTimePicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 第二次测评fragment
 *
 * @author kuangtiecheng
 */
public class FragmentSecondTest extends ProgressListFragment {
    private EvaluationAdapter evaluationAdapter;
    public static String PINGGU_TYPE = "pinggu_type";
    private int nowPage = 0;

    public static FragmentSecondTest newInstance() {
        FragmentSecondTest fragment = new FragmentSecondTest();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            nowPage = getArguments().getInt(PINGGU_TYPE);
        }
        setEmptyText(R.string.no_data);
        if (nowPage == 0) {
            if (((ActivityEvaluationSecond) getActivity()).firstIn) {
                obtainData();
                ((ActivityEvaluationSecond) getActivity()).firstIn = false;
            }
        } else {
            if (((ActivityEvaluationSecond) getActivity()).getBeanWenjuan() != null) {
                BeanQuestion bean = ((ActivityEvaluationSecond) getActivity()).getBeanWenjuan().getQuestions().get(nowPage);
                evaluationAdapter = new EvaluationAdapter(bean, getActivity());
            } else {
                BeanQuestion question = new BeanQuestion();
                evaluationAdapter = new EvaluationAdapter(question, getActivity());
            }
            setListAdapter(evaluationAdapter);
            setListShown(true);
        }
    }

    private void obtainData() {
        // Show indeterminate progress
        setListShown(false);
        AscyncGetMyEvaluations mAscyncGetMyEvaluations = new AscyncGetMyEvaluations(
                getActivity()) {

            @Override
            protected void onPostExecute(BeanWenjuan result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (((ActivityEvaluationSecond) getActivity()) != null) {
                    if (result != null) {
                        ((ActivityEvaluationSecond) getActivity())
                                .setBeanWenjuan(result);
                        evaluationAdapter = new EvaluationAdapter(result
                                .getQuestions().get(0), getActivity());
                        setListAdapter(evaluationAdapter);
                    }
                    setListShown(true);
                }
            }

        };
        mAscyncGetMyEvaluations.execute();

    }

    List<BeanOption> select = new ArrayList<BeanOption>();

    /*
     * 二次测评的adapter
     */
    private class EvaluationAdapter extends BaseAdapter {
        private BeanQuestion mBeanQuestion;
        private Context mContext;

        public EvaluationAdapter() {
            super();
            // TODO Auto-generated constructor stub
        }

        public EvaluationAdapter(BeanQuestion mBeanQuestion, Context mContext) {
            this();
            this.mBeanQuestion = mBeanQuestion;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (mBeanQuestion.getChildren() == null && mBeanQuestion.getOptions() == null) {
                return 0;
            }
            if (mBeanQuestion.getHaschild() == 1) {
                return mBeanQuestion.getChildren().size();
            } else {
                return 1;
            }

        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            if (mBeanQuestion.getHaschild() == 1) {
                return mBeanQuestion.getChildren().get(position);
            } else {
                return mBeanQuestion;
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            // if(convertView==null){
            /** 初始化控件 */
            holder = new ViewHolder();
            if (mBeanQuestion.getContext().equals("血压")) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.evaluation2_chooseitemtype1, parent, false);
                holder.tv_name = (FButton) convertView
                        .findViewById(R.id.pinggu_title);
                holder.tv_name.setCornerRadius(30);
                holder.et_2 = (CustomNumberPicker) convertView
                        .findViewById(R.id.pinggu_choose_item1);
                holder.et_3 = (CustomNumberPicker) convertView
                        .findViewById(R.id.pinggu_choose_item2);
                holder.unit_1 = (TextView) convertView
                        .findViewById(R.id.unit_1);
                holder.unit_2 = (TextView) convertView
                        .findViewById(R.id.unit_2);
            } else if (mBeanQuestion.getContext().equals("血糖")) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.evaluation2_chooseitemtype2, parent, false);
                holder.tv_name = (FButton) convertView
                        .findViewById(R.id.pinggu_title);
                holder.tv_name.setCornerRadius(30);
                holder.et_1 = (CustomTimePicker) convertView.findViewById(R.id.et_1);
                holder.et_2 = (CustomNumberPicker) convertView
                        .findViewById(R.id.et_2);
                holder.et_3 = (CustomNumberPicker) convertView
                        .findViewById(R.id.et_3);
                holder.tv_1 = (TextView) convertView.findViewById(R.id.tv_1);
                holder.tv_2 = (TextView) convertView.findViewById(R.id.tv_2);
                holder.tv_3 = (TextView) convertView.findViewById(R.id.tv_3);
                holder.unit_1 = (TextView) convertView
                        .findViewById(R.id.unit_1);
            } else if (mBeanQuestion.getContext().equals("血脂")) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.evaluation2_chooseitemtype1, parent, false);
                holder.tv_name = (FButton) convertView
                        .findViewById(R.id.pinggu_title);
                holder.tv_name.setCornerRadius(30);
                holder.et_2 = (CustomNumberPicker) convertView
                        .findViewById(R.id.pinggu_choose_item1);
                holder.et_3 = (CustomNumberPicker) convertView
                        .findViewById(R.id.pinggu_choose_item2);
                holder.et_3.setVisibility(View.GONE);
                holder.unit_1 = (TextView) convertView
                        .findViewById(R.id.unit_1);
                holder.unit_2 = (TextView) convertView
                        .findViewById(R.id.unit_2);
                holder.unit_1.setVisibility(View.GONE);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.evaluation_chooseitem, parent, false);
                holder.radioButtonList[0] = (RadioButton) convertView
                        .findViewById(R.id.result_a);
                holder.radioButtonList[1] = (RadioButton) convertView
                        .findViewById(R.id.result_b);
                holder.radioButtonList[2] = (RadioButton) convertView
                        .findViewById(R.id.result_c);
                holder.radioButtonList[3] = (RadioButton) convertView
                        .findViewById(R.id.result_d);
                holder.content = (TextView) convertView
                        .findViewById(R.id.content_evaluationItem);
            }

            /** 显示处理 */
            if (mBeanQuestion.getContext().equals("血压")) {
                holder.tv_name.setText(mBeanQuestion.getChildren()
                        .get(position).getContext());
                holder.et_2.setMaxAndMinValue(60, 180);
                holder.et_2.setTag(mBeanQuestion.getChildren().get(position)
                        .getChildren().get(0).getQuestionID());
                String dataEt2 = ((ActivityEvaluationSecond) getActivity()).answerList
                        .get(mBeanQuestion.getChildren().get(position)
                                .getChildren().get(0).getQuestionID()
                                + "");
                if (dataEt2 != null) {
                    holder.et_2.setText(dataEt2);
                }
                String unit = mBeanQuestion.getChildren().get(position)
                        .getChildren().get(0).getUtil();

                holder.et_2.setTips("请选择收缩压（" + unit + "）");
                holder.et_3.setMaxAndMinValue(60, 100);
                holder.et_3.setTips("请选择舒张压（" + unit + "）");
                holder.et_3.setTag(mBeanQuestion.getChildren().get(position)
                        .getChildren().get(1).getQuestionID());
                String dataEt3 = ((ActivityEvaluationSecond) getActivity()).answerList
                        .get(mBeanQuestion.getChildren().get(position)
                                .getChildren().get(1).getQuestionID()
                                + "");
                if (dataEt3 != null) {
                    holder.et_3.setText(dataEt3);
                }

                holder.unit_1.setText(unit);
                holder.unit_2.setText(unit);
                holder.et_2.addTextChangedListener(new CustomTextWacher(
                        holder.et_2));
                holder.et_3.addTextChangedListener(new CustomTextWacher(
                        holder.et_3));
            } else if (mBeanQuestion.getContext().equals("血糖")) {
                holder.tv_name.setText(mBeanQuestion.getChildren()
                        .get(position).getContext());
                List<String> xuetangList = new ArrayList<String>();
                int min = (int) (Float.parseFloat(mBeanQuestion.getChildren()
                        .get(position).getChildren().get(1).getOptions().get(0)
                        .getContext()) * 10);
                int max = (int) (Float.parseFloat(mBeanQuestion.getChildren()
                        .get(position).getChildren().get(1).getOptions().get(1)
                        .getContext()) * 10);

                for (int i = min; i <= max; i++) {
                    xuetangList.add(((float) i) / 10 + "");
                }
                holder.et_2.setList(xuetangList);
                String unit = mBeanQuestion.getChildren().get(position)
                        .getChildren().get(1).getUtil();
                holder.et_2.setTips("请选择血糖值（" + unit + "）");
                holder.et_3.setTips("请选择饥饿程度");

                holder.unit_1.setText(unit);
                List<String> selectorList = new ArrayList<String>();
                select = mBeanQuestion.getChildren().get(position)
                        .getChildren().get(2).getOptions();
                for (BeanOption beanOption : select) {
                    selectorList.add(beanOption.getContext());
                }
                holder.et_3.setList(selectorList);

                holder.et_1.setTag(mBeanQuestion.getChildren().get(position)
                        .getChildren().get(0).getQuestionID());
                holder.et_2.setTag(mBeanQuestion.getChildren().get(position)
                        .getChildren().get(1).getQuestionID());
                holder.et_3.setTag(mBeanQuestion.getChildren().get(position)
                        .getChildren().get(2).getQuestionID());
                holder.et_1.setNow();
                String dataEt1 = ((ActivityEvaluationSecond) getActivity()).answerList
                        .get(mBeanQuestion.getChildren().get(position)
                                .getChildren().get(0).getQuestionID()
                                + "");
                if (dataEt1 != null) {
                    holder.et_1.setText(dataEt1);
                }

                String dataEt2 = ((ActivityEvaluationSecond) getActivity()).answerList
                        .get(mBeanQuestion.getChildren().get(position)
                                .getChildren().get(1).getQuestionID()
                                + "");
                if (dataEt2 != null) {
                    holder.et_2.setText(dataEt2);
                }

                String dataEt3 = ((ActivityEvaluationSecond) getActivity()).answerList
                        .get(mBeanQuestion.getChildren().get(position)
                                .getChildren().get(2).getQuestionID()
                                + "");
                if (dataEt3 != null) {

                    for (BeanOption beanOption : select) {
                        if (beanOption.getValue().equals(dataEt3)) {
                            holder.et_3.setText(beanOption.getContext());
                        }
                    }
                }

                holder.et_1.addTextChangedListener(new CustomTextWacher(
                        holder.et_1));
                holder.et_2.addTextChangedListener(new CustomTextWacher(
                        holder.et_2));
                holder.et_3.addTextChangedListener(new CustomTextWacher(
                        holder.et_3));

            } else if (mBeanQuestion.getContext().equals("血脂")) {
                holder.tv_name.setText(mBeanQuestion.getChildren()
                        .get(position).getContext());
                holder.et_2.setMaxAndMinValue(0, 10);
                holder.et_2.setHint("请选择");
                String unit = mBeanQuestion.getChildren().get(position)
                        .getUtil();
                holder.et_2.setTips("请选择"
                        + mBeanQuestion.getChildren().get(position)
                        .getContext() + "（" + unit + "）");
                holder.unit_2.setText(unit);
                holder.et_2.setTag(mBeanQuestion.getChildren().get(position)
                        .getQuestionID());
                holder.et_2.addTextChangedListener(new CustomTextWacher(
                        holder.et_2));
                String dataEt2 = ((ActivityEvaluationSecond) getActivity()).answerList
                        .get(mBeanQuestion.getChildren().get(position)
                                .getQuestionID()
                                + "");
                if (dataEt2 != null) {
                    holder.et_2.setText(dataEt2);
                }

                List<String> xuezhiList = new ArrayList<String>();
                int min = (int) (Float.parseFloat(mBeanQuestion.getChildren()
                        .get(position).getOptions().get(0).getContext()) * 10);
                int max = (int) (Float.parseFloat(mBeanQuestion.getChildren()
                        .get(position).getOptions().get(1).getContext()) * 10);

                for (int i = min; i <= max; i++) {
                    xuezhiList.add(((float) i) / 10 + "");
                }
                holder.et_2.setList(xuezhiList);

            } else {
                for (int i = 0; i < 4; i++) {
                    if (i < mBeanQuestion.getOptions().size()) {
                        String contextStr = mBeanQuestion.getOptions().get(i)
                                .getContext();
                        holder.radioButtonList[i].setVisibility(View.VISIBLE);
                        holder.radioButtonList[i].setText(contextStr);
                        holder.radioButtonList[i]
                                .setOnCheckedChangeListener(new Choose(
                                        mBeanQuestion.getQuestionID(), i));
                    } else {
                        holder.radioButtonList[i].setVisibility(View.GONE);
                    }
                }
                String choose = ((ActivityEvaluationSecond) getActivity()).answerList
                        .get(mBeanQuestion.getQuestionID() + "");
                if (choose != null) {
                    if (choose.equals("A")) {
                        holder.radioButtonList[0].setChecked(true);
                    } else if (choose.equals("B")) {
                        holder.radioButtonList[1].setChecked(true);
                    } else if (choose.equals("C")) {
                        holder.radioButtonList[2].setChecked(true);
                    } else if (choose.equals("D")) {
                        holder.radioButtonList[3].setChecked(true);
                    }
                }
                holder.content.setText(mBeanQuestion.getContext().replace(
                        "\r\n", ""));
            }

            return convertView;

        }
    }

    private class Choose implements
            android.widget.CompoundButton.OnCheckedChangeListener {
        private long id;
        private int selection;

        public Choose(long id, int selection) {
            this.id = id;
            this.selection = selection;
        }

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            // TODO Auto-generated method stub
            switch (selection) {
                case 0:
                    ((ActivityEvaluationSecond) getActivity()).answerList.put(id
                            + "", "A");
                    break;
                case 1:
                    ((ActivityEvaluationSecond) getActivity()).answerList.put(id
                            + "", "B");
                    break;
                case 2:
                    ((ActivityEvaluationSecond) getActivity()).answerList.put(id
                            + "", "C");
                    break;
                case 3:
                    ((ActivityEvaluationSecond) getActivity()).answerList.put(id
                            + "", "D");
                    break;
                default:
                    break;
            }
        }

    }

    private class ViewHolder {
        FButton tv_name;
        TextView tv_1, tv_2, tv_3;
        TextView unit_1, unit_2;
        CustomNumberPicker et_2, et_3;
        CustomTimePicker et_1;

        TextView content;
        RadioButton[] radioButtonList = new RadioButton[4];
    }

    private class CustomTextWacher implements TextWatcher {

        EditText mEditText;

        public CustomTextWacher(EditText et) {
            mEditText = et;
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub
            if (mEditText == null) {
                return;
            }
            ((ActivityEvaluationSecond) getActivity()).answerList.put(mEditText
                    .getTag().toString(), arg0.toString());
            for (BeanOption mBeanOption : select) {
                if (mBeanOption.getContext().equals(arg0.toString())) {
                    ((ActivityEvaluationSecond) getActivity()).answerList.put(
                            mEditText.getTag().toString(),
                            mBeanOption.getValue());
                }
            }

        }

    }

}
