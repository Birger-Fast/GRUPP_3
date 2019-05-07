
package com.example.systemetapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.systemetapp.domain.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Product> products;
    private ListView listView;
    private ArrayAdapter<Product> adapter;

    private void createFakedProducts() {
        products = new ArrayList<>();
        Product p1 = new Product.Builder()
                .alcohol(4.4)
                .name("Pilsner Urquell")
                .nr(1234)
                .productGroup("Öl")
                .type("Öl")
                .volume(330).build();
        Product p2 = new Product.Builder()
                .alcohol(4.4)
                .name("Baron Trenk")
                .nr(1234)
                .productGroup("Öl")
                .type("Öl")
                .volume(330).build();
        products.add(p1);
        products.add(p2);
    }

    private List<Product> jsonToProducts(JSONArray array) {
        Log.d(LOG_TAG, "jsonToProducts()");
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject row = array.getJSONObject(i);
                String name = row.getString("name");
                double alcohol = row.getDouble("alcohol");
                double price = row.getDouble("price");
                int volume = row.getInt("volume");

                Product m = new Product(name, alcohol, price, volume);
                productList .add(m);
                Log.d(LOG_TAG, " * " + m);
            } catch (JSONException e) {
                ; // is ok since this is debug
            }
        }
        return productList;
    }


    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private void setupListView() {
        // look up a reference to the ListView object
        listView = findViewById(R.id.product_list);

        // create an adapter (with the faked products)
        adapter = new ArrayAdapter<Product>(this,
                android.R.layout.simple_list_item_1,
                products);

        // Set listView's adapter to the new adapter
        listView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up faked products
        createFakedProducts();
        // setup listview (and friends)
        setupListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_search:
                Log.d(LOG_TAG, "user presssed SEARCH");
                showSearchDialog();

                break;
            default:
                Log.d(LOG_TAG, "uh oh ;)");
                break;
        }
        return true;
    }



    private static final String MIN_ALCO = "min_alcohol";
    private static final String MAX_ALCO = "max_alcohol";
    private static final String MIN_PRICE = "min_price";
    private static final String MAX_PRICE = "max_price";
    private static final String TYPE = "product_group";
    private static final String NAME = "name";


    // get the entered text from a view
    private String valueFromView(View inflated, int viewId) {
        return ((EditText) inflated.findViewById(viewId)).getText().toString();
    }

    // if the value is valid, add it to the map
    private void addToMap(Map<String, String> map, String key, String value) {
        if (value!=null && !value.equals("")) {
            map.put(key, value);
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search products");
        final View viewInflated = LayoutInflater
                .from(this).inflate(R.layout.search_dialog, null);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Create a map to pass to the search method
                // The map makes it easy to add more search parameters with no changes in method signatures
                Map<String, String> arguments = new HashMap<>();

                // Add user supplied argument (if valid) to the map
                addToMap(arguments, MIN_ALCO, valueFromView(viewInflated, R.id.min_alco_input));
                addToMap(arguments, MAX_ALCO, valueFromView(viewInflated, R.id.max_alco_input));
                addToMap(arguments, MIN_PRICE, valueFromView(viewInflated, R.id.min_price_input));
                addToMap(arguments, MAX_PRICE, valueFromView(viewInflated, R.id.max_price_input));

                // Given the map, s earch for products and update the listview
                searchProducts(arguments);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOG_TAG, " User cancelled search");
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void searchProducts(Map<String, String> arguments) {
        // empty search string will give a lot of products :)
        String argumentString = "";

        // iterate over the map and build up a string to pass over the network
        for (Map.Entry<String, String> entry : arguments.entrySet())
        {
            // If first arg use "?", otherwise use "&"
            // E g:    ?min_alcohol=4.4&max_alcohol=5.4
            argumentString += (argumentString.equals("")?"?":"&")
                    + entry.getKey()
                    + "="
                    + entry.getValue();
        }
        // print argument
        Log.d(LOG_TAG, " arguments: " + argumentString);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://rameau.sandklef.com:9090/search/products/all/" + argumentString;
        Log.d(LOG_TAG, "Searching using url: " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray array) {
                        Log.d(LOG_TAG, "onResponse()");
                        products.clear();
                        products.addAll(jsonToProducts(array));
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
            }
        });
        //Tjosan
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

}