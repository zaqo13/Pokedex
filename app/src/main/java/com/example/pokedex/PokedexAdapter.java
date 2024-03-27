package com.example.pokedex;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {
    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout containerView;
        private TextView textView;

        PokedexViewHolder(View view) {
            // constructor
            super(view);
            containerView = view.findViewById(R.id.pokedex_row);    // R == resource
            textView = view.findViewById(R.id.pokedex_row_text_view);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pokemon current = (Pokemon) containerView.getTag();

                    Intent intent = new Intent(v.getContext(), PokemonActivity.class);
                    Log.d("PokodexAdapter", "name: " + current.getName() + " " + "url: " + current.getUrl());

                    intent.putExtra("namePokemon", current.getName());
                    intent.putExtra("url", current.getUrl());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }   // PokedexAdapter: PokedexViewHolder:

//    private List<Pokemon> pokemon = Arrays.asList(
//            new Pokemon("Pikkachu", 2),
//            new Pokemon("Venusur", 2),
//            new Pokemon("Ivasur", 3)
//    );

    private List<Pokemon> pokemons = new ArrayList<>();
    private RequestQueue requestQueue;

    PokedexAdapter(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        loadPokemon();
    }

    public void loadPokemon() {

        String url = "https://pokeapi.co/api/v2/pokemon?limit=60&offset=0";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject result = results.getJSONObject(i);
                                String name = result.getString("name");
                                String url = result.getString("url");
                                pokemons.add(new Pokemon(
                                        name.substring(0, 1).toUpperCase() + name.substring(1),
                                        url));
                            }

                            notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("PokodexAdapter: loadPokomon", "JSONArray results error" + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PokodexAdapter: loadPokomon", "Pokemon list error");
            }
        });

        requestQueue.add(request);


    }   // PokedexAdapter: loadPokemon():

    @NonNull
    @Override
    // this method convert this xml file into java memory!
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokedex_row, parent, false);

        return new PokedexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        Pokemon current = pokemons.get(position);
        holder.textView.setText(current.getName());
        holder.containerView.setTag(current);

    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }
}
