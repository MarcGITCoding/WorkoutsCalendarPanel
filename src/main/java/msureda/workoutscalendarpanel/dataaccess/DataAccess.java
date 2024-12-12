package msureda.workoutscalendarpanel.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import msureda.workoutscalendarpanel.dto.User;
import msureda.workoutscalendarpanel.dto.Exercise;
import msureda.workoutscalendarpanel.dto.Workout;

/**
 * Clase para la comunicación con la DB
 * @author Marc Sureda
 */
public class DataAccess {

    private String connectionString;

    public DataAccess(String connectionString) {
        this.connectionString = connectionString;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }
    
    public ArrayList<Workout> getWorkoutsByMonth(int year, int month) throws SQLException {
        ArrayList<Workout> workouts = new ArrayList<>();
        String sql = "SELECT Workouts.Id, Workouts.ForDate, Workouts.UserId, Workouts.Comments"
                   + " FROM Workouts"
                   + " WHERE YEAR(Workouts.ForDate) = ? AND MONTH(Workouts.ForDate) = ?"
                   + " ORDER BY Workouts.ForDate";

        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {
            selectStatement.setInt(1, year);
            selectStatement.setInt(2, month);

            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                Workout workout = new Workout();
                workout.setId(resultSet.getInt("Id"));
                workout.setForDate(resultSet.getDate("ForDate"));
                workout.setUserId(resultSet.getInt("UserId"));
                workout.setComments(resultSet.getString("Comments"));

                workouts.add(workout);
            }
        }

        return workouts;
    }
    
    public ArrayList<Workout> getWorkoutsByDay(Date day) throws SQLException {
        ArrayList<Workout> workouts = new ArrayList<>();
        String sql = "SELECT Workouts.Id AS WorkoutId, Workouts.ForDate, Workouts.Comments, "
            + "Usuaris.Id AS UserId, Usuaris.Nom AS UserName, Usuaris.Email AS UserEmail, "
            + "Exercicis.Id AS ExerciseId, Exercicis.NomExercici AS ExerciseName, Exercicis.Descripcio AS ExerciseDescription, Exercicis.DemoFoto AS ExerciseDemoPhoto "
            + "FROM Workouts "
            + "JOIN Usuaris ON Workouts.UserId = Usuaris.Id "
            + "LEFT JOIN ExercicisWorkouts ON Workouts.Id = ExercicisWorkouts.IdWorkout "
            + "LEFT JOIN Exercicis ON ExercicisWorkouts.IdExercici = Exercicis.Id "
            + "WHERE CONVERT(DATE, Workouts.ForDate) = ? "
            + "ORDER BY Workouts.ForDate";

        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {
            selectStatement.setDate(1, new java.sql.Date(day.getTime()));

            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                int workoutId = resultSet.getInt("WorkoutId");
                Workout workout = workouts.stream()
                    .filter(w -> w.getId() == workoutId)
                    .findFirst()
                    .orElseGet(() -> {
                        try {
                            Workout newWorkout = new Workout();
                            newWorkout.setId(workoutId);
                            newWorkout.setForDate(resultSet.getDate("ForDate"));
                            newWorkout.setComments(resultSet.getString("Comments"));

                            User user = new User();
                            user.setId(resultSet.getInt("UserId"));
                            user.setName(resultSet.getString("UserName"));
                            user.setEmail(resultSet.getString("UserEmail"));
                            newWorkout.setUser(user);

                            newWorkout.setExercises(new ArrayList<>());
                            workouts.add(newWorkout);
                            return newWorkout;
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                int exerciseId = resultSet.getInt("ExerciseId");
                if (exerciseId > 0) {
                    Exercise exercise = new Exercise();
                    exercise.setId(exerciseId);
                    exercise.setName(resultSet.getString("ExerciseName"));
                    exercise.setDescription(resultSet.getString("ExerciseDescription"));
                    exercise.setDemoPhoto(resultSet.getString("ExerciseDemoPhoto"));

                    workout.getExercises().add(exercise);
                }
            }
        }

        return workouts;
    }
}