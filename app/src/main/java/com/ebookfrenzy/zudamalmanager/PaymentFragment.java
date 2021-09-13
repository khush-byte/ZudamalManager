package com.ebookfrenzy.zudamalmanager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ebookfrenzy.zudamalmanager.databinding.FragmentAgentsBinding;
import com.ebookfrenzy.zudamalmanager.databinding.FragmentPaymentBinding;
import com.ebookfrenzy.zudamalmanager.request.AgentRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PaymentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public FragmentPaymentBinding binding;
    int mYear, mMonth, mDay;
    private long start, end;
    public String d1, d2;
    public int radio = 0;

    public PaymentFragment() {
        // Required empty public constructor
    }

    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
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
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ((FirstActivity) getActivity()).setToolbar("Zudamal Manager", true);
                ((FirstActivity) getActivity()).navController.navigate(R.id.FirstFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd.MM.yyyy").format(c.getTime());
        binding.dateStartCount.setText(date);
        binding.dateEndCount.setText(date);
        start = c.getTimeInMillis();
        end = c.getTimeInMillis();
        Calendar first_date = Calendar.getInstance();
        first_date.add(Calendar.DAY_OF_MONTH, -90);
        final long date_result = first_date.getTimeInMillis();

        binding.progressBarCount.setVisibility(View.GONE);

        binding.dateStartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(year, month, day);
                                @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd.MM.yyyy").format(c.getTime());
                                binding.dateStartCount.setText(date);

                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);

                                start = c.getTimeInMillis();

                                d1 = getDate(start, "yyyy-MM-dd");
                                d2 = getDate(end, "yyyy-MM-dd");

                                Log.i("Debug", d1+" "+d2);
                            }
                        }, mYear, mMonth, mDay);

                dpd.getDatePicker().setMinDate(date_result);
                Calendar a = Calendar.getInstance();
                a.add(Calendar.DAY_OF_MONTH,0);
                dpd.getDatePicker().setMaxDate(a.getTimeInMillis());
                dpd.show();
            }
        });

        binding.dateEndCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start!=0) {
                    DatePickerDialog dpd = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    c.set(year, month, day);
                                    @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd.MM.yyyy").format(c.getTime());
                                    binding.dateEndCount.setText(date);

                                    mYear = c.get(Calendar.YEAR);
                                    mMonth = c.get(Calendar.MONTH);
                                    mDay = c.get(Calendar.DAY_OF_MONTH);

                                    end = c.getTimeInMillis();

                                    d1 = getDate(start, "yyyy-MM-dd");
                                    d2 = getDate(end, "yyyy-MM-dd");
                                    Log.i("Debug", d1+" "+d2);
                                }
                            }, mYear, mMonth, mDay);

                    dpd.getDatePicker().setMinDate(start);
                    Calendar b = Calendar.getInstance();
                    b.add(Calendar.DAY_OF_MONTH, 0);
                    dpd.getDatePicker().setMaxDate(b.getTimeInMillis());
                    dpd.show();
                } else {
                    Toast.makeText(getContext(), "Начальная дата не выбрана!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        d1 = getDate(start, "yyyy-MM-dd");
        d2 = getDate(end, "yyyy-MM-dd");
        Log.i("Debug", d1+" "+d2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCount.setAdapter(adapter);

        binding.spinnerCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                radio =  position;
                Log.i("LOG", String.valueOf(radio));
                ((TextView)parentView.getChildAt(0)).setTextSize(14);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        binding.countCashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CountRequest req = new CountRequest();
                req.activity = CountActivity.this;
                req.execute();*/
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}