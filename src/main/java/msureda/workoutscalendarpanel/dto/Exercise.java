package msureda.workoutscalendarpanel.dto;

/**
 * Clase DTO para la tabla Exercisis
 * @author Marc Sureda
 */
public class Exercise {
    private int Id;
    private String Name;
    private String Description;
    private String DemoPhoto;

    @Override
    public String toString() {
        return Name + ": " + Description;
    }

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
     * @return the Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * @return the DemoPhoto
     */
    public String getDemoPhoto() {
        return DemoPhoto;
    }

    /**
     * @param DemoPhoto the DemoPhoto to set
     */
    public void setDemoPhoto(String DemoPhoto) {
        this.DemoPhoto = DemoPhoto;
    }
}
