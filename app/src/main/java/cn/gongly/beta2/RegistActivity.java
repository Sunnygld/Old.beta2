package cn.gongly.beta2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistActivity extends AppCompatActivity {

    String username,password;
    EditText number;
    EditText psw;
    TextView regist;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_main);

        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        number = (EditText)findViewById(R.id.Regist_username);
        psw = (EditText)findViewById(R.id.Regist_password);
        regist = (TextView)findViewById(R.id.Regist_regist);

        RadioGroup radgroup = (RadioGroup) findViewById(R.id.radioGroup);
        //第一种获得单选按钮值的方法
        //为radioGroup设置一个监听器:setOnCheckedChanged()
        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                if (radbtn.getText().toString().equals("老人")) {
                    type = "o";
                } else {
                    type = "y";
                }
             //   Toast.makeText(getApplicationContext(), "按钮组值发生改变,你选了" + radbtn.getText(), Toast.LENGTH_LONG).show();
            }
        });

        regist.setOnClickListener(
                new TextView.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        try {

                            username = number.getText().toString();
                            password = psw.getText().toString();
                            //判断输入是否为空
                            if(username.isEmpty()||password.isEmpty()||username.contains("#")){
                                Toast.makeText(RegistActivity.this, "用户名密码不能为空", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //若不为空，保存信息，传递信息，跳转页面

                                sendRequestWithHttpURLConnection(username,password,type);
                                Intent intent = new Intent();
                                intent.setClass(RegistActivity.this,LoginActivity.class);
                                //将输入传递给主界面
                                intent.putExtra("username",username);
                                intent.putExtra("password",password);
                                RegistActivity.this.setResult(1,intent);
                                //传递完毕
                                RegistActivity.this.finish();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }


    private void sendRequestWithHttpURLConnection(final String user,final String psw,final String type){

        new Thread(new Runnable(){

            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL("http://api.lincz.cn/AGXA/Login/register.php");//register//login

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes("name="+user+"&pwd="+psw+"&type="+type);

                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    //showResponse(response.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }
        ).start();
    }
}
