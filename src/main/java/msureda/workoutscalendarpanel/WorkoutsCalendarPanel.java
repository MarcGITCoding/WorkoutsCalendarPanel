package msureda.workoutscalendarpanel;

import java.awt.Color;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import msureda.workoutscalendarpanel.dataaccess.DataAccess;
import msureda.workoutscalendarpanel.dto.Workout;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Marc Sureda
 */
public class WorkoutsCalendarPanel extends JPanel implements Serializable {
    private int year;
    private int month;
    private Color activeButtonColor = new Color(144, 238, 144);
    
    private final ArrayList<WorkoutsCalendarListener> listeners = new ArrayList<>();
    private final ArrayList<Workout> workouts = new ArrayList<>();
    
    public WorkoutsCalendarPanel () {
        setLayout(new MigLayout("wrap 7", "[grow, fill]", "[grow, fill]"));
        
        renderCalendar(year, month);
    }
    
    public void renderCalendar(int year, int month) {
        removeAll();
        
        ArrayList<Workout> fetchedWorkouts = new ArrayList();
        try {
            fetchedWorkouts = DataAccess.getWorkoutsByMonth(year, month);
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutsCalendarPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        workouts.clear();
        workouts.addAll(fetchedWorkouts);
        
        //DAY HEADERS
        for (String day : new String[]{"L", "M", "X", "J", "V", "S", "D"}) {
            add(new JLabel(day, SwingConstants.CENTER), "growx");
        }
        
        revalidate();
        repaint();
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
