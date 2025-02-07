import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    public JPanel mainPanel;
    private JComboBox<String> ModoBox1;
    private JTextField textField1;
    private JButton ingresarButton;
    private JButton registrarButton;
    private JPasswordField passwordField;
    private MongoCollection<Document> collection;

    public Login() {
        ModoBox1.addItem("...");
        ModoBox1.addItem("Administrador");
        ModoBox1.addItem("cliente");
        ModoBox1.addItem("medico");

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField1.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String rol = (String) ModoBox1.getSelectedItem();

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "Por favor, ingrese su usuario.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "Por favor, ingrese su contraseña.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (authenticateUser(email, password, rol)) {
                    JOptionPane.showMessageDialog(mainPanel, "Login exitoso. ¡Bienvenido " + rol + "!");
                    abrirVentanaPorRol(rol);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Usuario no registrado o credenciales incorrectas. ¿Desea registrarse?", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame1 = new JFrame("Registro de Usuario");
                frame1.setContentPane(new registro().getregistroPanel());
                frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame1.setSize(800, 700);
                frame1.setVisible(true);
            }
        });
    }

    private boolean authenticateUser(String usuario, String contraseña, String rol) {
        try {
            MongoDatabase database = Conexion.getDatabase();
            MongoCollection<Document> collection = database.getCollection("registros");

            Document query = new Document("usuario", usuario).append("contraseña", contraseña).append("rol", rol);
            Document result = collection.find(query).first();
            return result != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void abrirVentanaPorRol(String rol) {
        JFrame nuevaVentana = new JFrame();
        switch (rol) {
            case "Administrador":
                nuevaVentana.setTitle("Interfaz Administrador");
                nuevaVentana.setContentPane(new InterfazAdmin().getAdminPanel());
                break;
            case "medico":
                nuevaVentana.setTitle("Interfaz Medico");
                nuevaVentana.setContentPane(new InterfazMedico().getMedicoPanel());
                break;
            case "cliente":
                nuevaVentana.setTitle("Interfaz Cliente");
                nuevaVentana.setContentPane(new InterfazCliente().getCLientePanel());
                break;
            default:
                JOptionPane.showMessageDialog(mainPanel, "Rol no reconocido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        nuevaVentana.setSize(800, 600);
        nuevaVentana.setVisible(true);
    }
}
