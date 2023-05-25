package com.example.miappagenda;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button Boton_Buscar, BotonAgregar;
    ListView List_Contact;
    EditText Text_Criterio;
    Configuraciones config = new Configuraciones();
    String URL = config.URLWB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BotonAgregar = findViewById(R.id.Agregar);
        BotonAgregar.setOnClickListener(view -> {
            Intent Ventana = new Intent(MainActivity.this, Registrar_Contacto.class);
            startActivity(Ventana);
        });
        Text_Criterio = findViewById(R.id.txtCriterio);
        List_Contact = findViewById(R.id.lvContacto);
        Boton_Buscar = findViewById(R.id.Buscar);
        Boton_Buscar.setOnClickListener(view -> {
            LLenar_Lista();
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            LLenar_Lista();
        }catch (Exception error){
            Toast.makeText(MainActivity.this, "No hubo conexión", Toast.LENGTH_SHORT).show();
        }
    }
    public void LLenar_Lista(){
        final String Criterio = Text_Criterio.getText().toString();
        RequestQueue Peticion = Volley.newRequestQueue(MainActivity.this);
        StringRequest Obj = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("resultado");
                    AdaptadorLista adaptador = new AdaptadorLista();
                    adaptador.Arreglo = jsonArray;
                    List_Contact.setAdapter(adaptador);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accion", "listar_contactos");
                params.put("filtro", Criterio);
                return params;
            }
        };
        Peticion.add(Obj);
    }
    class AdaptadorLista extends BaseAdapter{
        public JSONArray Arreglo;

        @Override
        public int getCount() {
            return Arreglo.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.fila_contacto, null);
            TextView texTitulo = (TextView) view.findViewById(R.id.tvTituloFilaContacto);
            TextView texTelefono = (TextView) view.findViewById(R.id.tvTelefonoFilaContacto);
            Button btnVer = (Button) view.findViewById(R.id.btnVerContacto);
            JSONObject object = null;
            try {
                object = Arreglo.getJSONObject(position);
                final String id_contacto, nombre, telefono;
                id_contacto = object.getString("id_contacto");
                nombre = object.getString("nombre");
                telefono = object.getString("telefono");
                texTitulo.setText(nombre);
                texTelefono.setText(telefono);
                btnVer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent Vis = new Intent(MainActivity.this, Modificar_Contacto.class);
                        Vis.putExtra("id_contacto", id_contacto);
                        Vis.putExtra("nombre", nombre);
                        Vis.putExtra("telefono", telefono);
                        startActivity(Vis);
                    }
                });
            }catch (JSONException e){
                e.printStackTrace();
            }
            return view;
        }
    }
}