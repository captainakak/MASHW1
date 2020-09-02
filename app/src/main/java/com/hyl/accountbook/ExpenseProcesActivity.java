package com.hyl.accountbook;

import com.hyl.dao.DBOpenHelper;
import com.hyl.util.pubFun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @programName: ExpenseProcesActivity.java
 * @programFunction: Add an income and expense record
 * @createDate: 2018/09/19
 * @author: AnneHan
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/19   1.00   AnneHan   New Create
 */
public class ExpenseProcesActivity extends AppCompatActivity {

    private int type = 0;//0:income   1:payout
    final static int EDIT_MODE = 2;

    private String[] str = null;
    private String[] accountId = null;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePicker = null;
    private AlertDialog dialog = null;
    private ArrayAdapter<String> adapter;
    private List<String> list = null;


    private TextView title_tv = null;
    private RadioGroup trans_type_tab_rg = null;
    private RadioButton rb1=null;
    private RadioButton rb2=null;

    private FrameLayout corporation_fl = null;
    private FrameLayout empty_fl = null;
    private Button cost_btn = null;
    private String  value="0";
    private Spinner first_level_category_spn = null;
    private Spinner sub_category_spn = null;
    private int type_sub_id = 0;
    private Spinner account_spn = null;
    private Spinner corporation_spn = null;
    private Button trade_time_btn = null;
    private Spinner project_spn = null;
    private Button memo_btn = null;
    private Button save_btn = null;
    private Button cancel_btn = null;

    private EditText edit = null;
    private int isInitOnly = 0;

    private Context context;

    //类别
    private static String[] bigCategoryList = { "" };
    private static String[] defaultSubCategory_info = { "" };
    //子类别
    private static String[][] subCategory_info = new String[][] {{ "" }, { "" }};
    //账户
    private static String[] accountList = { "" };
    //商家
    private static String[] shopList = { "" };
    //备注
    private static String[] noteList = { "" };

    private TextView txtBigCategory_view;
    private Spinner BigCategory_spinner;
    private ArrayAdapter<String> BigCategory_adapter;

    private TextView txtSubCategory_view;
    private Spinner subCategory_spinner;
    private ArrayAdapter<String> subCategory_adapter;

    private TextView txtAccount_view;
    private Spinner account_spinner;
    private ArrayAdapter<String> account_adapter;

    private TextView txtShop_view;
    private Spinner shop_spinner;
    private ArrayAdapter<String> shop_adapter;

    private TextView txtNote_view;
    private Spinner note_spinner;
    private ArrayAdapter<String> note_adapter;

    private String txtBigCategory = "";
    private String txtSubCategory = "";
    private String txtAccount = "";
    private String txtShop = "";
    private String txtNote = "";

