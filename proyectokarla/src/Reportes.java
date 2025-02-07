import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class Reportes {
        public JPanel reportesPanel;
        private JTextField textFieldCliente;
        private JTextField textFieldMascota;
        private JTextField textFieldMedico;
        private JTextField textFieldFecha;
        private JTextField textFieldCita;
        private JTextField textFieldEspecificacion;
        private JButton imprimirButton;
        private JButton guardarButton;
        private JButton regresarButton;

        public Reportes() {
            guardarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guardarReporte();
                }
            });

            imprimirButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    imprimirReporte();
                }
            });

            regresarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    regresarInterfazMedico();
                }
            });
        }

        private void guardarReporte() {
            String cliente = textFieldCliente.getText().trim();
            String mascota = textFieldMascota.getText().trim();
            String medico = textFieldMedico.getText().trim();
            String fecha = textFieldFecha.getText().trim();
            String cita = textFieldCita.getText().trim();
            String especificacion = textFieldEspecificacion.getText().trim();

            // Validación: Todos los campos deben estar llenos
            if (cliente.isEmpty() || mascota.isEmpty() || medico.isEmpty() || fecha.isEmpty() || cita.isEmpty() || especificacion.isEmpty()) {
                JOptionPane.showMessageDialog(reportesPanel, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Conectar a la base de datos y guardar el reporte
            try {
                MongoDatabase database = Conexion.getDatabase();
                MongoCollection<Document> collection = database.getCollection("reportes");

                Document reporte = new Document("cliente", cliente)
                        .append("mascota", mascota)
                        .append("medico", medico)
                        .append("fecha", fecha)
                        .append("cita", cita)
                        .append("especificacion", especificacion);

                collection.insertOne(reporte);
                JOptionPane.showMessageDialog(reportesPanel, "Reporte guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Limpiar los campos después de guardar
                limpiarCampos();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(reportesPanel, "Error al guardar el reporte. Inténtalo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void imprimirReporte() {
            String cliente = textFieldCliente.getText().trim();
            String mascota = textFieldMascota.getText().trim();
            String medico = textFieldMedico.getText().trim();
            String fecha = textFieldFecha.getText().trim();
            String cita = textFieldCita.getText().trim();
            String especificacion = textFieldEspecificacion.getText().trim();

            // Validación: No imprimir si algún campo está vacío
            if (cliente.isEmpty() || mascota.isEmpty() || medico.isEmpty() || fecha.isEmpty() || cita.isEmpty() || especificacion.isEmpty()) {
                JOptionPane.showMessageDialog(reportesPanel, "Todos los campos deben estar llenos para imprimir.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Simulación de impresión (puedes reemplazar esto con una funcionalidad real si necesitas imprimir en papel)
            System.out.println("------ REPORTE ------");
            System.out.println("Cliente: " + cliente);
            System.out.println("Mascota: " + mascota);
            System.out.println("Médico: " + medico);
            System.out.println("Fecha: " + fecha);
            System.out.println("Cita: " + cita);
            System.out.println("Especificación: " + especificacion);
            System.out.println("---------------------");

            JOptionPane.showMessageDialog(reportesPanel, "Reporte enviado a impresión (simulado).", "Información", JOptionPane.INFORMATION_MESSAGE);
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
            SwingUtilities.getWindowAncestor(reportesPanel).dispose();
        }

        private void limpiarCampos() {
            textFieldCliente.setText("");
            textFieldMascota.setText("");
            textFieldMedico.setText("");
            textFieldFecha.setText("");
            textFieldCita.setText("");
            textFieldEspecificacion.setText("");
        }

        public JPanel getReportesPanel() {
            return reportesPanel;
        }
    }
