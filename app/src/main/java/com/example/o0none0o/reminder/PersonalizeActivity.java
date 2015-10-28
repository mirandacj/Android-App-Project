package com.example.o0none0o.reminder;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Type;

/**
 * Created by o0None0o on 10/1/2015.
 */
public class PersonalizeActivity extends Activity{
    TextView textView;
    private static RadioGroup radio_g;
    private static RadioButton r1;
Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalize_layout);
        textView = (TextView) findViewById(R.id.tvPersonalize);

        radio_g= (RadioGroup)findViewById(R.id.radioGroup);
        }

public void selectFont(View view)
{
    boolean checked = ((RadioButton) view).isChecked();
    switch (view.getId())
    {
        case R.id.radioButton:

                font = Typeface.createFromAsset(getAssets(),"fonts/doridrobot.ttf");
            textView.setTypeface(font);
          break;
        case R.id.radioButton2:
            font=Typeface.createFromAsset(getAssets(),"fonts/Insomia.ttf");
            textView.setTypeface(font);
            break;
        case R.id.radioButton3:
            font=Typeface.createFromAsset(getAssets(),"fonts/JLSSpaceGothicR_NC.otf");
            textView.setTypeface(font);
            break;
    }
}
    }

