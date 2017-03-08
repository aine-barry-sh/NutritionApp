package ie.ul.csis.nutrition.user_interface.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ie.ul.csis.nutrition.R;
import ie.ul.csis.nutrition.user_interface.MainActivity;


public class ConfirmationFragment extends Fragment {
    private Button confirmButton;
    private Button rejectButton;

    public ConfirmationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmButton = (Button) view.findViewById(R.id.btn_sendPic);
        rejectButton = (Button) view.findViewById(R.id.btn_removePic);

        configureButtons();

    }


    private void configureButtons() {
        confirmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ((MainActivity) getActivity()).SendImage();
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) getActivity()).rejectPhoto();
            }
        });
    }


}
