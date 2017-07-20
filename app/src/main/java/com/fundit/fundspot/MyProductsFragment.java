package com.fundit.fundspot;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fundit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProductsFragment extends Fragment {

    ListView list_products;
    FloatingActionButton btn_floating_add;

    View view;

    public MyProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_products, container, false);

        fetchIDs();

        return view;
    }

    private void fetchIDs() {
        list_products = (ListView) view.findViewById(R.id.list_products);
        btn_floating_add = (FloatingActionButton) view.findViewById(R.id.btn_floating_add);

        btn_floating_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}
