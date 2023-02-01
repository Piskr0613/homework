package com.example.redrockhomework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class frag1 extends Fragment implements View.OnClickListener {
    private EditText editText;
    private TextView responseText;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.frag1_layout,null);
        editText= view.findViewById(R.id.search_et);
        Button button=view.findViewById(R.id.search_btn);
        button.setOnClickListener(this);
        responseText=view.findViewById(R.id.response_text);
    @Override
    public View onCreateView(final LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.music_list,null);//绑定布局
        ListView listView=view.findViewById(R.id.lv);//创建ListView列表并绑定控件
        MyBaseAdapter adapter=new MyBaseAdapter();//实例化一个适配器
        listView.setAdapter(adapter);//列表设置适配器
        //列表元素的点击监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(frag1.this.getContext(),MusicActivity.class);
                intent.putExtra("name",name[position]);
                intent.putExtra("position",String.valueOf(position));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.search_btn){
            String inputText=editText.getText().toString();
            sendRequestWithHttpURLConnection(inputText);
        }
    }

    private void sendRequestWithHttpURLConnection(String inputText){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try{
                    URL url=new URL("http://localhost:3000/search?keyword="+inputText);
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream=connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    shoeResponse(response.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader!=null){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void shoeResponse(final String response){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }

}
