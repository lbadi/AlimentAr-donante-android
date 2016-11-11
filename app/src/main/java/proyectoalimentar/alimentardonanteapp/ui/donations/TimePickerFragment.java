package proyectoalimentar.alimentardonanteapp.ui.donations;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    OnSelectedTimeCallback onSelectedTimeCallback;

    public static TimePickerFragment newInstance(OnSelectedTimeCallback onSelectedTimeCallback) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.onSelectedTimeCallback = onSelectedTimeCallback;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),this, hour, minute,
                true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        onSelectedTimeCallback.onSelectedTime(hourOfDay,minute);
        // Do something with the time chosen by the user
    }

    public interface OnSelectedTimeCallback{
        void onSelectedTime(int hour, int minute);
    }
}