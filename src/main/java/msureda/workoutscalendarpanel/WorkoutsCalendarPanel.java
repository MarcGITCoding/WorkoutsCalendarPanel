package msureda.workoutscalendarpanel;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JPanel;
import msureda.workoutscalendarpanel.dto.Workout;

/**
 *
 * @author Marc Sureda
 */
public class WorkoutsCalendarPanel extends JPanel implements Serializable {
    private int year;
    private int month;
    private Color activeButtonColor = new Color(144, 238, 144);
    
    private final ArrayList<WorkoutsCalendarListener> listeners = new ArrayList<>();
    
    public WorkoutsCalendarPanel (int year, int month) {
        this.year = year;
        this.month = month;
    }
    
    public void addCalendarEventListener(WorkoutsCalendarListener listener) {
        listeners.add(listener);
    }

    private void fireWorkoutsEvent(ArrayList<Workout> dayWorkouts) {
        CalendarEvent event = new CalendarEvent(this, dayWorkouts);
        for (WorkoutsCalendarListener listener : listeners) {
            listener.workoutsSelected(event);
        }
    }
    
    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the activeButtonColor
     */
    public Color getActiveButtonColor() {
        return activeButtonColor;
    }

    /**
     * @param activeButtonColor the activeButtonColor to set
     */
    public void setActiveButtonColor(Color activeButtonColor) {
        this.activeButtonColor = activeButtonColor;
    }
}
