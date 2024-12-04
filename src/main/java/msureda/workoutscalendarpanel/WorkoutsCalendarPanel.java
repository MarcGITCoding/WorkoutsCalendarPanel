package msureda.workoutscalendarpanel;

import java.awt.Color;
import java.io.Serializable;
import javax.swing.JPanel;

/**
 *
 * @author Marc Sureda
 */
public class WorkoutsCalendarPanel extends JPanel implements Serializable {
    private int year;
    private int month;
    private Color activeButtonColor = new Color(144, 238, 144);
    
    public WorkoutsCalendarPanel (int year, int month) {
        this.year = year;
        this.month = month;
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
