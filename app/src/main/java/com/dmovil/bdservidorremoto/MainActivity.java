package com.dmovil.bdservidorremoto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dmovil.bdservidorremoto.entidades.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//Se crea la actividad y la funcionalidad de la aplicacion, insertar, buscar, editar y actualizar
public class MainActivity extends AppCompatActivity {

    //Se crean las variables de instancia para los EditText
    EditText control, nombre, apellidos, carrera, telefono, email, direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se castean las variables con los EditText
        control = findViewById(R.id.edt_noControl);
        nombre = findViewById(R.id.edt_nombre);
        apellidos = findViewById(R.id.edt_apellidos);
        carrera = findViewById(R.id.edt_carrera);
        telefono = findViewById(R.id.edt_telefono);
        email = findViewById(R.id.edt_email);
        direccion = findViewById(R.id.edt_direccion);
        control.requestFocus();

    }

    //Metodo para insertar en la BD utilizando un StringRequest,
    // recibe como parametro una variable String para la URL del web service
    private void peticion (String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new
            Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "Se han registrado los datos con exito", Toast.LENGTH_LONG).show();
                    limpiar();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(),
                            Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    System.err.println(error.getMessage());

                    System.err.println(error.toString());
                }
        }){
            //Método que envía una coleccion Map con los datos del alumno
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("control", control.getText().toString());
                parametros.put("nombre", nombre.getText().toString());
                parametros.put("apellidos", apellidos.getText().toString());
                parametros.put("carrera", carrera.getText().toString());
                parametros.put("telefono",telefono.getText().toString());
                parametros.put("email", email.getText().toString());
                parametros.put("direccion", direccion.getText().toString());
                return parametros;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
    }

    //Método para insertar un registro utilizando un JsonObjectRequest,
    // recibe como parametro la URL del web service y envía al web service
    // un objeto JSON con los datos del registro
    public void insertarJSON(String URL) {
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("control", control.getText().toString());
            parametros.put("nombre", nombre.getText().toString());
            parametros.put("apellidos", apellidos.getText().toString());
            parametros.put("carrera", carrera.getText().toString());
            parametros.put("telefono", telefono.getText().toString());
            parametros.put("email", email.getText().toString());
            parametros.put("direccion", direccion.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(parametros);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.get("respuesta").toString().equals("ok")){
                                Toast.makeText(getApplicationContext(), "Los datos se cargaron correctamente",
                                        Toast.LENGTH_SHORT).show();
                                limpiar();
                            } else {
                                Toast.makeText(getApplicationContext(), response.get("respuesta").toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Problemas con el servidor"
                                        + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Se agrega el objeto request a la cola de peticiones con la clase Singleton
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    //Metodo para registrar,
    // valida todos los campos, llama al metodo insertarJSON y le pasa como parametro la URl del
    // web service insertar_datos2.php
    public void registrar(View view) {
        if (validaciones()){

            String url = "http://localhost:8001/inventario/articulos/listar";

            //peticion(url);

            insertarJSON(url);

        }
    }

    //Método para buscar un registro
    //Valida el campo de número de control y llama al método consultarRegistro,
    // como parametros se le pasan la URL del web service y el número de control
    public void buscar(View view) {
        if (validarConsulta()){
            String URL = "https://proyect-final-android.000webhostapp.com/consultar.php";
            consultarRegistro(URL, control.getText().toString());
        }
    }

    //Consulta un registro por medio de una peticion JsonObjectRequest,
    // recibe como parametros una URL y el número de control del alumno a buscar
    public void consultarRegistro(String URL, String noControl) {

        //Al utilizar el método de envio GET se crea una URL compuesta por la direccion del web service
        // y el argumento del número de control
        String URLBusqueda = URL + "?control=" + noControl;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URLBusqueda, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("respuesta")) {
                                Toast.makeText(getApplicationContext(), response.getString("respuesta"),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                control.setText(response.getString("no_control"));
                                nombre.setText(response.getString("nombre"));
                                apellidos.setText(response.getString("apellidos"));
                                carrera.setText(response.getString("carrera"));
                                telefono.setText(response.getString("telefono"));
                                email.setText(response.getString("correo"));
                                direccion.setText(response.getString("direccion"));

                                Toast.makeText(getApplicationContext(), "Consulta exitosa",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en el servidor\n" + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
/*
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URLBusqueda, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() == 1){
                            try {
                                JSONObject object = new JSONObject(response.get(0).toString());
                                control.setText(object.getString("no_control"));
                                nombre.setText(object.getString("nombre"));
                                apellidos.setText(object.getString("apellidos"));
                                carrera.setText(object.getString("carrera"));
                                telefono.setText(object.getString("telefono"));
                                email.setText(object.getString("correo"));
                                direccion.setText(object.getString("direccion"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "El registro no existe",
                                    Toast.LENGTH_SHORT).show();
                            limpiar();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en el servidor\n" + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
*/
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    //Método para editar, primero valida los campos y crea un objeto JSON con los datos del registro
    // que se va a actualizar, después llama al método editarRegistro y se le pasa como parametros
    // la URL del web service y el objeto JSON
    public void editar(View view) {

        String URL = "https://proyect-final-android.000webhostapp.com/editar.php";

        if (validaciones()){

            JSONObject camposRegistro = new JSONObject();
            try {
                camposRegistro.put("nombre", nombre.getText().toString());
                camposRegistro.put("apellidos", apellidos.getText().toString());
                camposRegistro.put("carrera", carrera.getText().toString());
                camposRegistro.put("telefono", telefono.getText().toString());
                camposRegistro.put("email", email.getText().toString());
                camposRegistro.put("direccion", direccion.getText().toString());
                camposRegistro.put("control", control.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(camposRegistro);

            editarRegistro(URL, camposRegistro);
        }
    }

    //Método para editar un registro, recibe como parametros la URL del web service y el objeto JSON
    // con los datos del registro que se va a actualizar, utiliza una peticion JsonObjectRequest.
    public void editarRegistro(String URL, JSONObject camposRegistro){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, camposRegistro,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resultado = response.getString("respuesta");

                            if (resultado.equals("1")){
                                Toast.makeText(getApplicationContext(), "Se actualizó el alumno",
                                        Toast.LENGTH_SHORT).show();
                                limpiar();
                            } else if (resultado.equals("0")){
                                Toast.makeText(getApplicationContext(), "No se realizaron cambios",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error en la consulta",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en el servidor\n" + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    //Método para eliminar, primero valida el número de control, después llama al metodo eliminarRegistro
    // y se le pasa como parametro la URL del web service y el numero de control.
    public void eliminar(View view) {

        String URL = "https://proyect-final-android.000webhostapp.com/eliminar.php";

        if (validarConsulta()){
            eliminarRegistro(URL, control.getText().toString());
        }
    }

    //Elimina un registro, recibe un atributo de URL y el número de control del registro que se va
    // a eliminar, se crea un objeto JSON con el número de control y se envia al web service con
    // una peticion JsonObjectRequest, y se agrega a la cola de peticiones
    public void eliminarRegistro(String URL, String noControl){
        JSONObject parametro = new JSONObject();
        try {
            parametro.put("control", noControl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, parametro,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resultado = response.getString("respuesta");
                            if (resultado.equals("1")){
                                Toast.makeText(getApplicationContext(), "Se eliminó el alumno",
                                        Toast.LENGTH_SHORT).show();
                                limpiar();
                            } else if (resultado.equals("0")){
                                Toast.makeText(getApplicationContext(), "No existe el número de control ingresado",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error en la consulta",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en el servidor\n" + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    //Consulta todos los registros de una tabla, aun no se implemente
    public void consultarLista(String URL) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject objeto = new JSONObject(response.get(i).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en el servidor" + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    //Método para validar los editText, devuelve un valor booleano,
    // verdadero si ningun campo esta vacío y con la estructura correcta o falso si alguna
    // condicion no se cumple y muestra un mensaje de error con instrucciones.
    public boolean validaciones(){
        boolean bandera = true;
        if(control.getText().toString().isEmpty()){
            control.setError("Dato requerido");
            bandera = false;
        }
        if(nombre.getText().toString().isEmpty()){
            nombre.setError("Dato requerido");
            bandera = false;
        }
        if(apellidos.getText().toString().isEmpty()){
            apellidos.setError("Dato requerido");
            bandera = false;
        }
        if(carrera.getText().toString().isEmpty()){
            carrera.setError("Dato requerido");
            bandera = false;
        }
        if (email.getText().toString().isEmpty()){
            email.setError("Dato requerido");
            bandera = false;
        } else if(!email.getText().toString().matches("[a-zA-Z0-9]+[-_.]*[a-zA-Z0-9]+\\@[a-zA-Z]+\\.[a-zA-Z]+")){
            email.setError("Introduzca una dirección de correo electrónico válida");
            bandera = false;
        }
        if (telefono.getText().toString().isEmpty()){
            telefono.setError("Dato requerido");
            bandera = false;
        } else if(!telefono.getText().toString().matches("(\\+?[0-9]{2,3}\\-)?([0-9]{10})")){
            telefono.setError("El telefono debe contener 10 digitos");
            bandera = false;
        }
        if(direccion.getText().toString().isEmpty()){
            direccion.setError("Este campo es obligatorio");
            bandera = false;
        }
        return bandera;
    }

    //Valida que el campo control se encuentre lleno y manda un mensaje de advertencia si no es así.
    public boolean validarConsulta() {
        boolean bandera = true;
        if (control.getText().toString().isEmpty()) {
            control.setError("Se requiere un criterio de busqueda");
            bandera = false;
        }
        return bandera;
    }

    //Limpia los EditText y enfoca al EditText de control
    private void limpiar (){
        control.setText("");
        nombre.setText("");
        apellidos.setText("");
        carrera.setText("");
        telefono.setText("");
        email.setText("");
        direccion.setText("");
        control.requestFocus();
    }
}