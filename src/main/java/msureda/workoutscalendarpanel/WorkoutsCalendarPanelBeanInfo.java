package msureda.workoutscalendarpanel;

import java.beans.*;
import javax.swing.JPanel;

/**
 * Archivo bean info para el componente
 * @author Marc Sureda
 */
public class WorkoutsCalendarPanelBeanInfo extends SimpleBeanInfo {
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor descriptor = new BeanDescriptor(WorkoutsCalendarPanel.class);

        return descriptor;
    }
    
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] panelProperties = Introspector.getBeanInfo(JPanel.class).getPropertyDescriptors();
            
            PropertyDescriptor connectionString = new PropertyDescriptor("connectionString", WorkoutsCalendarPanel.class);
            connectionString.setDisplayName("Connection String");
            connectionString.setShortDescription("Cadena de textos de la conexión SQL.");
            
            PropertyDescriptor instructorId = new PropertyDescriptor("instructorId", WorkoutsCalendarPanel.class);
            instructorId.setDisplayName("ID de Instructor");
            instructorId.setShortDescription("ID del Instructor del que sacar los entrenamientos de los alumnos.");
            
            PropertyDescriptor year = new PropertyDescriptor("year", WorkoutsCalendarPanel.class);
            year.setDisplayName("Año");
            year.setShortDescription("El año inicial que muestra el calendario.");
            
            PropertyDescriptor month = new PropertyDescriptor("month", WorkoutsCalendarPanel.class);
            month.setDisplayName("Mes");
            month.setShortDescription("El mes inicial que muestra el calendario.");
            
            PropertyDescriptor activeButtonColor = new PropertyDescriptor("activeButtonColor", WorkoutsCalendarPanel.class);
            activeButtonColor.setDisplayName("Color del botón activo");
            activeButtonColor.setShortDescription("El color de fondo de los botones de días con entrenamientos.");
            
            PropertyDescriptor[] allProperties = new PropertyDescriptor[panelProperties.length + 4];
            System.arraycopy(panelProperties, 0, allProperties, 0, panelProperties.length);
            
            allProperties[panelProperties.length] = connectionString;
            allProperties[panelProperties.length + 1] = instructorId;
            allProperties[panelProperties.length + 2] = year;
            allProperties[panelProperties.length + 3] = month;
            allProperties[panelProperties.length + 4] = activeButtonColor;
            
            return allProperties;
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

