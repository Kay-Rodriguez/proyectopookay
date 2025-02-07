import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registro {
    private JTextField textFieldUsuario;
    private JTextField textFieldCorreo;
    private JTextField textFieldTelefono;
    private JPasswordField passwordField;
    private JComboBox<String> comboBoxRol;
    private JButton guardarRegistroButton;
    private JPanel registroPanel;

    public registro() {
        comboBoxRol.addItem("...");
        comboBoxRol.addItem("Administrador");
        comboBoxRol.addItem("cliente");
        comboBoxRol.addItem("medico");

        guardarRegistroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = textFieldUsuario.getText().trim();
                String correo = textFieldCorreo.getText().trim();
                String telefono = textFieldTelefono.getText().trim();
                String contraseña = new String(passwordField.getPassword()).trim();
                String rol = (String) comboBoxRol.getSelectedItem();

                if (usuario.isEmpty() || correo.isEmpty() || telefono.isEmpty() || contraseña.isEmpty() || rol.equals("...")) {
                    JOptionPane.showMessageDialog(registroPanel, "Por favor, completa todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!telefono.matches("\\d+")) {
                    JOptionPane.showMessageDialog(registroPanel, "Por favor, ingresa un número válido para el teléfono.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Convertir teléfono a un número solo si es necesario
                int telefonoInt = 000000000;
                try {
                    telefonoInt = Integer.parseInt(telefono);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(registroPanel, "Por favor, ingresa un número válido para el teléfono.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                if (registerUser(usuario, correo, telefonoInt, contraseña, rol)) {
                    JOptionPane.showMessageDialog(registroPanel, "Registro exitoso. ¡Ahora puedes iniciar sesión!");
                } else {
                    JOptionPane.showMessageDialog(registroPanel, "Error: El usuario ya está registrado o ocurrió un problema al registrar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public JPanel getregistroPanel() {
        return registroPanel;
    }

    private boolean registerUser(String usuario, String correo, int telefono, String contraseña, String rol) {
        try {
            // Conectar a la base de datos
            MongoDatabase database = Conexion.getDatabase();
            MongoCollection<Document> collection = database.getCollection("registros");
            // Crear la consulta
            Document query = new Document("usuario", usuario).append("contraseña", contraseña).append("correo",correo).append("telefono", telefono).append("rol", rol);
            // Verificar si existe un documento que cumpla con la consulta
            Document result = collection.find(query).first();

            // Si el resultado no es null, las credenciales son válidas
            if (result!= null) {
                return false; // Usuario ya registrado
            }

            Document newUser = new Document("usuario", usuario)
                    .append("correo", correo)
                    .append("telefono", telefono)
                    .append("contraseña", contraseña)
                    .append("rol", rol);

            collection.insertOne(newUser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
