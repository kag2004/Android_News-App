package com.example.test1;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<NewsData> mDataset;
    private static View.OnClickListener onClickListener;
    //RecyclerView.ViewHolder 이걸 사용 하는 이유는 메모르 관리 효율적으로 관리 해준다는 장점 때문에
    //xml파일이 하나로 계속 View를 만들필요 없이 여기에서 자동적으로 하나하나 필요 할 때마다 써줄 수 있다
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //onCreateViewHolder 한줄에 들어가는 요소를 선택하는 거
        //전체 받아온 것을 하나 하나 풀어 주는 쪽 이라 생각하면 됨
        // each data item is just a string in this case
        public TextView TextView_title;
        public TextView TextView_description;
        public SimpleDraweeView ImageView_title;
        public View rootView;
//        public SimpleDraweeView ImageView_title;
        //4.여기서 자식들 뷰들이 찾아서 쓴다.
        public MyViewHolder(View v) {
            super(v);
            TextView_title = v.findViewById(R.id.TextView_title);
            TextView_description = v.findViewById(R.id.TextView_description);
            ImageView_title = (SimpleDraweeView) v.findViewById(R.id.ImageView_title);
            rootView = v;

            //setClickable : 클릭을 할 수 있다 없다 / setEnabled :활성화 상태이다,아니다 의미
            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    //어뎀터 최초 셋팅 해준다 초기 데이터 들이 여기로 들어와 셋팅~~>>
    //어뎁터 받아온 받아온걸 위에 자식에게 뿌려준다.
    //+ 액티비티가 아닌 곳에서 Context를 쓰기 위해 액티비티에서 Context값을 넘겨 가져 온다.(해보면서 이해 해야할듯)
    //하지만 액티비티 Context를 하면 누수가 생겨서 좋은 점은 아님
    public MyAdapter(List<NewsData> myDataset, Context context, View.OnClickListener onClick) {
        mDataset = myDataset;
        onClickListener = onClick;
        Fresco.initialize(context);

    }
    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        //하나의 로우를 이미지 파일을 연결 하는 곳  onCreateViewHolder 이부분 영역
        //1.애는 뷰 홀더 RecyclerView의 항목 화면 연결은 onCreateViewHolder 함수
        //2.즉 전체 를 받아오는 부분 이고
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_news, parent, false);
        //3.row_news 가 부모 이다.
        //3.이걸 MyViewHolder 이 녀석을 이용해 v를 public MyViewHolder(View v) 에서 하나하나 찾아 가서
        //
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    //5.반복 될때 마다 데이터를 셋팅 하는 곳 onBindViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    //해당 뉴스 정보를 가져 오게 포지션 즉 리사이클러뷰 [] 배열의 00번째 - 데이터 00번째 가져와라 이런 느낌
        //List에 순서대로 add하여 넣었으니 get으로 해당 순버 뉴스 데이터를 꺼내 온다. (저런 느낌)
//        holder.TextView_title.setText(mDataset.get(position).getTitle());
        NewsData news = mDataset.get(position);

        holder.TextView_title.setText(news.getTitle());
        holder.TextView_description.setText(news.getDescription());
        //이미지를 가져 오는 방법은 약간 다름
        //1. fresco를 사용 Fresco는 이미지를 표시하기 위한 레아이웃 SimpleDraweeView를 사용 한다.
        //2. 이런 방식은 해당 Uri 주소에 있는 이미지를 가져 오는 방식이다.
        Uri uri = Uri.parse(news.getUrlToImage());//데이터에 들어있는 이미지 주소 바꿉
        holder.ImageView_title.setImageURI(uri);  //해당 Uri 주소 이미지 가져 오기 위한

        //여긴 태그만 달아주면 된다. 내가 누구를 눌렀고 누구인지 하기 위해서
        holder.rootView.setTag(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //삼항 연산 mDataset == null조건이 트루면 0 / 거짓 mDataset.size 조건식
        return mDataset == null ? 0 : mDataset.size();
    }

    public NewsData getNews(int position){
        return mDataset != null ? mDataset.get(position) : null;
    }
}
