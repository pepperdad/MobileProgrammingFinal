package com.example.mpfinal.ui.collection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mpfinal.Display_Fish;
import com.example.mpfinal.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CollectionRecyclerViewAdapter extends RecyclerView.Adapter<CollectionRecyclerViewAdapter.ViewHolder> {

    private FirebaseAuth mFirebaseAuth; // firebase auth
    private DatabaseReference mDatabaseRef; //realtime db
    private FirebaseUser user;

    private LayoutInflater layoutInflater;
    private ArrayList<Boolean> fish, display_fish;


    CollectionRecyclerViewAdapter(Context context, ArrayList<Boolean> fish, ArrayList<Boolean> display_fish){
        this.layoutInflater = LayoutInflater.from(context);
        this.fish = fish;
        this.display_fish = display_fish;

        user= FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("MadCampWeek4");

    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.collection_cardview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //display_fish.get(position)
        holder.tv_groupInfo.setText("버튼을 누르면 트리에 나와요!");
        String[] names=Display_Fish.getNames();

        switch (position){
            case 1:
                holder.img_fish.setAnimation(R.raw.i_socks);
                break;
            case 2:
                holder.img_fish.setAnimation(R.raw.i_googlecard);
                break;
            case 3:
                holder.img_fish.setAnimation(R.raw.i_gift);
                break;
            case 4:
                holder.img_fish.setAnimation(R.raw.i_giftcard);
                break;
            case 5:
                holder.img_fish.setAnimation(R.raw.i_bugger);
                break;
            case 6:
                holder.img_fish.setAnimation(R.raw.i_cosmetic);
                break;
            case 0:
                holder.img_fish.setAnimation(R.raw.i_vegetable);
                break;
        }

        if (fish.get(position)){
            holder.tv_groupName.setText(names[position]);
        }else{
            holder.img_fish.setImageResource(R.drawable.question);
            holder.tv_groupName.setText("?");
            holder.fb_addfish.setVisibility(View.GONE);
            holder.tv_groupInfo.setText("Don't have it yet");
        }


        if (display_fish.get(position)){
            holder.fb_addfish.setText("제거하기");
            holder.fb_addfish.setIconResource(R.drawable.outline_remove_black_24dp);
        }else{
            holder.fb_addfish.setText("추가하기");
            holder.fb_addfish.setIconResource(R.drawable.outline_add_black_24dp);
        }

        holder.fb_addfish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.fb_addfish.getText().equals("추가하기")){
                    //Log.d("리사이클러", "추가"+position);
                    holder.fb_addfish.setText("제거하기");
                    holder.fb_addfish.setIconResource(R.drawable.outline_remove_black_24dp);
                    // position이랑 display_fish 순번, 위치 같음
                    ArrayList<Boolean> displayFish=Display_Fish.getOwn();
                    displayFish.set(position, true);
                    Display_Fish.setOwn(displayFish);
                    //Log.d("리사이클러a",Display_Fish.getOwn().toString());
                    mDatabaseRef.child("UserAccount").child(user.getUid()).child("display_fish").setValue(displayFish);

                } else{
                    //Log.d("리사이클러", "삭제"+position);
                    holder.fb_addfish.setText("추가하기");
                    holder.fb_addfish.setIconResource(R.drawable.outline_add_black_24dp);
                    ArrayList<Boolean> displayFish=Display_Fish.getOwn();
                    displayFish.set(position, false);
                    Display_Fish.setOwn(displayFish);
                    //Log.d("리사이클러r",Display_Fish.getOwn().toString());
                    mDatabaseRef.child("UserAccount").child(user.getUid()).child("display_fish").setValue(displayFish);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return (fish != null ? fish.size() : 0);
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_groupName, tv_groupInfo;
        ExtendedFloatingActionButton fb_addfish;
        LottieAnimationView img_fish;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            int pos = getAdapterPosition();
            tv_groupName = (TextView)itemView.findViewById(R.id.tv_groupSearchName);
            tv_groupInfo = (TextView)itemView.findViewById(R.id.tv_groupSearchInfo);
            fb_addfish = (ExtendedFloatingActionButton)itemView.findViewById(R.id.fb_addfish);
            img_fish = itemView.findViewById(R.id.img_fish);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("리사이클러", "아이템 누름");
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

        }
    }
}
