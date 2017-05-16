package cn.gongly.beta2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText Euesrname;
    EditText Epassword;
    TextView forget;
    TextView login;
    TextView regist;

    String result ;
    String account ;
    String password ;
    String type ;
    String imuser;
    String imensure;
    String another ;
    String anotherim ;
    String anotherhave ;
    String anotheronline ;
    String anotherbinding ;

    String a;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        login = (TextView)findViewById(R.id.M_login);
        regist = (TextView)findViewById(R.id.M_regist);
        Euesrname = (EditText)findViewById(R.id.M_username);
        Epassword = (EditText)findViewById(R.id.M_password);

        Log.d("123","start");

        login.setOnClickListener(
                new TextView.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        try {
                            String username1 = Euesrname.getText().toString();
                            String password1 = Epassword.getText().toString();

                            Log.d("123", username1);
                            sendRequestWithHttpURLConnection(LoginActivity.this, username1, password1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        );

        regist.setOnClickListener(
                new TextView.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
                            LoginActivity.this.startActivityForResult(intent,1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void sendRequestWithHttpURLConnection(final LoginActivity mainActivity, final String user,final String psw){

        new Thread(new Runnable(){

            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL("http://api.lincz.cn/AGXA/Login/login.php");//register//login

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes("name="+user+"&pwd="+psw+"&type=y");

                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    mainActivity.parseJSONWithJSONObject(response.toString());
                    mainActivity.a=response.toString();
                    mainActivity.showResponse();
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

    private void showResponse(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                //a=response;
        // responseText.setText(a);
        //  String b = a;
        Log.d("123","aaa");
        Log.d("123",result);

        System.out.print(result);

        if (result.equals("success")) {
            Log.d("123", "sss");
            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        } else {
            Toast.makeText(LoginActivity.this,"帐号或密码错误",Toast.LENGTH_SHORT).show();
        }

//            }
//        });
    }

    private void parseJSONWithJSONObject(String jsonData)  {
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            result = jsonObject.getString("result");
            account = jsonObject.getString("account");
            password = jsonObject.getString("password");
            type = jsonObject.getString("type");
            imuser = jsonObject.getString("imuser");
            imensure = jsonObject.getString("imensure");
            another = jsonObject.getString("another");
            anotherim = jsonObject.getString("anotherim");
            anotherhave = jsonObject.getString("anotherhave");
            anotheronline = jsonObject.getString("anotheronline");
            anotherbinding = jsonObject.getString("anotherbinding");

            Log.d("MainActivity","123");
            Log.d("MainActivity",result);
            Log.d("MainActivity",account);
            Log.d("MainActivity",password);
            Log.d("MainActivity",type);
            Log.d("MainActivity",imuser);
            Log.d("MainActivity",imensure);
            Log.d("MainActivity",another);
            Log.d("MainActivity",anotherim);
            Log.d("MainActivity",anotherhave);
            Log.d("MainActivity",anotheronline);
            Log.d("MainActivity",anotherbinding);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
