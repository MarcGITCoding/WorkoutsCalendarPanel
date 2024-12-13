package msureda.workoutscalendarpanel.components;

import javax.swing.*;
import java.awt.*;

public class CustomMonthYearLabel extends JLabel {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.drawString(getText(), getInsets().left + 2, getInsets().top + g.getFontMetrics().getAscent() + 2);

        g2d.setColor(getForeground());
        g2d.drawString(getText(), getInsets().left, getInsets().top + g.getFontMetrics().getAscent());

        g2d.dispose();
    }
}