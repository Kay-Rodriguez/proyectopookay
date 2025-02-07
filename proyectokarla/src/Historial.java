import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Historial {
    public JPanel historialPanel;
    private JTable tableGestionClientes;
    private JTable tableReportes;
    private JButton regresarButton;
    private JScrollPane scrollPaneClientes;
    private JScrollPane scrollPaneReportes;

    public Historial() {
        historialPanel = new JPanel();
        historialPanel.setLayout(new BorderLayout());

        // Crear tablas
        tableGestionClientes = new JTable();
        tableReportes = new JTable();

        // Cargar datos desde MongoDB
        cargarDatos("gestionclientes", tableGestionClientes);
        cargarDatos("reportes", tableReportes);

        // Panel para los datos de gestión de clientes
        JPanel panelClientes = new JPanel(new BorderLayout());
        panelClientes.setBorder(BorderFactory.createTitledBorder("Gestión de Citas"));
        scrollPaneClientes = new JScrollPane(tableGestionClientes);
        panelClientes.add(scrollPaneClientes, BorderLayout.CENTER);

        // Panel para los reportes
        JPanel panelReportes = new JPanel(new BorderLayout());
        panelReportes.setBorder(BorderFactory.createTitledBorder("Reportes Generados"));
        scrollPaneReportes = new JScrollPane(tableReportes);
        panelReportes.add(scrollPaneReportes, BorderLayout.CENTER);

        // Botón para regresar
        regresarButton = new JButton("Regresar");
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInterfazMedico();
            }
        });

        // Agregar componentes a la interfaz
        historialPanel.add(panelClientes, BorderLayout.NORTH);
        historialPanel.add(panelReportes, BorderLayout.CENTER);
        historialPanel.add(regresarButton, BorderLayout.SOUTH);
    }

    private void cargarDatos(String coleccion, JTable tabla) {
        try {
            MongoDatabase database = Conexion.getDatabase();
            MongoCollection<Document> collection = database.getCollection(coleccion);
            FindIterable<Document> documentos = collection.find();

            DefaultTableModel modelo = new DefaultTableModel();

            // Obtener los nombres de las columnas dinámicamente
            boolean columnasDefinidas = false;
            for (Document doc : documentos) {
                if (!columnasDefinidas) {
                    for (String key : doc.keySet()) {
                        modelo.addColumn(key);
                    }
                    columnasDefinidas = true;
                }

                Object[] fila = new Object[doc.size()];
                int i = 0;
                for (String key : doc.keySet()) {
                    fila[i++] = doc.get(key);
                }
                modelo.addRow(fila);
            }

            tabla.setModel(modelo);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(historialPanel, "Error al cargar datos de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirInterfazMedico() {
        JFrame frame = new JFrame("Interfaz Médico");
        frame.setContentPane(new InterfazMedico().getMedicoPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Cerrar la ventana actual
        SwingUtilities.getWindowAncestor(historialPanel).dispose();
    }

}