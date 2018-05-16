package com.tunex.mightyglobackend.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hp on 26-Apr-18.
 */

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.tunex.mightyglobackend";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DATAS = "glo_datas";

    private Contract() {
    }

    public static final class DataEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DATAS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DATAS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DATAS;

        public final static String TABLE_NAME = "glo_backend_table";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_RECIPIENT_NUMBER = "recipient_number";

        public final static String COLUMN_BUNDLE_VALUE = "bundle_value";

        public final static String COLUMN_BUNDLE_COST = "bundle_cost";

        public final static String COLUMN_REQUEST_SOURCE = "request_source";

        public final static String COLUMN_TIME_RECEIVED = "time_received";

        public final static String COLUMN_STATUS_ = "status";

        public final static String COLUMN_TIME_DONE = "time_done";


        /** Request Source */
        public static final String REQUEST_SOURCE_UNKNOWN = "Unknown";
        public static final String REQUEST_SOURCE_AIRTIME = "Airtime";
        public static final String REQUEST_SOURCE_CASH = "Cash";
        public static final String REQUEST_SOURCE_AGENT = "Agent";
        public static final String REQUEST_SOURCE_SALES_REP = "Sales Rep";
        public static final String REQUEST_SOURCE_WEB = "Web";
        public static final String REQUEST_SOURCE_API = "Api";


        /** Bundle Value */
        public static final String BUNDLE_UNKNOWN = "Unknown";
        public static final String ONE_GIG = "1 GB";
        public static final String TWO_GIG = "2 GB";
        public static final String FOUR_FIVE_GIG = "4.5 GB";
        public static final String SEVEN_TWO_GIG = "7.2 GB";
        public static final String TWELVE_FIVE_GIG = "12.5 GB";
        public static final String FIFTEEN_SIX_GIG = "15.6 GB";
        public static final String TWENTY_FIVE_GIG = "25 GB";
        public static final String TWELVE_FIVE_MB = "12.5 Mb";


        /** Bundle Price */
        public static final String BUNDLE_UNKOWN_PRICE = "Unknown";
        public static final String ONE_GIG_PRICE = "#1000";
        public static final String TWO_GIG_PRICE = "#2000";
        public static final String FOUR_FIVE_GIG_PRICE = "#3000";
        public static final String SEVEN_TWO_GIG_PRICE = "#4000";
        public static final String TWELVE_FIVE_GIG_PRICE = "#5000";
        public static final String FIFTEEN_SIX_GIG_PRICE = "#6000";
        public static final String TWENTY_FIVE_GIG_PRICE = "#7000";
        public static final String TWELVE_FIVE_MB_PRICE = "#8000";



    }

}
