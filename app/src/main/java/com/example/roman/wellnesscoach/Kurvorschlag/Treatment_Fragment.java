package com.example.roman.wellnesscoach.Kurvorschlag;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TabStopSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.roman.wellnesscoach.Authentifizierung.MainWindow;
import com.example.roman.wellnesscoach.Kurvorschlag.ProposeTreatment;
import com.example.roman.wellnesscoach.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Roman on 28.04.2016.
 */
public class Treatment_Fragment extends Fragment {

    Button equipButton;
    Button startButton;
    LinearLayout treatDetails;
    Button treatFinish;
    TextView treatmentTimer;
    Button pauseFinish;
    TextView pauseTimer;

    View myFragmentView;

    CountDownTimer tTimer;
    CountDownTimer pTimer;
    JSONObject treatmentJSON;
    int treatmentTime;
    int pauseTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.propose_treatment_2, container, false);

        initializeElements();
        initialize();
        startTreatmentTimer();
        endTreatmentTimer();
        onEndPauseClick();
        // Inflate the layout for this fragment
        return myFragmentView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        System.out.println("OnPause is Called!");
        if(tTimer != null) {
            tTimer.cancel();
            tTimer = null;
        }
        if(pTimer != null)
        {
            pTimer.cancel();
            pTimer = null;
        }
    }

    public void initializeElements()
    {
        equipButton = (Button)myFragmentView.findViewById(R.id.bDevice);
        startButton = (Button)myFragmentView.findViewById(R.id.bStart);
        treatFinish = (Button)myFragmentView.findViewById(R.id.bFertig);
        pauseFinish = (Button)myFragmentView.findViewById(R.id.bWeiter);
        treatDetails = (LinearLayout)myFragmentView.findViewById(R.id.treatList);
        pauseTimer = (TextView) myFragmentView.findViewById(R.id.tPause);
        treatmentTimer = (TextView) myFragmentView.findViewById(R.id.tTime);
    }

    public void initialize()
    {
        try {
            if(getArguments().getInt("NbrFragment") != 0)
            {
                startButton.setEnabled(false);
                RelativeLayout background = (RelativeLayout)myFragmentView.findViewById(R.id.FragmentBackground);
                //background.setBackgroundColor(getResources().getColor(R.color.VaporHauptfarbe));
            }

            String treatment = getArguments().getString("Treatment");
            treatmentJSON = new JSONObject(treatment);

            equipButton.setText(treatmentJSON.getString("Device"));

            ArrayList<TextView> textList = new ArrayList<>();

            TextView a = new TextView(getActivity());
            if(!treatmentJSON.getString("Temperatur").equals("false")) {
                a.setText(getTabbedString("Temperatur:", treatmentJSON.getString("Temperatur"), 500), TextView.BufferType.SPANNABLE);
                textList.add(a);
            }

            TextView b =  new TextView(getActivity());
            if(!treatmentJSON.getString("Krauter").equals("false")) {
                b.setText(getTabbedString("Kräuter:", treatmentJSON.getString("Krauter"), 500), TextView.BufferType.SPANNABLE);
                textList.add(b);
            }

            TextView c = new TextView(getActivity());
            if(!treatmentJSON.getString("Light").equals("Ohne")) {
                c.setText(getTabbedString("Lichtfarbe:", treatmentJSON.getString("Light"), 500), TextView.BufferType.SPANNABLE);
                textList.add(c);
            }

            TextView d = new TextView(getActivity());
            if(!treatmentJSON.getString("Musik").equals("Ohne")) {
                d.setText(getTabbedString("Musik:", treatmentJSON.getString("Musik"), 500), TextView.BufferType.SPANNABLE);
                textList.add(d);
            }

            addTextlistToLayout(textList);

            treatmentTimer.setText("Kurdauer: " + treatmentJSON.getString("Time"));
            pauseTimer.setText("Pause: " + treatmentJSON.getString("Pause"));

            treatmentTime = Integer.parseInt(treatmentJSON.getString("Time"));
            pauseTime = Integer.parseInt(treatmentJSON.getString("Pause"));
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    public static SpannableStringBuilder getTabbedString(String a, String b, int tab)
    {
        SpannableStringBuilder span = new SpannableStringBuilder(a + "\t" + b);
        span.setSpan(new TabStopSpan.Standard(tab), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        return span;
    }

    public void addTextlistToLayout(ArrayList<TextView> list)
    {
        for(int i = 0; i < list.size(); i++)
            if (list.get(i) != null) {
                TextView temp = list.get(i);
                temp.setTextColor(getResources().getColor(R.color.VaporZweitfarbe));
                temp.setTextSize(18);
                temp.setTypeface(null, Typeface.BOLD);
                treatDetails.addView(list.get(i));
            }
    }

    public void startTreatmentTimer()
    {
        final Button startButton = (Button)myFragmentView.findViewById(R.id.bStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setEnabled(false);
                Button fertigButton = (Button)myFragmentView.findViewById(R.id.bFertig);
                fertigButton.setEnabled(true);

                tTimer = new CounterClass(treatmentTime*1000, 1000, treatmentTimer)
                {
                    @Override
                    public void onFinish()
                    {
                        cancel();
                        tTimer = null;
                        treatmentTimer.setText("Fertig");
                        Button fertigButton = (Button)myFragmentView.findViewById(R.id.bFertig);
                        fertigButton.setEnabled(false);
                        Button endPauseButton = (Button)myFragmentView.findViewById(R.id.bWeiter);
                        endPauseButton.setEnabled(true);
                        startPauseTimer();
                    }
                };
                tTimer.start();

            }
        });
    }

    public void endTreatmentTimer()
    {
        treatFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tTimer.onFinish();
            }
        });
    }


    public void startPauseTimer()
    {
        pTimer = new CounterClass(pauseTime*1000, 1000, pauseTimer)
        {
            @Override
            public void onFinish()
            {
                cancel();
                pTimer = null;
                Intent nextTreatment = new Intent(getActivity(), ProposeTreatment.class);
                nextTreatment.putExtra("JSON", ProposeTreatment.treatmentString);
                nextTreatment.putExtra("Counter", ProposeTreatment.treatmentcounter);
                startActivity(nextTreatment);
            }
        };
        pTimer.start();
    }

    public void onEndPauseClick()
    {
        pauseFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pTimer.onFinish();
            }
        });
    }

    public class CounterClass extends CountDownTimer {

        TextView targetView;

        public CounterClass(long millisInFuture, long countDownInterval, TextView target) {
            super(millisInFuture, countDownInterval);
            targetView = target;
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println("Timer = " + hms);
            targetView.setText(hms);

        }

        @Override
        public void onFinish() {

        }
    }

}
