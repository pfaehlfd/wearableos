package de.unistuttgart.vis.wearable.os.api;

import android.os.Parcel;
import de.unistuttgart.vis.wearable.os.activity.ActivityEnum;

/**
 * @author roehrdor
 */
public class ActivityChangedCallback extends BaseCallbackObject {
    ActivityEnum activity;

    /**
     * Create a new Activity changed object with the given activity
     *
     * @param activity the activity
     */
    public ActivityChangedCallback(ActivityEnum activity) {
        this.activity = activity;
    }

    /**
     * Create a new Activity changed object from the parcel
     *
     * @param in the parcel to read the object from
     */
    public ActivityChangedCallback(Parcel in) {
        this.activity = ActivityEnum.values()[in.readInt()];
    }

    public static final Creator<ActivityChangedCallback> CREATOR = new Creator<ActivityChangedCallback>() {
        @Override
        public ActivityChangedCallback createFromParcel(Parcel source) {
            return new ActivityChangedCallback(source);
        }

        @Override
        public ActivityChangedCallback[] newArray(int size) {
            return new ActivityChangedCallback[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Signal the object being send is type of call back object
        dest.writeInt(2);
        dest.writeInt(this.activity.ordinal());
    }
}