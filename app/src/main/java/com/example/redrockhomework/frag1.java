package com.example.redrockhomework;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class frag1 extends Fragment {
    private View view;
    public String[] name={"歌曲1","歌曲2","歌曲3"};
    public static int[] icons={R.drawable.music0,R.drawable.music1,R.drawable.music2};

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

    class MyBaseAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int i) {
            return name[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View view=View.inflate(frag1.this.getContext(),R.layout.item_layout,null);
            TextView tv_name=view.findViewById(R.id.item_name);
            ImageView iv=view.findViewById(R.id.iv);
            tv_name.setText(name[i]);
            iv.setImageResource(icons[i]);
            return view;
        }


    }
}
