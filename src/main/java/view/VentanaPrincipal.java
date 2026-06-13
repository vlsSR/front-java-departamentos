package view;

import model.Departamento;
import service.DepartamentoApiClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {
    private JTable tablaDepartamentos;
    private DefaultTableModel modeloTabla;
    private JButton btnRefrescar, btnEscribir, btnEliminar, btnActualizar;
    private JLabel lblId, lblNombre, lblLocalidad;
    private JTextField txId, txNombre, txLocalidad;
    private DepartamentoApiClient apiClient;

    public VentanaPrincipal() {
        apiClient = new DepartamentoApiClient();

        setTitle("Consumidor API REST - Control de Empleados");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columnas = {"Codigo(ID)", "Nombre Completo", "Localidad"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaDepartamentos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaDepartamentos);
        add(scrollPane, BorderLayout.CENTER);

        btnRefrescar = new JButton("Cargar listado");
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnRefrescar);

        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        lblId = new JLabel("Id: ");
        formulario.add(lblId);
        txId = new JTextField();
        formulario.add(txId);
        lblNombre = new JLabel("Nombre: ");
        formulario.add(lblNombre);
        txNombre = new JTextField();
        formulario.add(txNombre);
        lblLocalidad = new JLabel("Localidad: ");
        formulario.add(lblLocalidad);
        txLocalidad = new JTextField();
        formulario.add(txLocalidad);
        add(formulario, BorderLayout.NORTH);

        btnEscribir = new JButton("Escribir");
        panelBoton.add(btnEscribir);

        btnEliminar = new JButton("Eliminar");
        panelBoton.add(btnEliminar);

        btnActualizar = new JButton("Actualizar");
        panelBoton.add(btnActualizar);

        add(panelBoton, BorderLayout.SOUTH);
        cargarDatosDesdeApi();
        btnEscribir.addActionListener(e -> escribirDatos());
        btnRefrescar.addActionListener(e -> cargarDatosDesdeApi());
        btnEliminar.addActionListener(e -> eliminarDatos());
        btnActualizar.addActionListener(e -> actulizar());
    }

    public void actulizar() {
        int row = tablaDepartamentos.getSelectedRow();
        String id = tablaDepartamentos.getValueAt(row, 0).toString();
        System.out.println(id);

        Departamento departamento = new Departamento(
                txId.getText(),
                txNombre.getText(),
                txLocalidad.getText()
        );

        try {
            apiClient.actualizarDepartamento(id, departamento);
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo establecer la comunicacion con la API REST \n Detalle del error "+e.getMessage(),
                    "Fallo critico de conexion", JOptionPane.ERROR_MESSAGE);
        }
        cargarDatosDesdeApi();
    }

    private void eliminarDatos() {
        int row = tablaDepartamentos.getSelectedRow();
        String id = tablaDepartamentos.getValueAt(row, 0).toString();
        System.out.println(id);
        try {
            apiClient.eliminarDepartamento(id);
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo establecer la comunicacion con la API REST \n Detalle del error "+e.getMessage(),
                    "Fallo critico de conexion", JOptionPane.ERROR_MESSAGE);
        }
        cargarDatosDesdeApi();
    }

    private void escribirDatos() {
        Departamento departamento = new Departamento(
                txId.getText(),
                txNombre.getText(),
                txLocalidad.getText()
        );

        try {
            apiClient.insertarDepartamento(departamento);
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo establecer la comunicacion con la API REST \n Detalle del error "+e.getMessage(),
                    "Fallo critico de conexion", JOptionPane.ERROR_MESSAGE);
        }
        cargarDatosDesdeApi();
    }

    private void cargarDatosDesdeApi() {
        try {
            Departamento[] departamentos = apiClient.obtenerDepartamentos();

            modeloTabla.setRowCount(0);

            for (Departamento depa:departamentos) {
                modeloTabla.addRow(new Object[]{
                        depa.getCodigo(),
                        depa.getNombre(),
                        depa.getLocalidad()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo establecer la comunicacion con la API REST \n Detalle del error "+e.getMessage(),
                    "Fallo Critico de conexion", JOptionPane.ERROR_MESSAGE);
        }
    }
}
