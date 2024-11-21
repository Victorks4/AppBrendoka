package com.example.appproject05.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.appproject05.R;

<<<<<<<< HEAD:app/src/main/java/com/example/appproject05/fragments/PedidosFragment.java
public class PedidosFragment extends Fragment {
========
import com.example.appproject05.R;

public class PerfilFragment extends Fragment {
>>>>>>>> origin/master:app/src/main/java/com/example/appproject05/fragments/PerfilFragment.java
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pedidos, container, false);
    }
}