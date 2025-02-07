import com.mongodb.client.*;
import org.bson.Document;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfazAdmin {
    public JPanel AdminPanel;
    private JTable tableMascotas;
    private JTable tableHistorial;
    private JTable tableUsuarios;
    private JButton btnIngresar;
    private JButton btnLeer;
    private JButton btnModificar;
    private JButton btnEliminar;

    public InterfazAdmin() {
        AdminPanel = new JPanel();
        //AdminPanel.setLayout(new BorderLayout());

        // Crear tablas
        tableMascotas = new JTable();
        tableHistorial = new JTable();
        tableUsuarios = new JTable();

        // Paneles con scroll para mostrar los datos
        JPanel panelMascotas = crearPanel("Registro de Mascotas", tableMascotas);
        JPanel panelHistorial = crearPanel("Historial Médico", tableHistorial);
        JPanel panelUsuarios = crearPanel("Usuarios Registrados", tableUsuarios);

        // Botones CRUD
        JPanel panelBotones = new JPanel();
        btnIngresar = new JButton("Ingresar");
        btnLeer = new JButton("Leer");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        // Acciones de botones
        btnIngresar.addActionListener(e -> ingresarDatos());
        btnLeer.addActionListener(e -> leerDatos());
        btnModificar.addActionListener(e -> modificarDatos());
        btnEliminar.addActionListener(e -> eliminarDatos());

        panelBotones.add(btnIngresar);
        panelBotones.add(btnLeer);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        // Agregar componentes al panel principal
        JPanel panelCentral = new JPanel(new GridLayout(3, 1));
        panelCentral.add(panelMascotas);
        panelCentral.add(panelHistorial);
        panelCentral.add(panelUsuarios);

        AdminPanel.add(panelCentral, BorderLayout.CENTER);
        AdminPanel.add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanel(String titulo, JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void leerDatos() {
        cargarDatos("mascotas", tableMascotas);
        cargarDatos("gestionclientes", tableHistorial);
        cargarDatos("reportes", tableHistorial);
        cargarDatos("registros", tableUsuarios);
    }

    private void cargarDatos(String coleccion, JTable tabla) {
        try {
            MongoDatabase database = Conexion.getDatabase();
            MongoCollection<Document> collection = database.getCollection(coleccion);
            FindIterable<Document> documentos = collection.find();

            DefaultTableModel modelo = new DefaultTableModel();
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
            JOptionPane.showMessageDialog(AdminPanel, "Error al cargar datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ingresarDatos() {
        String coleccion = JOptionPane.showInputDialog("Ingrese la colección (mascotas, gestionclientes, registros):");
        if (coleccion != null && !coleccion.isEmpty()) {
            String datos = JOptionPane.showInputDialog("Ingrese los datos en formato JSON:");
            try {
                MongoDatabase database = Conexion.getDatabase();
                MongoCollection<Document> collection = database.getCollection(coleccion);
                Document doc = Document.parse(datos);
                collection.insertOne(doc);
                JOptionPane.showMessageDialog(AdminPanel, "Datos ingresados correctamente.");
                leerDatos(); // Refrescar datos
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(AdminPanel, "Error al ingresar datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modificarDatos() {
        String coleccion = JOptionPane.showInputDialog("Ingrese la colección a modificar:");
        String id = JOptionPane.showInputDialog("Ingrese el ID del documento a modificar:");
        String nuevosDatos = JOptionPane.showInputDialog("Ingrese los nuevos datos en formato JSON:");

        try {
            MongoDatabase database = Conexion.getDatabase();
            MongoCollection<Document> collection = database.getCollection(coleccion);
            Document filtro = new Document("_id", id);
            Document nuevosValores = Document.parse(nuevosDatos);
            collection.updateOne(filtro, new Document("$set", nuevosValores));
            JOptionPane.showMessageDialog(AdminPanel, "Datos modificados correctamente.");
            leerDatos(); // Refrescar datos
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(AdminPanel, "Error al modificar datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarDatos() {
        String coleccion = JOptionPane.showInputDialog("Ingrese la colección:");
        String id = JOptionPane.showInputDialog("Ingrese el ID del documento a eliminar:");

        try {
            MongoDatabase database = Conexion.getDatabase();
            MongoCollection<Document> collection = database.getCollection(coleccion);
            Document filtro = new Document("_id", id);
            collection.deleteOne(filtro);
            JOptionPane.showMessageDialog(AdminPanel, "Datos eliminados correctamente.");
            leerDatos(); // Refrescar datos
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(AdminPanel, "Error al eliminar datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getAdminPanel() {
        return AdminPanel;
    }
}
