package com.mirea.kt.ribo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText user_login;
    private EditText user_password;
    private Button main_btn,hidenFastStart;
    private TextView result_info;
    public String value,valueDescription;

    private String g = "RIBO-04-22";

    private Button buttonStart;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
// Устанавливаем ссылки на объекты из дизайна
        user_login = findViewById(R.id.user_login);
        user_password = findViewById(R.id.user_password);
        main_btn = findViewById(R.id.main_btn);
        result_info = findViewById(R.id.result_info);
        buttonStart = findViewById(R.id.buttonStart);
        hidenFastStart=findViewById(R.id.HidenFastStart);
        // Обработчик нажатия на кнопку
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                result_info.setText("msgClick");
                if (user_login.getText().toString().trim().equals("") || user_password.getText().toString().trim().isEmpty())
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
                else {
                    String login = user_login.getText().toString();
                    String password = user_password.getText().toString();
                    String url = "https://android-for-students.ru/coursework/login.php";
                    HashMap<String, String> map = new HashMap<>();
                    map.put("lgn", login);
                    map.put("pwd", password);
                    map.put("g", g);
                    HTTPRunnable httpRunnable = new HTTPRunnable(url, map);
                    Thread th = new Thread(httpRunnable);
                    th.start();
                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        try {
                            JSONObject jsonObject = new JSONObject(httpRunnable.getResponseBody());
                            int result = jsonObject.getInt("result_code");
                            if (result == 1) {
                                TextView textView = findViewById(R.id.result_info);

                                String message = String.format("%s\n%s\n",
                                        jsonObject.getString("title"),
                                        jsonObject.getString("task"),
                                        jsonObject.getString("variant"));

//                                valueDescription = jsonObject.getString("description");
                                textView.setText(message);
                                k++;
                                Log.e("myTask",jsonObject.toString());


                            } else if (result == -1) {
                                Toast.makeText(getApplicationContext(), "Сервер не отвечает", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException | NullPointerException exception) {
                            exception.printStackTrace();
                        }
                    }

//                    new HttpPostRequest().execute(url, login, password, g);
//                    handleHttpResponse(response);
//                    onPostExecute()
//                    result_info.setText(response);
                }

            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (k != 0) {

                    Toast.makeText(getApplicationContext(), "Вы успешно вошли!", Toast.LENGTH_LONG).show();
                    Intent switcher = new Intent(MainActivity.this, MainActivity2.class);
//                    switcher.putExtra("description",valueDescription);
                    startActivity(switcher);
                } else {
                    Toast.makeText(MainActivity.this, R.string.user_not_check_quest, Toast.LENGTH_LONG).show();
                }
            }
        });
        hidenFastStart.setOnClickListener(v -> {
            user_login.setText("Student79796");
            user_password.setText("Vs1ZYAW");
        });

    }
}
