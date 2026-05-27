import GUI.View;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new View("Project Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}