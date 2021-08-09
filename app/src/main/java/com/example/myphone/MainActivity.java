package com.example.myphone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
//import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView r1;
    MyOwnAdapter ad;
    ArrayList<ContactModel> contact;
    Toolbar toolbar;
    //View myview;
    ImageButton deleteBtn;
    ImageButton selectBtn;
    ImageButton clearBtn;

    
    private static final int READ_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    //private static final int WRITE_PERMISSION_CODE = 101;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // checkPermission(Manifest.permission.READ_CONTACTS,STORAGE_PERMISSION_CODE);
       // checkPermission(Manifest.permission.CALL_PHONE,READ_PERMISSION_CODE);

        while (ContextCompat.checkSelfPermission(MainActivity.this, PERMISSIONS[0]) == PackageManager.PERMISSION_DENIED) {
        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        Toast.makeText(this,"Press Allow",Toast.LENGTH_SHORT).show();
        }
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
           // checkPermission(Manifest.permission.CALL_PHONE,READ_PERMISSION_CODE);
           //finish();
           // System.exit(0);
            contact =  getContacts(this);
        }*/
       /*
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS},
                STORAGE_PERMISSION_CODE);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CALL_PHONE},
                READ_PERMISSION_CODE);*/
      //  Log.d("1234","abcd");
        r1 = (RecyclerView) findViewById(R.id.myRecycler);

       contact =  getContacts(this);


       // contact = new ArrayList<ContactModel>();



     /*   int i=0;
        while(i<contact.size()){
            Log.d(contact.get(i).name + contact.get(i).count,"abcd");
            i++;
        }*/

    // myview = (View) findViewById(R.id.myRecycler);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        deleteBtn = (ImageButton) findViewById(R.id.Button2);
        selectBtn = (ImageButton) findViewById(R.id.selectBtn);
        clearBtn = (ImageButton) findViewById(R.id.clearBtn);
     //   toolbar.setVisibility(View.GONE);
        ad = new MyOwnAdapter(contact,this);
        r1.setAdapter(ad);
        r1.setLayoutManager(new LinearLayoutManager(this));
        r1.addItemDecoration(new DividerItemDecoration(this));
        init();

    }

    public ArrayList<ContactModel> getContacts(Context ctx) {
        ArrayList<ContactModel> list = new ArrayList<>();
            int c = 0;
            ContentResolver contentResolver = ctx.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                        Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                        Bitmap photo = null;
                        if (inputStream != null) {
                            photo = BitmapFactory.decodeStream(inputStream);
                        }
                        while (cursorInfo.moveToNext()) {
                            ContactModel info = new ContactModel();
                            info.id = id;
                            info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            info.photo = photo;
                            info.photoURI = pURI;
                            info.count = c;
                            c++;
                            list.add(info);
                          /*  boolean chk = true;
                            for (int i = 0; i < list.size(); ++i) {
                                if (list.get(i).name == info.name) {
                                    Log.d(list.get(i).name, "abcd");
                                    Toast.makeText(ctx, list.get(i).name, Toast.LENGTH_SHORT).show();
                                    chk = false;
                                }
                            }
                            if (chk == true) {
                                list.add(info);
                            }

                           */
                        }

                        cursorInfo.close();
                    }
                }
                cursor.close();
            }
           return list;
       // }
        //return list;
    }



    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,  @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);


        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // Toast.makeText(MainActivity.this, "Call Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(MainActivity.this, "Call Permission Denied", Toast.LENGTH_SHORT).show();
               // finish();
               // System.exit(0);

            }
        }


       // String req = String.valueOf(requestCode);
       // Toast.makeText(MainActivity.this, req, Toast.LENGTH_SHORT).show();
    }

    public void init(){
        ad.setItemClick(new MyOwnAdapter.OnItemClick() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override

            public void onItemClick(View view, ContactModel inbox, int position) {
                if (ad.selectedItemCount() > 0) {
                    //////////////////////////////////////////////****************************\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                    toolbar.setTitle(String.valueOf(ad.selectedItemCount()) + "Selected");
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "Button Pressed", Toast.LENGTH_SHORT).show();
                            ad.deleteContacts();
                            ad.notifyDataSetChanged();
                            toolbar.setVisibility(View.INVISIBLE);
                            deleteBtn.setVisibility(View.INVISIBLE);
                            selectBtn.setVisibility(View.INVISIBLE);
                            clearBtn.setVisibility(View.INVISIBLE);
                        }
                    });
                    selectBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ad.SelectAll();
                            ad.notifyDataSetChanged();
                            toolbar.setTitle(String.valueOf(ad.selectedItemCount()) + "Selected");
                        }
                    });
                    clearBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ad.clearAll();
                            ad.notifyDataSetChanged();
                            toolbar.setVisibility(View.INVISIBLE);
                            deleteBtn.setVisibility(View.INVISIBLE);
                            selectBtn.setVisibility(View.INVISIBLE);
                            clearBtn.setVisibility(View.INVISIBLE);
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "clicked " + inbox.name, Toast.LENGTH_SHORT).show();
                    toolbar.setVisibility(View.INVISIBLE);
                    deleteBtn.setVisibility(View.INVISIBLE);
                    selectBtn.setVisibility(View.INVISIBLE);
                    clearBtn.setVisibility(View.INVISIBLE);
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onLongPress(View view, ContactModel inbox, int position) {
               // toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
                selectBtn.setVisibility(View.VISIBLE);
                clearBtn.setVisibility(View.VISIBLE);
                toolbar.setTitle(String.valueOf(ad.selectedItemCount()) + "Selected");
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Button Pressed", Toast.LENGTH_SHORT).show();
                        ad.deleteContacts();
                        ad.notifyDataSetChanged();
                        toolbar.setVisibility(View.INVISIBLE);
                        deleteBtn.setVisibility(View.INVISIBLE);
                        selectBtn.setVisibility(View.INVISIBLE);
                        clearBtn.setVisibility(View.INVISIBLE);
                    }
                });
                clearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            ad.clearAll();
                        ad.notifyDataSetChanged();
                        toolbar.setVisibility(View.INVISIBLE);
                        deleteBtn.setVisibility(View.INVISIBLE);
                        selectBtn.setVisibility(View.INVISIBLE);
                        clearBtn.setVisibility(View.INVISIBLE);
                    }
                });
                selectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.SelectAll();
                        ad.notifyDataSetChanged();
                        toolbar.setTitle(String.valueOf(ad.selectedItemCount()) + "Selected");
                    }
                });

            }
        });
    }

     /*
       toggling action bar that will change the color and option
     */



}