package be.howest.lolmetabuilder.DAL;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Milan on 17/12/2014.
 */
public class DataLoader extends AsyncTaskLoader<Cursor> {
    private final String mSql;
    private Cursor mData;

    public DataLoader(Context context, String sql) {
        super(context);
        this.mSql = sql;
    }

    public Cursor loadInBackground() {
        Helper helper = Helper.getInstance(getContext());

        SQLiteDatabase database = helper.getReadableDatabase();

        mData = database.rawQuery(mSql, null);

        mData.getCount();

        return mData;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if(isReset()) {
            if(cursor != null) {
                cursor.close();
            }
            return;
        }

        Cursor oldData = mData;
        mData = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if(oldData != null && oldData != cursor && !oldData.isClosed()) {
            oldData.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if(mData != null) {
            deliverResult(mData);
        }

        if(takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() { cancelLoad();}

    @Override
    public void onCanceled(Cursor cursor) {
        if(cursor != null && !cursor.isClosed()) {
            mData.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if(mData != null && !mData.isClosed()) {
            mData.close();
        }

        mData = null;
    }

    private void releaseResources(Cursor data) {
        data.close();
    }
}
