package com.example.lcoll_000.calanderapp;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link onDateSet} interface
 * to handle interaction events.
 * Use the {@link CreateDateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateDateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public TextView date;
    public TextView time;
    public EditText eventName;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private onDateSet mListener;

    public CreateDateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateDateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateDateFragment newInstance(String param1, String param2) {
        CreateDateFragment fragment = new CreateDateFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_date, container, false);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onDateSet) {
            mListener = (onDateSet) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onDateSet");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date = (TextView) view.findViewById(R.id.Date);
        date.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = new DatePickerFragment();
                        Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.main_fragment_container);
                        newFragment.setTargetFragment(f, 0);
                        newFragment.show(getFragmentManager(), "datePicker");
                    }
                }
        );
        time = (TextView) view.findViewById(R.id.Time);
        time.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = new TimePickerFragment();
                        Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.main_fragment_container);
                        newFragment.setTargetFragment(f, 0);
                        newFragment.show(getFragmentManager(), "timePicker");
                    }
                }
        );

        eventName = (EditText) view.findViewById(R.id.editEvent);
        Button submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getActivity();
                String sTime = time.getText().toString();
                String[] aTime = sTime.split(":");
                String sDate = date.getText().toString();
                String[] aDate = sDate.split("/");
                String text = eventName.getText().toString();
                try {
                    main.createDate(Integer.parseInt(aTime[0]), Integer.parseInt(aTime[1]), Integer.parseInt(aDate[0]), Integer.parseInt(aDate[1]), Integer.parseInt(aDate[2]), text);
                    FloatingActionButton fab = (FloatingActionButton) main.findViewById(R.id.fab);
                    fab.show();
                    main.getFragmentManager()
                            .popBackStack();
                    main.getFragmentManager()
                            .popBackStack();

                    main.getFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment_container, CalenderFragment.newInstance(main.eventList, "null") )
                            .addToBackStack(null)
                            .commit();
                    //exit out of fragment
                } catch(NumberFormatException e){
                    Snackbar newSB = Snackbar.make(view, "You Dumbass, put a real date in.", Snackbar.LENGTH_SHORT);
                    newSB.show();

                }
            }
        });

        Button quit = (Button) view.findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getActivity();
                FloatingActionButton fab = (FloatingActionButton) main.findViewById(R.id.fab);
                fab.show();
                main.getFragmentManager().popBackStack();
                //exit fragment
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setDate(int year, int month, int day){

        date.setText(month + "/" + day + "/" + year);
    }

    public void setTime(int hour, int seconds){
        int correctHour = 0;
        if (hour > 12){
            correctHour = hour - 12;
            time.setText(correctHour + ":" + seconds);
        }else {
            time.setText(hour + ":" + seconds);
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    public interface onTimeSet {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface onDateSet {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
