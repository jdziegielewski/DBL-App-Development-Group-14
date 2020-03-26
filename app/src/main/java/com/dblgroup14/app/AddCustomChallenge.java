package com.dblgroup14.app;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.dblgroup14.support.ImageAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCustomChallenge#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCustomChallenge extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    
    public AddCustomChallenge() {
        // Required empty public constructor
    }
    
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCustomChallenge.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCustomChallenge newInstance(String param1, String param2) {
        AddCustomChallenge fragment = new AddCustomChallenge();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    
        GridView gridview = (GridView) getActivity().findViewById(R.id.gv);
        gridview.setAdapter(new ImageAdapter(this.getActivity()));
    
    
    
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        
        
        
            @Override
            public void onItemClick(AdapterView<?> arg0, View imageview, int pos,
                    long arg3) {
            
                Toast.makeText(getActivity().getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
            
            }
        });
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_custom_challenge, container, false);
    }
}
