package com.tunex.mightyglobackend;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.tunex.mightyglobackend.data.Contract.DataEntry;

import org.w3c.dom.Text;

/**
 * Created by hp on 26-Apr-18.
 */

public class DataCursorAdapter extends CursorAdapter {


    /* Constructs a new {@link PetCursorAdapter}.
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public DataCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /* Makes a new blank list item view. No data is set (or bound) to the views yet.
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

//        return LayoutInflater.from(context).inflate(R.layout.list_mighty_api_request_item, parent, false);
        return LayoutInflater.from(context).inflate(R.layout.card_view_item_list_layout, parent, false);
    }

      /* This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the correct row.
     */

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView numberTextView = (TextView) view.findViewById(R.id.number);
        TextView valueTextView = (TextView) view.findViewById(R.id.value);
        TextView costTextView = (TextView) view.findViewById(R.id.cost);
        TextView sourceTextView = (TextView) view.findViewById(R.id.source);
        TextView receivedTimeTextView = (TextView) view.findViewById(R.id.received_time);
        TextView statusTextView = (TextView) view.findViewById(R.id.status);
        TextView timeDoneTextView = (TextView) view.findViewById(R.id.timeDone);


        int recipientNumberColumnIndex = cursor.getColumnIndex(DataEntry.COLUMN_RECIPIENT_NUMBER);
        int bundleValueColumnIndex = cursor.getColumnIndex(DataEntry.COLUMN_BUNDLE_VALUE);
        int costColumnIndex = cursor.getColumnIndex(DataEntry.COLUMN_BUNDLE_COST);
        int sourceColumnIndex = cursor.getColumnIndex(DataEntry.COLUMN_REQUEST_SOURCE);
        int receivedTimeColumnIndex = cursor.getColumnIndex(DataEntry.COLUMN_TIME_RECEIVED);
        int statusColumnIndex = cursor.getColumnIndex(DataEntry.COLUMN_STATUS_);
        int timeDoneColumnIndex = cursor.getColumnIndex(DataEntry.COLUMN_TIME_DONE);

       String currentRecipientNumber = cursor.getString(recipientNumberColumnIndex);
        String currentBundleValue = cursor.getString(bundleValueColumnIndex);
        String currentCost = cursor.getString(costColumnIndex);
        String currentSource = cursor.getString(sourceColumnIndex);
        String currentReceivedTime = cursor.getString(receivedTimeColumnIndex);
//        String currentStatus = cursor.getString(statusColumnIndex);
        String currentTimeDone = cursor.getString(timeDoneColumnIndex);

        numberTextView.setText(currentRecipientNumber);
        valueTextView.setText(currentBundleValue);
        costTextView.setText(currentCost);
        sourceTextView.setText(currentSource);
        receivedTimeTextView.setText(currentReceivedTime);
//        statusTextView.setText(currentStatus);
        timeDoneTextView.setText(currentTimeDone);

    }
}
