import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazMedico {
    public JPanel MedicoPanel;
    private JButton REGISTROMASCOTASButton;
    private JButton GESTIONDEREPORTESButton;
    private JButton HISTORIALMEDICOButton;

    public JPanel getMedicoPanel() {
        return MedicoPanel;
    }

    public InterfazMedico() {
        REGISTROMASCOTASButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistroMascota medico = new RegistroMascota();
                JFrame frame = new JFrame("Registrar mascotas");
                frame.setContentPane(new RegistroMascota().RegistroMPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800,700);
                frame.setPreferredSize(new Dimension(800, 700));
                frame.pack();
                frame.setVisible(true);
            }
        });

        GESTIONDEREPORTESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reportes medico = new Reportes();
                JFrame frame = new JFrame("gestionar Reportes");
                frame.setContentPane(new Reportes ().reportesPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800,700);
                frame.setPreferredSize(new Dimension(800, 700));
                frame.pack();
                frame.setVisible(true);

            }
        });
        HISTORIALMEDICOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Historial medico = new Historial();
                JFrame frame = new JFrame("Histrorial Medico");
                frame.setContentPane(new Historial().historialPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800,700);
                frame.setPreferredSize(new Dimension(800, 700));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

}