    private TextView txtDate;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_proces);

        //接收传递过来的参数
        final Intent intent = getIntent();
        type = intent.getIntExtra("strType", 0);

        context = this;

        initSpinner();

        loadingFormation();

        trade_time_btn.setText(pubFun.format(calendar.getTime()));

        cost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(ExpenseProcesActivity.this, KeyPad.class);
                i.putExtra("value", value);
                startActivityForResult(i, 0);
            }
        });
        trade_time_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                openDate();
            }
        });
    }

    private void loadingFormation(){
        cost_btn=(Button)findViewById(R.id.cost_btn);
        trade_time_btn=(Button)findViewById(R.id.trade_time_btn);
    }

    private void openDate() {
        datePicker = new DatePickerDialog(this, mDateSetListenerSatrt,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    @Override
    /**
     * return money
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            Bundle extras = data.getExtras();
            value = extras.getString("value");
            cost_btn.setText(DecimalFormat.getCurrencyInstance().format(Double.parseDouble(value)));
        }
    }

    /**
     * return date
     */
    private DatePickerDialog.OnDateSetListener mDateSetListenerSatrt = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.YEAR, year);
            trade_time_btn.setText(pubFun.format(calendar.getTime()));
        }
    };

    /**
     * 初始化spinner
     */
    private void initSpinner(){
        if(type == 0){
            //类别
            bigCategoryList = new String[] { "Occupational income", "Other income" };
            defaultSubCategory_info = new String[] { "Part-time income", "Investment income" ,"Bonus income", "Overtime income", "Interest income", "Wage income" };
            //子类别
            subCategory_info = new String[][] {
                    {"Part-time income", "Investment income", "Bonus income", "Overtime income", "Interest income", "Wage income"},
                    {"Operating income", "Unexpected money" ,"Winning income","Gift income" }};
            //账户
            accountList = new String[] {"Cash Account", "Financial Account", "Virtual Account", "Liability Account", "Debt Account" };
            //商家
            shopList = new String[] {"Bank", "Bus", "Canteen", "Mall", "Supermarket", "Other" };
            //备注
            noteList = new String[] {"Corruption", "Travel", "Decoration", "Company Reimbursement", "Business Trip", "Other" };
        }else{
            bigCategoryList = new String[] {"Clothes & Accessories", "Food and Drink", "Home Property", "Traffic", "Communications", "Leisure and Entertainment", "Learning and Training"
                    ,"Relationships","Medical Care","Financial Insurance","Other Miscellaneous" };
            defaultSubCategory_info = new String[] {"Clothes and pants", "Shoes, hats and bags" ,"Makeup accessories" };
            //子类别
            subCategory_info = new String[][] {
                    {"Clothes and pants", "Shoes, hats and bags" ,"Makeup accessories" },
                    {"Breakfast, lunch and dinner", "Fruit Snacks", "Tobacco, Wine and Tea" },
                    {"Rent", "Property Management" ,"Maintenance", "Water, Electricity and Gas" ,"Daily Necessities" },
                    {"Public Transportation", "Taxi and Rent a Car" ,"Private Car Fee" },
                    {"Mobile phone fee", "Internet fee" ,"landline fee" ,"posting fee" },
                    {"Leisure and Fun", "Travel and Vacation", "Pet Babies", "Corrupt Party", "Sports and Fitness" },
                    {"Books, Newspapers and Magazines", "Digital Equipment", "Training and Training" },
                    {"Gifts and treats", "Charitable donations", "Return money and goods", "Respect parents" },
                    {"Treatment fee", "Beauty fee" ,"Health care fee" ,"Drug fee" },
                    {"Compensation Penalty", "Interest Expenditure", "Consumption Tax", "Mortgage Repayment", "Investment Loss", "Bank Procedure" },
                    {"Loss from bad debts", "Unexpected losses" ,"Other expenses" }};
            //账户
            accountList = new String[] {"Bank Card", "Bus Card", "Meal Card", "Alipay", "TenPay", "Cash", "Other" };
            //商家
            shopList = new String[] {"Bank", "Bus", "Canteen", "Mall", "Supermarket", "Other" };
            //备注
            noteList = new String[] {"Corrupt", "tourism", "decoration", "company reimbursement", "business trip", "other" };
        }

        /**
         * 1、定义类别下拉菜单
         */
        txtBigCategory_view = (TextView) findViewById(R.id.txtBigCategory);
        BigCategory_spinner = (Spinner) findViewById(R.id.BigCategory_spinner);
        //将可选内容与ArrayAdapter连接起来
        BigCategory_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, bigCategoryList);
        //设置下拉列表的风格
        BigCategory_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter添加到spinner中
        BigCategory_spinner.setAdapter(BigCategory_adapter);
        // 添加事件Spinner事件监听
        BigCategory_spinner
                .setOnItemSelectedListener(new BigCategory_spinnerSelectedListener());
        // 设置默认值
        BigCategory_spinner.setVisibility(View.VISIBLE);

        /**
         * 2、定义子类别下拉菜单
         */
        txtSubCategory_view = (TextView) findViewById(R.id.txtSubCategory);
        subCategory_spinner = (Spinner) findViewById(R.id.subCategory_spinner);
        subCategory_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, defaultSubCategory_info);
        subCategory_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategory_spinner.setAdapter(subCategory_adapter);
        subCategory_spinner
                .setOnItemSelectedListener(new subCategory_spinnerSelectedListener());
        subCategory_spinner.setVisibility(View.VISIBLE);

        /**
         * 3、定义账户下拉菜单
         */
        txtAccount_view = (TextView)findViewById(R.id.txtAccount);
        account_spinner = (Spinner) findViewById(R.id.account_spinner);
        account_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, accountList);
        account_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        account_spinner.setAdapter(account_adapter);
        account_spinner
                .setOnItemSelectedListener(new account_spinnerSelectedListener());
        account_spinner.setVisibility(View.VISIBLE);

        /**
         * 4、定义商家下拉菜单
         */
        txtShop_view = (TextView)findViewById(R.id.txtShop);
        shop_spinner = (Spinner) findViewById(R.id.shop_spinner);
        shop_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, shopList);
        shop_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shop_spinner.setAdapter(shop_adapter);
        shop_spinner
                .setOnItemSelectedListener(new shop_spinnerSelectedListener());
        shop_spinner.setVisibility(View.VISIBLE);

        /**
         * 5、定义备注下拉菜单
         */
        txtNote_view = (TextView)findViewById(R.id.txtNote);
        note_spinner = (Spinner) findViewById(R.id.note_spinner);
        note_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, noteList);
        note_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        note_spinner.setAdapter(note_adapter);
        note_spinner
                .setOnItemSelectedListener(new note_spinnerSelectedListener());
        note_spinner.setVisibility(View.VISIBLE);
    }

    /**
     * 选择 类别 事件 监听器
     */
    class BigCategory_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtBigCategory = bigCategoryList[arg2];
            int pos = BigCategory_spinner.getSelectedItemPosition();
            subCategory_adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, subCategory_info[pos]);
            subCategory_spinner.setAdapter(subCategory_adapter);
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 选择 子类别 事件 监听器
     */
    class subCategory_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtSubCategory = (String) subCategory_spinner
                    .getItemAtPosition(arg2);
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 选择 账户事件 监听器
     */
    class account_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtAccount = accountList[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }

    }

    /**
     * 选择 商家事件 监听器
     */
    class shop_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtShop = shopList[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 选择 商家事件 监听器
     */
    class note_spinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            txtNote = noteList[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    public void OnMySaveClick(View v) {
        saveInfo();
    }
    public void OnMyCancelClick(View v) {
        exit();
    }

    /**
     * cancel event
     */
    private void exit() {
        if(type != EDIT_MODE){
            Intent intent = new Intent(this,SpendingActivity.class);
            startActivity(intent);
            finish();
        }else{
            this.setResult(RESULT_OK, getIntent());
            this.finish();
        }
    }

    /**
     * save event
     */
    private void saveInfo() {
        //Save之前先判断用户是否登录
        SharedPreferences sharedPreferences= getSharedPreferences("setting",Activity.MODE_PRIVATE);
        String userID =sharedPreferences.getString("userID", "");

        Log.i("info", "此次登录的用户是" + userID);

        if(userID.isEmpty()){
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("您还未登录，请点击确定按钮进行登录！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            setResult(RESULT_OK);
                            Intent intent=new Intent(ExpenseProcesActivity.this,LoginActivity.class);
                            ExpenseProcesActivity.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    })
                    .show();
        }else{
            if(value.equals("") || value == null || Double.parseDouble(value) <= 0){
                Toast.makeText(getApplicationContext(), getString(R.string.input_message),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            //调用DBOpenHelper
            DBOpenHelper helper = new DBOpenHelper(this,"qianbao.db",null,1);
            SQLiteDatabase db = helper.getWritableDatabase();
            //插入数据
            ContentValues values= new ContentValues();
            values.put("userID",userID);
            values.put("Type",type);
            values.put("incomeWay",txtAccount);
            values.put("incomeBy",txtShop);
            values.put("category",txtBigCategory);
            values.put("item",txtSubCategory);
            values.put("cost", value);
            values.put("note", txtNote);
            values.put("makeDate",pubFun.format(calendar.getTime()));
            long rowid = db.insert("basicCode_tb",null,values);

            //Test
            Cursor c = db.query("basicCode_tb",null,"userID=?",new String[]{userID},null,null,null);
            if(c!=null && c.getCount() >= 1){
                String[] cols = c.getColumnNames();
                while(c.moveToNext()){
                    for(String ColumnName:cols){
                        Log.i("info",ColumnName+":"+c.getString(c.getColumnIndex(ColumnName)));
                    }
                }
                c.close();
            }
            db.close();

            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        }
    }
}
