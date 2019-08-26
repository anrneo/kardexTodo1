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
    private TextView mTextProd, mTextCant, mtextUser;
    private Spinner mProductName ;
    private EditText   mCant;

    private DatabaseReference mDatabase;// ...


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // mDatabase = FirebaseDatabase.getInstance().getReference();



        final Spinner mProdConsul = findViewById(R.id.search);

        Button mSearch = findViewById(R.id.consultar);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prod = mProdConsul.getSelectedItem().toString();
                        readData(prod);
                   }
        });

        Button mDelete = findViewById(R.id.delete);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductName = findViewById(R.id.producName);
                mCant = findViewById(R.id.cant);

                try {
                    Integer.parseInt(mCant.getText().toString());
                    saveData(mProductName.getSelectedItem().toString(), mCant.getText().toString(), 0);
                } catch (NumberFormatException excepcion) {
                    Toast.makeText(getApplicationContext(), "Cantidad no valida !", Toast.LENGTH_LONG).show();
                }
            }
        });



        Button mGuardar = findViewById(R.id.save);
        mGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductName = findViewById(R.id.producName);
                mCant = findViewById(R.id.cant);

                    try {
                        Integer.parseInt(mCant.getText().toString());
                        saveData(mProductName.getSelectedItem().toString(), mCant.getText().toString(), 1);
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

    public void cantKardex(String producto,  int cantFinal){
        int cantidad;
        if (cantFinal < 0)
            cantidad = 0;
        else
            cantidad = cantFinal;
        // Write a message to the database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        Product kardex = new Product(1, producto,Integer.toString(cantidad), email );

        mDatabase.child(producto).setValue(kardex);
        mCant = findViewById(R.id.cant);
        mCant.setText("");
        Toast.makeText(getApplicationContext(), "Datos guardados !", Toast.LENGTH_LONG).show();

    }





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


    public void readData(final String prod){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // My top posts by number of stars
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    int i =1;
                    for (DataSnapshot ds1: dataSnapshot.getChildren()) {
                       // Product post = dataSnapshot.child(prod).getValue(Product.class);
                        int j=0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String nombre = ds.child("producto").getValue(String.class);
                            String user = ds.child("usuario").getValue(String.class);
                            String cant = ds.child("cantidad").getValue(String.class);

                            if (nombre.equals(prod)){

                                mTextProd = findViewById(R.id.textProd);
                                mTextProd.setText(nombre);

                                mTextCant = findViewById(R.id.textCant);
                                mTextCant.setText(cant);

                                mtextUser = findViewById(R.id.TextUser);
                                mtextUser.setText(user);

                            }else{
                               // Toast.makeText(getApplicationContext(), "Crea el producto ingresando la cantidad", Toast.LENGTH_LONG).show();
                            }
                            j++;

                        }


                        i++;
                    }
                    // TODO: handle the post


                }else{
                    Toast.makeText(getApplicationContext(), "Error en consulta", Toast.LENGTH_LONG).show();
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
