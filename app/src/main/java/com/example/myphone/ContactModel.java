package com.example.myphone;

import android.graphics.Bitmap;
import android.net.Uri;

public class ContactModel {

        public String id;
        public String name;
        public String mobileNumber;
        public Bitmap photo = null;
        public Uri photoURI;
        public static int count = 0;
        public boolean selected = false;
        public void setSelected(boolean s){
                selected = s;
        }
        public boolean isSelected(){
                return selected;
        }


}
