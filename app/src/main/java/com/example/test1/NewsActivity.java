package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] mDataset ={"1","2"};

    //Volley : newRequestQueue == Request 요청이 들어오면 들어노는 순서대로 뿌려주는 역활
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        //activity_news 에 연결

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //하나의 로우 사이즈 맞춤
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //개가 어디 방향인지 LinearLayoutManager 해줌
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        queue = Volley.newRequestQueue(this);
        getNews();//여기까지가 1번 과정
        //1. 화면 로딩 -> 뉴스 정보를 받아온다.

        //2. 정보-> 어댑터 넘겨준다.
        //3. 어댑터 -> 셋팅
    }
    public void getNews(){

        String url ="https://newsapi.org/v2/top-headlines?country=kr&apiKey=3d5dcab149914c7bb8339c92f62ffb5d";

        // Request a string response from the provided URL.
        // 요청을 하면 어떠한 문자열 반응이 온다.
        //
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override//뉴스 API에 있는 정보가 쭉 들어온다.
                    // response 데이터를 가져와서 List<NewsData>배열있는걸 news.add(newsData); 넣어 ->>
                    //그걸 mAdapter = new MyAdapter(news); 어뎁터에 넘기는 작업
                    public void onResponse(String response) {
                        //Log.d(" NEWS", response);

                //public void onResponse(String response) 으로 가져오면 문자열인데 그걸 제이슨 오브젝트 형식으로 바꿔서 가져 오려고 하려고
                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            JSONArray arrayArticles = jsonObj.getJSONArray("articles");

                            //response ->>NewsData Class 분류
                            List<NewsData> news = new ArrayList<>(); //많은 데이터가 담겨 있어서 배열로 사용
                            for (int i = 0 , j = arrayArticles.length(); i < j; i++){
                                JSONObject obj = arrayArticles.getJSONObject(i);

                                Log.d("NEWS", obj.toString());

                                NewsData newsData = new NewsData(); //클래스 선언 쓰겠다고
                                newsData.setTitle(obj.getString("title"));
                                newsData.setUrlToImage(obj.getString("urlToImage"));
                                newsData.setDescription(obj.getString("description"));
                                news.add(newsData);
                            }

                            //여기가 mAdapter, mRecyclerView 뉴스 모든 정보가 여기로 들어온다.
                            //NewsData 클래스에서 NewsData(즉 Mydata)
                            mAdapter = new MyAdapter(news, NewsActivity.this, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //이 포지션을 찾아서 해당 클릭이 뭔지 알 수 있다.
                                    Object obj = v.getTag();
                                    if(obj != null){
                                        int position = (int)obj;

                                        //((MyAdapter)mAdapter) : 클랜스 변환 // 해당 포지션의 대한 뉴스 데이터를 가져온다.
                                        ((MyAdapter)mAdapter).getNews(position);
                                        Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
                                        intent.putExtra("news", ((MyAdapter)mAdapter).getNews(position));
                                        startActivity(intent);
                                    }
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // stringRequest 에있는 정보를 ==>queue 에 담아준다.
        queue.add(stringRequest);
    }

}
