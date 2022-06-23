package com.example.dailytask.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailytask.AddNewTask;
import com.example.dailytask.MainActivity;
import com.example.dailytask.Model.ToDoModel;
import com.example.dailytask.R;
import com.example.dailytask.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;
    private OnNoteListener onNoteListener;

    public  ToDoAdapter(DatabaseHandler db,MainActivity activity, OnNoteListener onNoteListener){
        this.activity=activity;
        this.onNoteListener=onNoteListener;
        this.db=db;
    }




    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layouts, parent, false);
        return new ViewHolder(itemView,onNoteListener);
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        db.openDatabase();
        ToDoModel item= todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }
                else{
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    public int getItemCount() {
        return todoList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }
    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }
    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }



    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox task;
        OnNoteListener onNoteListener;

        ViewHolder(View view,OnNoteListener onNoteListener) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
            view.setOnClickListener(this);
            this.onNoteListener=onNoteListener;
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }

}
