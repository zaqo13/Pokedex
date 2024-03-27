package com.example.pokedex;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonActivity extends AppCompatActivity {

    private TextView tv_pokemonName;
    private TextView tv_url;
    private TextView tv_type_name1;
    private TextView tv_type_name2;
    private TextView tv_id;
    private String url;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        MaterialToolbar materialToolbar = findViewById(R.id.toolbar_pokemon_activity);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name = getIntent().getStringExtra("namePokemon");
        url = getIntent().getStringExtra("url");

        Log.d("PokemonActivity:", "name: " + name + " " + "url: " + url);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        tv_pokemonName = findViewById(R.id.tv_pokemon_name);
        tv_type_name1 = findViewById(R.id.tv_type_name1);
        tv_type_name2 = findViewById(R.id.tv_type_name2);
        tv_id = findViewById(R.id.tv_id);

        tv_pokemonName.setText(name);

        loadPic();

    }   // PokemonActivity: onCreate():

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadPic() {
        Log.d("loadPic:", "loadPic started + url" + url);

        tv_type_name1.setText("");
        tv_type_name2.setText("");  //setting tv_type_name1 empty at the initial stage

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tv_id.setText(String.format("#%03d", response.getInt("id")));        //"#%03d" == #003, #004, #055,....

                            JSONArray typeEntries = response.getJSONArray("types");
                            for (int i = 0; i < typeEntries.length(); i++) {
                                JSONObject typeEntry = typeEntries.getJSONObject(i);
                                int slot = typeEntry.getInt("slot");
                                String typeName = typeEntry.getJSONObject("type").getString("name");

                                if (slot == 1) {
                                    tv_type_name1.setText(typeName);
                                } else if (slot == 2) {
                                    tv_type_name2.setText(typeName);
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("PokodexAdapter: loadPic", "results error " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PokemonActivity: onErrorResponse", "Pokemon list error");
                    }
                });

        requestQueue.add(request);
    }   // PokemonActivity: loadPic()
}   // PokemonActivity: