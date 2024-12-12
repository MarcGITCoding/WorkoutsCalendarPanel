# WorkoutsCalendarPanel

## Overview

The `WorkoutsCalendarPanel` is a Java Swing component designed to display a calendar with workout events. This panel fetches workout data from a SQL Server database and provides an interactive interface for users to view and interact with their workouts. It also includes event-driven functionality to notify listeners about selected workouts.

## Features

- **Dynamic Calendar Rendering**: Displays a calendar for the current month, allowing navigation between months.
- **Workout Highlighting**: Highlights days with scheduled workouts.
- **Interactive Day Buttons**: Displays tooltips with workout counts and allows users to view detailed workout information.
- **Swipe Navigation**: Navigate between months using mouse drag gestures.
- **Customizable Appearance**: Set the color for days with workouts.
- **Event Notification**: Notifies registered listeners when workouts are selected.

## Classes

### Main Classes

- **WorkoutsCalendarPanel**: The main calendar panel. Handles calendar rendering, data fetching, and user interaction.
- **DataAccess**: Manages database operations, such as fetching workout data by month or day.
- **CalendarEvent**: Represents an event triggered when workouts are selected.
- **CalendarEventListener**: Interface for components to listen to workout selection events.

### Data Transfer Objects (DTOs)

- **Workout**: Represents a workout with details like date, user, comments, and exercises.
- **User**: Represents a user associated with a workout.
- **Exercise**: Represents an exercise associated with a workout.

## Setup

1. **Add the Component**: Include the `WorkoutsCalendarPanel` component in your Java Swing application (you can find it [here](https://github.com/MarcGITCoding/BlobPollingPanel/releases)).
2. **Configure the Database**:
   - Ensure you have a SQL Server database with a `Workouts` table (and related tables for users and exercises).
   - Set the connection string using `setConnectionString(String connectionString)`.
3. **Register Event Listeners**:
   - Implement the `CalendarEventListener` interface to handle workout selection events.
   - Add the listener using `addCalendarEventListener(CalendarEventListener listener)`.

## Usage Example

```java
WorkoutsCalendarPanel calendarPanel = new WorkoutsCalendarPanel();

// Set the database connection string
calendarPanel.setConnectionString("jdbc:sqlserver://localhost;database=simulapdb;user=sa;password=1234;encrypt=false;");

// Set ActiveButtonColor, Month and Year (optional)
calendarPanel.setMonth(10);
calendarPanel.setYear(2023);

// Customize active button color (optional)
calendarPanel.setActiveButtonColor(new Color(173, 216, 230));

// Add a listener to handle workout selection
calendarPanel.addCalendarEventListener(event -> {
    ArrayList<Workout> selectedWorkouts = event.getWorkouts();
    selectedWorkouts.forEach(workout -> {
        System.out.println("Workout on " + workout.getForDate() + ": " + workout.getComments());
    });
});

// Add the panel to a JFrame
JFrame frame = new JFrame("Workout Calendar");
frame.add(calendarPanel);
frame.setSize(800, 600);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setVisible(true);
```

## Dependencies
- Java 8+
- MigLayout for layout management
- SQL Server JDBC Driver