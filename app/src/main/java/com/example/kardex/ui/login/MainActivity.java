package com.example.kardex.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kardex.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private TextView mt1, mt2;
    private Spinner mProductName ;
    private EditText   mCant, mSumCant;

    private DatabaseReference mDatabase;// ...


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProductName = findViewById(R.id.producName);
        mCant = findViewById(R.id.cant);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();


        Button mDelete = findViewById(R.id.delete);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Integer.parseInt(mCant.getText().toString());
                    if ( email.equals("seller@gmail.com")){
                        saveData(mProductName.getSelectedItem().toString(), mCant.getText().toString(), 0, email);
                    }else{
                        Toast.makeText(getApplicationContext(), "Usuario sin permisos !", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException excepcion) {
                    Toast.makeText(getApplicationContext(), "Cantidad no valida !", Toast.LENGTH_LONG).show();
                }
            }
        });

        readData(mProductName.getSelectedItem().toString());

        Button mGuardar = findViewById(R.id.save);
        mGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    try {
                        Integer.parseInt(mCant.getText().toString());
                        if ( email.equals("seller@gmail.com")){
                            saveData(mProductName.getSelectedItem().toString(), mCant.getText().toString(), 1, email);
                        }else{
                            Toast.makeText(getApplicationContext(), "Usuario sin permisos !", Toast.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException excepcion) {
                        Toast.makeText(getApplicationContext(), "Cantidad no valida !", Toast.LENGTH_LONG).show();
                    }
                 }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });
    }




    public void saveData(String producto,  String cantidad, int tipo, String email){
        mt2 = findViewById(R.id.text2);
        String array = mt2.getText().toString();
        String[] split = array.split("\n");
        int day = 0;
        switch (producto) {
            case "Celular":
                day = Integer.parseInt(split[0]);
                break;
            case "Mause":
                day = Integer.parseInt(split[1]);
                break;
            case "Pantalla":
                day = Integer.parseInt(split[2]);
                break;
            case "Parlante":
                day = Integer.parseInt(split[3]);
                break;
            case "Portatil":
                day = Integer.parseInt(split[4]);
                break;
            case "Teclado":
                day = Integer.parseInt(split[5]);
                break;

        }
            int data = 0;
        if (tipo == 1){
            data = day +Integer.parseInt(cantidad)  ;
        }else{
            data = day - Integer.parseInt(cantidad) ;

        }

        if (data < 0){
            Toast.makeText(getApplicationContext(), "La cantidad a retirar supera el inventario !", Toast.LENGTH_LONG).show();
        }else{
            mDatabase = FirebaseDatabase.getInstance().getReference();

            // Write a message to the database


            Product kardex = new Product(1, producto, Integer.toString(data), email );

            mDatabase.child(producto).setValue(kardex);

            Toast.makeText(getApplicationContext(), "Datos guardados !", Toast.LENGTH_LONG).show();

            mCant = findViewById(R.id.cant);
            mCant.setText("");
        }


    }



/*

    public void saveData(final String producto, final String cantidad, final int tipo){

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // My top posts by number of stars
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.removeEventListener(this);

                if(dataSnapshot.exists()){
                    mDatabase.removeEventListener(this);
                    for (DataSnapshot ds1: dataSnapshot.getChildren()) {
                        // Product post = dataSnapshot.child(prod).getValue(Product.class);
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String nombre = ds.child("producto").getValue(String.class);
                            String cant = ds.child("cantidad").getValue(String.class);

                            if (nombre.equals(producto)){
                                mDatabase.child(producto).setValue(null);
                                mDatabase.removeEventListener(this);

                                if (tipo == 1){
                                    int cantFinal =  Integer.parseInt(cant) + Integer.parseInt(cantidad);
                                    cantKardex(producto, cantFinal);
                                    mDatabase.removeEventListener(this);


                                }else if(tipo == 0){
                                    int cantFinal =  Integer.parseInt(cant) - Integer.parseInt(cantidad);
                                    cantKardex(producto, cantFinal);
                                    mDatabase.removeEventListener(this);

                                }
                            }else{
                                if (tipo == 0){
                                    cantKardex(producto, 0);
                                    mDatabase.removeEventListener(this);
                                }


                                else{
                                    cantKardex(producto, Integer.parseInt(cantidad));
                                    mDatabase.removeEventListener(this);
                                }


                            }

                        }


                    }
                    // TODO: handle the post


                }else{
                    if (tipo == 0){
                        cantKardex(producto, 0);
                        mDatabase.removeEventListener(this);
                    }


                    else{
                        cantKardex(producto, Integer.parseInt(cantidad));
                        mDatabase.removeEventListener(this);
                    }

                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


    }

*/
    public void readData(final String prod){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // My top posts by number of stars
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                       String result = "";
                       String cantis = "";
                       String array = "";
                       int i = 1;
                       int j = 0;
                    for (DataSnapshot ds1: dataSnapshot.getChildren()) {
                       // Product post = dataSnapshot.child(prod).getValue(Product.class);
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String nombre = ds.child("producto").getValue(String.class);
                            String user = ds.child("usuario").getValue(String.class);
                            String cant = ds.child("cantidad").getValue(String.class);
                            int k =  Integer.parseInt(cant);
                                if (i == 1){
                                    result += new String(nombre+"             "+user+"\n");
                                    cantis += new String(cant+"\n");

                                    j += k;
                                    array += cant+",";

                                }

                        }

                        i++;
                    }
                    mt1 = findViewById(R.id.text1);
                    mt2 = findViewById(R.id.text2);
                    mSumCant = findViewById(R.id.editText2);

                    mt1.setText(result);
                    mt2.setText(cantis);
                    mSumCant.setText("Cantidad ("+j+")");

                   // String[] split = array.split(",");
                    // new Cant(split[0], split[1], split[2], split[3], split[4], split[5]);
                    //Toast.makeText(getApplicationContext(),array, Toast.LENGTH_LONG).show();

                }else{

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });




    }





}
