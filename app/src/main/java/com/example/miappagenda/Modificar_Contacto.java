package com.example.miappagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class Modificar_Contacto extends AppCompatActivity {
    Button Mod, Delete, Base;
    EditText ViewNom, ViewTel;
    String id_contacto, nombre_C, Telefono_C;
    Configuraciones config = new Configuraciones();
    String URL = config.URLWB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_contacto);
        ViewNom = findViewById(R.id.MosNom);
        ViewTel = findViewById(R.id.MosTel);
        Mod = findViewById(R.id.Edit_Contac);
        Delete = findViewById(R.id.Del_Contac);
        Base = findViewById(R.id.Back_Base);
        Mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mod_Contac();
            }
        });
        Base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBase_Cont();
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eli_Contac();
            }
        });
    }
    private void GoBase_Cont(){
        Intent BackV = new Intent(Modificar_Contacto.this, MainActivity.class);
        startActivity(BackV);
        Modificar_Contacto.this.finish();
    }
    private void Mod_Contac(){
        RequestQueue Peticion = Volley.newRequestQueue(Modificar_Contacto.this);
        StringRequest Pet = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String Estado = jsonObject.getString("Estado");
                    if (Estado.equals("1")) {
                        Toast.makeText(Modificar_Contacto.this, "Se ha modificaado el contacto", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Modificar_Contacto.this, "Error, No se agregado el contacto", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Modificar_Contacto.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accion", "modificar");
                params.put("id_contacto", id_contacto);
                params.put("nombre", ViewNom.getText().toString());
                params.put("telefono", ViewTel.getText().toString());
                return params;
            }
        };
        Peticion.add(Pet);
    }
    private void Eli_Contac(){
        RequestQueue Peticion = Volley.newRequestQueue(Modificar_Contacto.this);
        StringRequest Pet = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String Estado = jsonObject.getString("Estado");
                    if (Estado.equals("1")) {
                        Toast.makeText(Modificar_Contacto.this, "Se ha eliminado el contacto", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Modificar_Contacto.this, "Error, No se agregado el contacto", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Modificar_Contacto.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accion", "eliminar");
                params.put("id_contacto", id_contacto);
                return params;
            }
        };
        Peticion.add(Pet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle Dta = getIntent().getExtras();
        if(Dta==null){
            Toast.makeText(Modificar_Contacto.this, "Enviar ID del contacto Por favor", Toast.LENGTH_SHORT).show();
            id_contacto = "";
            GoBase_Cont();
        }else {
            id_contacto = Dta.getString("id_contacto");
            nombre_C = Dta.getString("nombre");
            Telefono_C = Dta.getString("telefono");
            Ver_Contacto();
        }
    }
    private void Ver_Contacto(){
        ViewNom.setText(nombre_C);
        ViewTel.setText(Telefono_C);
    }
}