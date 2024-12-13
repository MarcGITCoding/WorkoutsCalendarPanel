package msureda.workoutscalendarpanel;

import msureda.workoutscalendarpanel.components.CustomMonthYearLabel;
import msureda.workoutscalendarpanel.events.CalendarEventListener;
import msureda.workoutscalendarpanel.events.CalendarEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
    LocalDate currentDate = LocalDate.now();

    private String connectionString = "jdbc:sqlserver://localhost;database=simulapdb;user=sa;"
                                     + "password=1234;encrypt=false;";
    private DataAccess dataAccess;
    private int year = currentDate.getYear();
    private int month = currentDate.getMonthValue();
    private Color activeButtonColor = new Color(60, 179, 113);
    private Point initialClick;
    private CustomMonthYearLabel monthYearLabel;
    
    private final ArrayList<CalendarEventListener> listeners = new ArrayList<>();
    private final ArrayList<Workout> workouts = new ArrayList<>();
    
    public WorkoutsCalendarPanel () {
        dataAccess = new DataAccess(connectionString);
        setLayout(new MigLayout("wrap 7", "[grow, fill]", "[grow, fill]"));

        renderCalendar(year, month);
        
        addSwipeListeners();
    }
    
    public void renderCalendar(int year, int month) {
        removeAll();
        
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int totalDays = yearMonth.lengthOfMonth();
        
        JPanel headerPanel = new JPanel(new MigLayout("insets 0", "[grow][right]", "[]"));
        headerPanel.setOpaque(false);

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES"));
        
        monthYearLabel = new CustomMonthYearLabel();
        monthYearLabel.setText(monthFormatter.format(yearMonth).toUpperCase());
        monthYearLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        monthYearLabel.setForeground(new Color(252, 220, 136));
        headerPanel.add(monthYearLabel, "growx, gapleft 10");

        JButton upButton = createNavigationButton("▲", e -> previousMonth());
        JButton downButton = createNavigationButton("▼", e -> nextMonth());
        headerPanel.add(upButton, "growx, gapright 10");
        headerPanel.add(downButton, "growx, gapright 10");

        add(headerPanel, "span, growx, align center");
        
        ArrayList<Workout> fetchedWorkouts = new ArrayList();
        try {
            fetchedWorkouts = dataAccess.getWorkoutsByMonth(year, month);
        } catch (SQLException ex) {
            Logger.getLogger(WorkoutsCalendarPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        workouts.clear();
        workouts.addAll(fetchedWorkouts);
        
        //DAY HEADERS
        for (String day : new String[]{"L", "M", "X", "J", "V", "S", "D"}) {
            add(new JLabel(day, SwingConstants.CENTER), "growx");
        }
        
        int startDay = (firstDayOfMonth.get(ChronoField.DAY_OF_WEEK) + 6) % 7;
        
        // DAYS OF THE PREVIOUS MONTH
        YearMonth prevMonth = yearMonth.minusMonths(1);
        int prevMonthDays = prevMonth.lengthOfMonth();
        for (int i = startDay - 1; i >= 0; i--) {
            LocalDate prevDate = prevMonth.atDay(prevMonthDays - i);
            JButton dayButton = createDisabledDayButton(prevDate.getDayOfMonth());
            add(dayButton);
        }
        
        // CURRENT MONTH DAYS
        for (int currentDay = 1; currentDay <= totalDays; currentDay++) {
            LocalDate date = LocalDate.of(year, month, currentDay);
            JButton dayButton = createDayButton(date);
            add(dayButton);
        }

        // DAYS OF THE NEXT MONTH
        int remainingCells = 42 - (startDay + totalDays);
        YearMonth nextMonth = yearMonth.plusMonths(1);
        for (int i = 1; i <= remainingCells; i++) {
            LocalDate nextDate = nextMonth.atDay(i);
            JButton dayButton = createDisabledDayButton(nextDate.getDayOfMonth());
            add(dayButton);
        }
        
        revalidate();
        repaint();
    }

    private JButton createNavigationButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(200, 200, 200));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(40, 30));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        return button;
    }
    
    private JButton createDisabledDayButton(int day) {
        JButton button = new JButton(String.valueOf(day));
        button.setEnabled(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.DARK_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return button;
    }
    
    private JButton createDayButton(LocalDate date) {
        JButton button = new JButton(String.valueOf(date.getDayOfMonth()));
        button.setFocusPainted(false);

        long count = workouts.stream()
            .filter(w -> w.getForDate().toLocalDate().equals(date))
            .count();
        
        if (count > 0) {
            button.setBackground(activeButtonColor);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setToolTipText("Entrenamientos: " + count);

            button.addActionListener(e -> {
                ArrayList<Workout> dayWorkouts = new ArrayList();
                
                try {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(date);
                    dayWorkouts = dataAccess.getWorkoutsByDay(sqlDate);
                } catch (SQLException ex) {
                    Logger.getLogger(WorkoutsCalendarPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

                fireWorkoutsEvent(dayWorkouts);
            });
        } else {
            button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            button.setBackground(new Color(255, 240, 201));
        }

        return button;
    }
    
    private void addSwipeListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //Set click to know the initial location
                initialClick = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (initialClick == null) return;

                int diffY = e.getY() - initialClick.y;
                if (Math.abs(diffY) > 50) {
                    if (diffY > 0) { //swipe down, so we go prev month
                        previousMonth();
                    } else { //swipe up, so we go next month
                        nextMonth();
                    }
                    initialClick = null; // Reset initial point after swipe
                }
            }
        });
    }

    private void nextMonth() {
        if (month == 12) {
            year++;
            month = 1;
        } else {
            month++;
        }
        renderCalendar(year, month);
    }

    private void previousMonth() {
        if (month == 1) {
            year--;
            month = 12;
        } else {
            month--;
        }
        renderCalendar(year, month);
    }

    public void addCalendarEventListener(CalendarEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeCalendarEventListener(CalendarEventListener listener) {
        listeners.remove(listener);
    }

    private void fireWorkoutsEvent(ArrayList<Workout> dayWorkouts) {
        CalendarEvent event = new CalendarEvent(this, dayWorkouts);
        for (CalendarEventListener listener : listeners) {
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
        if (year < 1970) throw new IllegalArgumentException("El año no puede ser menor que 1970.");
        if (year > 9000) throw new IllegalArgumentException("El año no puede ser mayor que 9000.");
        
        this.year = year;
        renderCalendar(year, month);
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
        if (month <= 0) throw new IllegalArgumentException("El mes no puede ser menor que 1.");
        if (month > 12) throw new IllegalArgumentException("El mes no puede ser mayor que 12.");
        
        this.month = month;
        renderCalendar(year, month);
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
        renderCalendar(year, month);
    }

    /**
     * @return the connectionString
     */
    public String getConnectionString() {
        return connectionString;
    }

    /**
     * @param connectionString the connectionString to set
     */
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
        dataAccess = new DataAccess(this.connectionString);
        renderCalendar(year, month);
    }
}
