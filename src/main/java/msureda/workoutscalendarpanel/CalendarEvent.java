package msureda.workoutscalendarpanel;

import java.util.ArrayList;
import java.util.EventObject;
import msureda.workoutscalendarpanel.dto.Workout;

/**
 * Custom event for the workouts calendar listener
 * @author Marc Sureda
 */
public class CalendarEvent extends EventObject {
    private final ArrayList<Workout> workouts;

    public CalendarEvent(Object source, ArrayList<Workout> workouts) {
        super(source);
        this.workouts = workouts;
    }

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }
}
