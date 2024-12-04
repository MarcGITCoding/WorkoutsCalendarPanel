package msureda.workoutscalendarpanel.dto;

import java.sql.Date;

/**
 * Clase DTO para la tabla Workouts
 * @author Marc Sureda
 */
public class Workout {
    private int Id;
    private Date ForDate;
    private int UserId;
    private String Comments;
    
    /**
     * @return the Id
     */
    public int getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(int Id) {
        this.Id = Id;
    }

    /**
     * @return the ForDate
     */
    public Date getForDate() {
        return ForDate;
    }

    /**
     * @param ForDate the ForDate to set
     */
    public void setForDate(Date ForDate) {
        this.ForDate = ForDate;
    }

    /**
     * @return the UserId
     */
    public int getUserId() {
        return UserId;
    }

    /**
     * @param UserId the UserId to set
     */
    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    /**
     * @return the Comments
     */
    public String getComments() {
        return Comments;
    }

    /**
     * @param Comments the Comments to set
     */
    public void setComments(String Comments) {
        this.Comments = Comments;
    }
}
