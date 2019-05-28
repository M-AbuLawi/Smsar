package com.yasoft.smsar;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ShowFilter extends Fragment {


    ImageButton dec, inc, inc1, dec1, exit;
    CheckBox mBox;
    boolean parking, mFiltered = false;
    Discover mDiscover = new Discover();
    Bundle bundel;
    RangeSeekBar seekBar;
    Context mContext;
    View root;
    private TextView _screenRooms;
    private TextView _screenBaths;
    private TextView low;
    private TextView high;
    private int numberOfRooms = 1, numberOfBathrooms = 1;
    private int lowestPrice=-1, highestPrice=-1;
    private String from="",to="",city="";
    public ShowFilter() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_filter, container, false);
        mContext = root.getContext();
        hideNavigationBar();

        _screenRooms = root.findViewById(R.id.numofRooms);
        _screenBaths = root.findViewById(R.id.numofbathroom);

        inc = root.findViewById(R.id.increaseR);
        dec = root.findViewById(R.id.decreaseR);

        inc1 = root.findViewById(R.id.increaseB);
        dec1 = root.findViewById(R.id.decreaseB);

        TextView mApply = root.findViewById(R.id.apply);
        TextView mReset = root.findViewById(R.id.reset);

        exit = root.findViewById(R.id.closeFilter);

        seekBar = root.findViewById(R.id.priceRange);
        low = root.findViewById(R.id.low);
        high = root.findViewById(R.id.high);
        seekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

                lowestPrice = ((int) leftValue);
                highestPrice = ((int) rightValue);

                low.setText(lowestPrice + "");
                high.setText(highestPrice + "");
                mFiltered=true;
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
        bundel = new Bundle();

        inc.setOnClickListener(v -> {
            mFiltered=true;
            ++numberOfRooms;
            updateScreen();
        });

        dec.setOnClickListener(v -> {
            --numberOfRooms;
            updateScreen();
        });


        inc1.setOnClickListener(v -> {
            ++numberOfBathrooms;
            mFiltered=true;
            updateScreen();
        });

        dec1.setOnClickListener(v -> {
            --numberOfBathrooms;
            updateScreen();
        });


        mBox = root.findViewById(R.id.parkingAsk);
        //mSwitch;


        mApply.setOnClickListener(v -> {
            parking = mBox.isChecked();
            parseData();
            Objects.requireNonNull(getActivity()).onBackPressed();
        });


        bundel.putBoolean("Filtered", mFiltered);
        mReset.setOnClickListener(v -> {

            reset();
            updateScreen();

        });

        Button mCity = root.findViewById(R.id.city);
        Button mDate = root.findViewById(R.id.date);


     //   String[] listItem = getResources().getStringArray(R.array.cities);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.fragment_testing,
                getResources().getStringArray(R.array.cities));


        mCity.setOnClickListener(v -> showCitiesDialog(adapter));
        mDate.setOnClickListener(v -> showDatePicker());

        exit.setOnClickListener(v -> exit());

        //     ((UserMainActivity)getActivity()).parseDataFilter(numberOfRooms,numberOfBathrooms,parking);

        return root;
    }


    private void exit() {
        Objects.requireNonNull(getActivity()).onBackPressed();
        mFiltered=false;
    }


    private void updateScreen() {

        check();

        _screenRooms.setText(numberOfRooms + "");
        _screenBaths.setText(numberOfBathrooms + "");
    }

    void reset(){
        mFiltered = false;
        mBox.setChecked(false);
        numberOfBathrooms = 1;
        numberOfRooms = 1;
        lowestPrice=-1;
        highestPrice=-1;
        from="";
        to="";



    }
    void check() {
        if (numberOfRooms < 0)
            numberOfRooms = 0;
        if (numberOfBathrooms < 0)
            numberOfBathrooms = 0;
    }


    public void showCitiesDialog(ArrayAdapter adapter) {
        DialogPlus dialog = DialogPlus.newDialog(mContext)
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        city=item.toString();
                        mFiltered=true;
                        dialog.dismiss();
                    }
                })
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        dialog.show();
    }

    public void showDatePicker() {
        new DoubleDateAndTimePickerDialog.Builder(mContext)
                .bottomSheet()
                .title("Date")
                .tab0Text("From")
                .tab1Text("To")
                .titleTextColor(Color.BLACK)
                .listener(dates ->{from=dates.get(0).toString();to=dates.get(1).toString();})
                .mainColor(getResources().getColor(R.color.gray))
                .setTab0DisplayHours(false)
                .setTab0DisplayMinutes(false)

                .setTab1DisplayHours(false)
                .setTab1DisplayMinutes(false)
                .display();
        mFiltered=true;
    }

    private void parseData(){

        HashMap<String,Object> list=new HashMap<>();
        if (numberOfBathrooms>0)
            list.put("noBathrooms",numberOfBathrooms);
        if(numberOfRooms>0)
            list.put("noRooms",numberOfRooms);
        if(lowestPrice>-1)
            list.put("lowest",lowestPrice);
        if(highestPrice>-1)
            list.put("highest",highestPrice);
        list.put("parking",parking);
        if(from.length()>1)
            list.put("form",from);
        if(to.length()>1)
             list.put("to",to);
        if(city.length()>1)
            list.put("mCity",city);


        Bundle bundle = new Bundle();
        bundle.putSerializable("hashmap",list);
        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getBaseContext(),
                SmsarMainActivity.class);

        ((SmsarMainActivity)mContext).parsDataToFilter(bundle);
        getActivity().onBackPressed();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDiscover.setArguments(bundel);
        if (root.getContext().toString().contains("User")) {
            ((UserMainActivity) Objects.requireNonNull(getActivity())).showBottomNavigationView();

        } else if (root.getContext().toString().contains("Smsar")) {
            ((SmsarMainActivity) Objects.requireNonNull(getActivity())).showBottomNavigationView();
        }
    }

    private void hideNavigationBar() {

        //Hide navigation bar
        //START
        if (root.getContext().toString().contains("User")) {
            ((UserMainActivity) Objects.requireNonNull(getActivity())).hideBottomNavigationView();

        } else if (root.getContext().toString().contains("Smsar")) {
            ((SmsarMainActivity) Objects.requireNonNull(getActivity())).hideBottomNavigationView();
        }
        //END

    }


}
