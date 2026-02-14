import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AppointmentApp extends JFrame {
    private Map<Integer, String> patients = new HashMap<>();
    private Map<Integer, String> doctors = new HashMap<>();
    private java.util.List<Appointment> appointments = new ArrayList<>();

    private JTextField tfPatient = new JTextField(10);
    private JButton btnAddPatient = new JButton("Add Patient");
    private JTextField tfDoctor = new JTextField(10);
    private JButton btnAddDoctor = new JButton("Add Doctor");

    private JComboBox<String> cbPatient = new JComboBox<>();
    private JComboBox<String> cbDoctor = new JComboBox<>();
    private JTextField tfDateTime = new JTextField(12);
    private JButton btnBook = new JButton("Book Appointment");

    private DefaultTableModel model = new DefaultTableModel(
        new String[]{"Patient", "Doctor", "DateTime"}, 0);
    private JTable table = new JTable(model);

    public AppointmentApp() {
        super("Appointment Scheduler");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8,8));
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new GridLayout(2, 4, 5,5));
        top.setBorder(BorderFactory.createTitledBorder("Add Patients / Doctors"));
        top.add(new JLabel("Patient Name:")); top.add(tfPatient); top.add(btnAddPatient);
        top.add(new JLabel("Doctor Name:")); top.add(tfDoctor); top.add(btnAddDoctor);
        add(top, BorderLayout.NORTH);

        JPanel middle = new JPanel(new GridLayout(2, 2, 5,5));
        middle.setBorder(BorderFactory.createTitledBorder("Schedule Appointment"));
        middle.add(new JLabel("Select Patient:")); middle.add(cbPatient);
        middle.add(new JLabel("Select Doctor:")); middle.add(cbDoctor);
        add(middle, BorderLayout.CENTER);

        JPanel aft = new JPanel();
        tfDateTime.setText("yyyy-MM-dd HH:mm");
        aft.add(new JLabel("DateTime:")); aft.add(tfDateTime);
        aft.add(btnBook);
        add(aft, BorderLayout.SOUTH);

        add(new JScrollPane(table), BorderLayout.EAST);

        btnAddPatient.addActionListener(e -> addEntity(patients, cbPatient, tfPatient, "Patient"));
        btnAddDoctor.addActionListener(e -> addEntity(doctors, cbDoctor, tfDoctor, "Doctor"));

        btnBook.addActionListener(e -> bookAppointment());
    }

    private void addEntity(Map<Integer, String> map, JComboBox<String> combo, JTextField field, String type) {
        String name = field.getText().trim();
        if (name.isEmpty()) return;
        int id = map.size() + 1;
        map.put(id, name);
        combo.addItem(id + ": " + name);
        JOptionPane.showMessageDialog(this, type + " added with ID " + id);
        field.setText("");
    }

    private void bookAppointment() {
        if (cbPatient.getItemCount() == 0 || cbDoctor.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Register patient and doctor first.");
            return;
        }
        try {
            int pid = Integer.parseInt(cbPatient.getSelectedItem().toString().split(":")[0]);
            int did = Integer.parseInt(cbDoctor.getSelectedItem().toString().split(":")[0]);
            String dt = tfDateTime.getText().trim();
            LocalDateTime.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            appointments.add(new Appointment(pid, did, dt));
            model.addRow(new Object[]{patients.get(pid), doctors.get(did), dt});
            JOptionPane.showMessageDialog(this, "Appointment booked.");
            tfDateTime.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Use yyyy-MM-dd HH:mm");
        }
    }

    private static class Appointment {
        int patientId, doctorId;
        String datetime;
        Appointment(int p, int d, String dt) { patientId = p; doctorId = d; datetime = dt; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentApp().setVisible(true));
    }
}
