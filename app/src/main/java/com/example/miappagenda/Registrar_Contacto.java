package com.example.miappagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registrar_Contacto extends AppCompatActivity {

    Button Add_C, G_BD;
    EditText Nom, Phone;
    Configuraciones confg = new Configuraciones();
    String URL = confg.URLWB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_contacto);
        Nom = findViewById(R.id.IngreNom);
        Phone = findViewById(R.id.IngreTel);
        Add_C = findViewById(R.id.Agregar);
        G_BD = findViewById(R.id.Regresar);
        Add_C.setOnClickListener(view -> {
            Go_Add();
        });
        G_BD.setOnClickListener(view -> {
            Back();
        });
    }
    private void Back(){
        Intent Regresar = new Intent(Registrar_Contacto.this, MainActivity.class);
        startActivity(Regresar);
        Registrar_Contacto.this.finish();
    }
    private void Go_Add(){
        RequestQueue Peticion = Volley.newRequestQueue(Registrar_Contacto.this);
        StringRequest Pet = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String Estado = jsonObject.getString("Estado");
                    if (Estado.equals("1")) {
                        Toast.makeText(Registrar_Contacto.this, "Se ha agregado el contacto", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Registrar_Contacto.this, "Error, No se agregado el contacto", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Registrar_Contacto.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accion", "registrador");
                params.put("nombre", Nom.getText().toString());
                params.put("telefono", Phone.getText().toString());
                return params;
            }
        };
        Peticion.add(Pet);
    }
}