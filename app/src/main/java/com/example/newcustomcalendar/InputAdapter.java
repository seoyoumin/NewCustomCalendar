package com.example.newcustomcalendar;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class InputAdapter extends RecyclerView.Adapter<InputAdapter.InputViewHoler>{

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    private ArrayList<String> InputBox;
    //private ArrayList<String>


    public InputAdapter(ArrayList<String> list){
        InputBox = list;
    }

    @Override
    public InputAdapter.InputViewHoler onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.inputbox, parent, false);


        return new InputViewHoler(view);
    }

    @Override
    public  void onBindViewHolder(InputAdapter.InputViewHoler holder, int position){
        String text = InputBox.get(position);

        holder.inputText.setText(text);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


            }
        });
    }

    @Override
    public int getItemCount(){
        return InputBox.size();
    }

    public class InputViewHoler extends RecyclerView.ViewHolder{
        TextView inputText;

        InputViewHoler(View itemView){
            super(itemView);

            inputText = itemView.findViewById(R.id.inputText);

            inputText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(onItemClickListener !=null){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
