

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class RegistroMascota {
        public JPanel RegistroMPanel;
        private JTextField textFieldDueño;
        private JTextField textFieldMascota;
        private JTextField textFieldTipo;
        private JTextField textFieldEdad;
        private JTextField textFieldPeso;
        private JButton guardarButton;
        private JButton regresarButton;

        public RegistroMascota() {
            guardarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registrarMascota();
                }
            });

            regresarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    regresarInterfazMedico();
                }
            });
        }

        private void registrarMascota() {
            String dueño = textFieldDueño.getText().trim();
            String mascota = textFieldMascota.getText().trim();
            String tipo = textFieldTipo.getText().trim();
            String edadTexto = textFieldEdad.getText().trim();
            String pesoTexto = textFieldPeso.getText().trim();

            // Validación: campos vacíos
            if (dueño.isEmpty() || mascota.isEmpty() || tipo.isEmpty() || edadTexto.isEmpty() || pesoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(RegistroMPanel, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int edad;
            double peso;

            // Validación: Edad como número entero
            try {
                edad = Integer.parseInt(edadTexto);
                if (edad < 0) {
                    JOptionPane.showMessageDialog(RegistroMPanel, "La edad debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(RegistroMPanel, "Edad inválida. Ingresa un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validación: Peso como número decimal
            try {
                peso = Double.parseDouble(pesoTexto);
                if (peso <= 0) {
                    JOptionPane.showMessageDialog(RegistroMPanel, "El peso debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(RegistroMPanel, "Peso inválido. Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Conectar a la base de datos y guardar los datos
            try {
                MongoDatabase database = Conexion.getDatabase();
                MongoCollection<Document> collection = database.getCollection("mascotas");

                Document mascotaDoc = new Document("dueño", dueño)
                        .append("mascota", mascota)
                        .append("tipo", tipo)
                        .append("edad", edad)
                        .append("peso", peso);

                collection.insertOne(mascotaDoc);
                JOptionPane.showMessageDialog(RegistroMPanel, "Registro de mascota exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Limpiar los campos después del registro exitoso
                textFieldDueño.setText("");
                textFieldMascota.setText("");
                textFieldTipo.setText("");
                textFieldEdad.setText("");
                textFieldPeso.setText("");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(RegistroMPanel, "Error al registrar la mascota. Inténtalo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void regresarInterfazMedico() {
            JFrame frame = new JFrame("Interfaz Médico");
            frame.setContentPane(new InterfazMedico().getMedicoPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setPreferredSize(new Dimension(800, 600));
            frame.pack();
            frame.setVisible(true);

            // Cerrar la ventana actual
            SwingUtilities.getWindowAncestor(RegistroMPanel).dispose();
        }

        public JPanel getRegistroMPanel() {
            return RegistroMPanel;
        }
    }
