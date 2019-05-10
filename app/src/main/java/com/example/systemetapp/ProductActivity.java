package com.example.systemetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import com.example.systemetapp.domain.Product;

public class ProductActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProductActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // extract the Product pass in the bundle
        Bundle extras = getIntent().getExtras();
        Product p = (Product) extras.get("product");
        // display the product
        displayProduct(p);
    }

    private void setViewText(int viewId, String label, String text) {
        TextView tv = findViewById(viewId);
        tv.setText(Html.fromHtml("<b>"+label+"</b>: " + text));
        Log.d(LOG_TAG, " * " + text);
    }

    private void displayProduct(Product product) {
        setViewText(R.id.product_name, "Name", product.name());
        setViewText(R.id.product_volume, "Volume", String.valueOf(product.volume()));
        setViewText(R.id.product_alcohol, "Alcohol", String.valueOf(product.alcohol()));
        setViewText(R.id.product_price, "Price", String.valueOf(product.price()));
    }

}