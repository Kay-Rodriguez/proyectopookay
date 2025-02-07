
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazCliente {
    public JPanel ClientePanel;
    private JTextField textFieldMascotas;
    private JTextField textFieldDueño;
    private JTextField textFieldTelefono;
    private JTextField textFieldCorreo;
    private JTextField textFieldAsunto;
    private JButton Enviar;
    private JLabel Listo;

    public JPanel getCLientePanel() { return ClientePanel;}

    public InterfazCliente() {
        Enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarDatos();
            }
        });
    }

    private void enviarDatos() {
        String mascota = textFieldMascotas.getText().trim();
        String dueño = textFieldDueño.getText().trim();
        String telefono = textFieldTelefono.getText().trim();
        String correo = textFieldCorreo.getText().trim();
        String asunto = textFieldAsunto.getText().trim();

        // Validación: Todos los campos deben estar llenos
        if (mascota.isEmpty() || dueño.isEmpty() || telefono.isEmpty() || correo.isEmpty() || asunto.isEmpty()) {
            JOptionPane.showMessageDialog(ClientePanel, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación: Teléfono solo números
        if (!telefono.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(ClientePanel, "El teléfono debe contener 10 dígitos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // Conectar a la base de datos y guardar la información
        try {
            MongoDatabase database = Conexion.getDatabase();
            MongoCollection<Document> collection = database.getCollection("gestionclientes");

            Document cliente = new Document("mascota", mascota)
                    .append("dueño", dueño)
                    .append("telefono", telefono)
                    .append("correo", correo)
                    .append("asunto", asunto);

            collection.insertOne(cliente);
            JOptionPane.showMessageDialog(ClientePanel, "Datos enviados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Mostrar mensaje en la etiqueta "Listo"
            Listo.setText("Gracias por contactarnos, confirmaremos su cita en 24 horas.");

            // Limpiar los campos después de enviar
            limpiarCampos();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ClientePanel, "Error al enviar los datos. Inténtelo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        textFieldMascotas.setText("");
        textFieldDueño.setText("");
        textFieldTelefono.setText("");
        textFieldCorreo.setText("");
        textFieldAsunto.setText("");
    }

    public JPanel getClientePanel() {
        return ClientePanel;
    }
}
