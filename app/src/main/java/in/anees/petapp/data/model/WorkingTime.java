package in.anees.petapp.data.model;

import java.util.Date;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class WorkingTime {
    private Date mOpeningTime;
    private Date mClosingTime;

    public Date getOpeningTime() {
        return mOpeningTime;
    }

    public WorkingTime setOpeningTime(Date mOpeningTime) {
        this.mOpeningTime = mOpeningTime;
        return this;
    }

    public Date getClosingTime() {
        return mClosingTime;
    }

    public WorkingTime setClosingTime(Date mClosingTime) {
        this.mClosingTime = mClosingTime;
        return this;
    }
}
