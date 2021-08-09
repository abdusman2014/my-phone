package com.example.myphone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.provider.Telephony;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyOwnAdapter extends RecyclerView.Adapter<MyOwnAdapter.MyHolder> {


    MyHolder myholder;
    boolean deleteselect = false;
    ArrayList<ContactModel> data;
    Context ctx;
    int count = 0;
   // Boolean chk[];
   private OnItemClick itemClick;

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }
    MyOwnAdapter(ArrayList<ContactModel> d, Context c){
        data = d;
        ctx = c;
       // chk = new Boolean[data.size()];
      /*  int i = 0;
        int count = 1;
        while(i<data.size()){
          //  Toast.makeText(ctx,String.valueOf(data.size()),Toast.LENGTH_SHORT).show();
            if (data.get(i).photo != null){
                chk[i] = true;
                Toast.makeText(ctx,String.valueOf(count),Toast.LENGTH_SHORT).show();
                count++;
            }
            else
                chk[i] = false;
            i++;
        }*/
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater in = LayoutInflater.from(ctx);
        View myView =   in.inflate(R.layout.my_list, parent, false);
        return new MyHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
            myholder = holder;
            holder.name.setText(data.get(position).name);
            holder.number.setText(data.get(position).mobileNumber);
            if (data.get(position).photo != null){
                holder.image.setImageURI(data.get(position).photoURI);
            }
            else{
                holder.image.setImageResource(R.drawable.my_contact);
            }

        holder.itemView.setBackgroundColor(data.get(position).isSelected() ? Color.GRAY : Color.WHITE);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteselect = true;
                    data.get(position).setSelected(true);
                    holder.itemView.setBackgroundColor(data.get(position).isSelected() ? Color.GRAY : Color.WHITE);
                    count++;
                    if (itemClick == null) {
                        return true;
                    } else {
                        itemClick.onLongPress(v, data.get(position), position);
                        return true;
                    }
                   // return true;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteselect == true){

                        if (data.get(position).isSelected() == true){
                            data.get(position).setSelected(false);
                            count--;
                        }
                        else{
                            data.get(position).setSelected(true);

                            count++;
                        }
                        holder.itemView.setBackgroundColor(data.get(position).isSelected() ? Color.GRAY : Color.WHITE);
                        checkSelection();
                        if (itemClick == null) return;
                        itemClick.onItemClick(v, data.get(position), position);
                    }
                    else {
                        AlertDialog.Builder myAlert;
                        myAlert = new AlertDialog.Builder(ctx);
                        myAlert.setTitle("Choose");
                        myAlert.setMessage("What do you want to do?");

                        // Toast.makeText(ctx,"asdads",Toast.LENGTH_SHORT).show();
                        myAlert.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + data.get(position).mobileNumber));//change the number
                                ctx.startActivity(callIntent);
                            }
                        });
                        myAlert.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                                smsIntent.setType("vnd.android-dir/mms-sms");
                                smsIntent.setData(Uri.parse("smsto:" + data.get(position).mobileNumber));
                                ctx.startActivity(smsIntent);
                          /*  Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setData(Uri.parse("smsto:"+ data.get(position).mobileNumber)); // This ensures only SMS apps respond
                           // intent.putExtra("sms_body", message);
                            if (intent.resolveActivity(ctx.getPackageManager()) != null) {
                                ctx.startActivity(intent);
                            }*/
                            }
                        });
                        // myAlert.setCancelable(false);
                        myAlert.show();
                    }


                    // Toast.makeText(ctx,data.get(position).name,Toast.LENGTH_SHORT).show();
                }
            });

    }

    public void checkSelection(){
        deleteselect = false;
        for (int i=0; i<data.size(); ++i){
            if (data.get(i).isSelected() == true){
                deleteselect = true;
                return;
            }
        }
    }
    public int selectedItemCount(){
        return count;
    }

    public void SelectAll(){

        for (int i=0; i<data.size(); ++i){
            myholder.itemView.setBackgroundColor(data.get(i).isSelected() ? Color.GRAY : Color.GRAY);
                count = data.size();
                data.get(i).setSelected(true);


        }
        Toast.makeText(ctx,String.valueOf(count),Toast.LENGTH_SHORT).show();
    }
    public void clearAll(){
        for (int i=0; i<data.size(); ++i){
            myholder.itemView.setBackgroundColor(data.get(i).isSelected() ? Color.WHITE : Color.WHITE);
            count = 0;
            data.get(i).setSelected(false);
            deleteselect = false;


        }

        Toast.makeText(ctx,String.valueOf(count),Toast.LENGTH_SHORT).show();
    }

    public void deleteContacts(){
        int a = 0;
        for (int i=0; i<data.size(); ++i) {
            if (data.get(i).isSelected() == true) {
                data.remove(i);
                i--;
                a++;
            }
          //  recycler.removeViewAt(position);
          //  mAdapter.notifyItemRemoved(position);
          //  mAdapter.notifyItemRangeChanged(position, list.size());
        }
        deleteselect = false;
        count = 0;
        Toast.makeText(ctx,String.valueOf(data.size()) + "  "+ String.valueOf(a),Toast.LENGTH_SHORT).show();
    }






    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView number;
        ImageView image;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.myName);
            number = (TextView) itemView.findViewById(R.id.myNumber);
            image = (ImageView) itemView.findViewById(R.id.myImage);


           // itemView.setOnClickListener((View.OnClickListener) ctx );
        }


    }
    public interface OnItemClick {


        void onItemClick(View view, ContactModel inbox, int position);

        void onLongPress(View view, ContactModel inbox, int position);
    }
}
