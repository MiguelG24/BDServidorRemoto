package com.dmovil.bdservidorremoto.entidades;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.collection.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

//Se crea una clase con patron Singleton para tener un solo objeto y crear una sola cola de peticiones
public final class MySingleton {

    //Declaracion de variables, una estatica del mismo tipo de la clase, una de RequestQueue
    //Una para procesar imagenes y la ultima para el Contexto donde se ejecutara.
    private static MySingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    //Constructor privado que solo será llamado por un método estático, inicializa las variables.
    private MySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    //Método estatico que devuelve el unico objeto de la clase que puede ser creado.
    public static synchronized MySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MySingleton(context);
        }
        return instance;
    }

    //Metodo para obtener la cola de peticiones
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    //Metodo generico que añade un Request(ya sea StringRequest, JsonObjectRequest o JsonArrayRequest)
    // a la cola de peticiones
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    //Obtiene el objeto de ImageLoader.
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
