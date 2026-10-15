import java.util.ArrayList;
import java.util.Scanner;

public class HospitalSystem {

    // ---------- Data models ----------
    static class Patient {
        int id;
        String name;
        int age;
        String gender;
        String phone;
        String diagnosis;

        Patient(int id, String name, int age, String gender, String phone, String diagnosis) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
            this.diagnosis = diagnosis;
        }

        @Override
        public String toString() {
            return String.format(
                "ID: %-4d | Name: %-20s | Age: %-3d | Gender: %-6s | Phone: %-12s | Diagnosis: %s",
                id, name, age, gender, phone, diagnosis
            );
        }
    }

    static class Doctor {
        int id;
        String name;
        String specialty;

        Doctor(int id, String name, String specialty) {
            this.id = id;
            this.name = name;
            this.specialty = specialty;
        }

        @Override
        public String toString() {
            return String.format("ID: %-4d | Dr. %-20s | Specialty: %s", id, name, specialty);
        }
    }

    static class Appointment {
        int id;
        int patientId;
        int doctorId;
        String date;
        String time;

        Appointment(int id, int patientId, int doctorId, String date, String time) {
            this.id = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.date = date;
            this.time = time;
        }

        @Override
        public String toString() {
            return String.format(
                "Appt #%-4d | Patient ID: %-4d | Doctor ID: %-4d | %s at %s",
                id, patientId, doctorId, date, time
            );
        }
    }

    static class Bill {
        int id;
        int patientId;
        double amount;
        boolean paid;

        Bill(int id, int patientId, double amount, boolean paid) {
            this.id = id;
            this.patientId = patientId;
            this.amount = amount;
            this.paid = paid;
        }

        @Override
        public String toString() {
            return String.format(
                "Bill #%-4d | Patient ID: %-4d | Amount: %-10.2f | Status: %s",
                id, patientId, amount, paid ? "PAID" : "UNPAID"
            );
        }
    }

    // ---------- Storage ----------
    static ArrayList<Patient> patients = new ArrayList<>();
    static ArrayList<Doctor> doctors = new ArrayList<>();
    static ArrayList<Appointment> appointments = new ArrayList<>();
    static ArrayList<Bill> bills = new ArrayList<>();

    static int nextPatientId = 1;
    static int nextDoctorId = 1;
    static int nextApptId = 1;
    static int nextBillId = 1;

    static Scanner sc = new Scanner(System.in);

    // ---------- Main loop ----------
    public static void main(String[] args) {
        seedDoctors();

        while (true) {
            printMainMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1: addPatient(); break;
                case 2: viewPatients(); break;
                case 3: searchPatient(); break;
                case 4: updatePatient(); break;
                case 5: deletePatient(); break;
                case 6: viewDoctors(); break;
                case 7: addDoctor(); break;
                case 8: bookAppointment(); break;
                case 9: viewAppointments(); break;
                case 10: createBill(); break;
                case 11: viewBills(); break;
                case 12: markBillPaid(); break;
                case 0:
                    System.out.println("\nThank you for using Hospital Management System. Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }

    // ---------- Menus ----------
    static void printMainMenu() {
        System.out.println("\n========================================");
        System.out.println("   HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println(" 1.  Add Patient");
        System.out.println(" 2.  View All Patients");
        System.out.println(" 3.  Search Patient by ID");
        System.out.println(" 4.  Update Patient");
        System.out.println(" 5.  Delete Patient");
        System.out.println(" 6.  View All Doctors");
        System.out.println(" 7.  Add Doctor");
        System.out.println(" 8.  Book Appointment");
        System.out.println(" 9.  View Appointments");
        System.out.println("10.  Create Bill");
        System.out.println("11.  View Bills");
        System.out.println("12.  Mark Bill as Paid");
        System.out.println(" 0.  Exit");
        System.out.println("========================================");
    }

    // ---------- Patient functions ----------
    static void addPatient() {
        System.out.println("\n--- Add New Patient ---");
        String name = readNonEmpty("Name: ");
        int age = readPositiveInt("Age: ");
        String gender = readNonEmpty("Gender (M/F/Other): ");
        String phone = readNonEmpty("Phone: ");
        String diagnosis = readNonEmpty("Diagnosis: ");

        Patient p = new Patient(nextPatientId++, name, age, gender, phone, diagnosis);
        patients.add(p);

        System.out.println("✅ Patient added successfully. ID = " + p.id);
    }

    static void viewPatients() {
        System.out.println("\n--- All Patients ---");
        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        for (Patient p : patients) {
            System.out.println(p);
        }
    }

    static void searchPatient() {
        System.out.println("\n--- Search Patient ---");
        int id = readInt("Enter patient ID: ");
        Patient p = findPatient(id);
        if (p == null) {
            System.out.println("❌ No patient found with ID " + id);
        } else {
            System.out.println(p);
        }
    }

    static void updatePatient() {
        System.out.println("\n--- Update Patient ---");
        int id = readInt("Enter patient ID to update: ");
        Patient p = findPatient(id);
        if (p == null) {
            System.out.println("❌ No patient found with ID " + id);
            return;
        }
        System.out.println("Current: " + p);
        System.out.println("(Press Enter to keep current value)");

        String name = readOptional("Name [" + p.name + "]: ");
        if (!name.isEmpty()) p.name = name;

        String ageStr = readOptional("Age [" + p.age + "]: ");
        if (!ageStr.isEmpty()) {
            try {
                int a = Integer.parseInt(ageStr);
                if (a > 0) p.age = a;
                else System.out.println("⚠️ Age must be positive. Keeping old value.");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid number. Keeping old value.");
            }
        }

        String gender = readOptional("Gender [" + p.gender + "]: ");
        if (!gender.isEmpty()) p.gender = gender;

        String phone = readOptional("Phone [" + p.phone + "]: ");
        if (!phone.isEmpty()) p.phone = phone;

        String diagnosis = readOptional("Diagnosis [" + p.diagnosis + "]: ");
        if (!diagnosis.isEmpty()) p.diagnosis = diagnosis;

        System.out.println("✅ Patient updated.");
    }

    static void deletePatient() {
        System.out.println("\n--- Delete Patient ---");
        int id = readInt("Enter patient ID to delete: ");
        Patient p = findPatient(id);
        if (p == null) {
            System.out.println("❌ No patient found with ID " + id);
            return;
        }
        patients.remove(p);
        System.out.println("✅ Patient deleted.");
    }

    static Patient findPatient(int id) {
        for (Patient p : patients) {
            if (p.id == id) return p;
        }
        return null;
    }

    // ---------- Doctor functions ----------
    static void seedDoctors() {
        doctors.add(new Doctor(nextDoctorId++, "Maurilio Pugliesi", "General Medicine"));
        doctors.add(new Doctor(nextDoctorId++, "Justine Doe",     "Cardiology"));
        doctors.add(new Doctor(nextDoctorId++, "Abebe Kebede",    "Pediatrics"));
    }

    static void viewDoctors() {
        System.out.println("\n--- All Doctors ---");
        for (Doctor d : doctors) {
            System.out.println(d);
        }
    }

    static void addDoctor() {
        System.out.println("\n--- Add New Doctor ---");
        String name = readNonEmpty("Doctor name: ");
        String spec = readNonEmpty("Specialty: ");
        Doctor d = new Doctor(nextDoctorId++, name, spec);
        doctors.add(d);
        System.out.println("✅ Doctor added. ID = " + d.id);
    }

    static Doctor findDoctor(int id) {
        for (Doctor d : doctors) {
            if (d.id == id) return d;
        }
        return null;
    }

    // ---------- Appointment functions ----------
    static void bookAppointment() {
        System.out.println("\n--- Book Appointment ---");
        if (patients.isEmpty()) {
            System.out.println("⚠️ No patients yet. Add a patient first.");
            return;
        }
        if (doctors.isEmpty()) {
            System.out.println("⚠️ No doctors available.");
            return;
        }

        int patientId = readInt("Patient ID: ");
        if (findPatient(patientId) == null) {
            System.out.println("❌ No patient with ID " + patientId);
            return;
        }

        int doctorId = readInt("Doctor ID: ");
        if (findDoctor(doctorId) == null) {
            System.out.println("❌ No doctor with ID " + doctorId);
            return;
        }

        String date = readNonEmpty("Date (e.g. 2026-05-20): ");
        String time = readNonEmpty("Time (e.g. 10:30): ");

        Appointment a = new Appointment(nextApptId++, patientId, doctorId, date, time);
        appointments.add(a);
        System.out.println("✅ Appointment booked. ID = " + a.id);
    }

    static void viewAppointments() {
        System.out.println("\n--- All Appointments ---");
        if (appointments.isEmpty()) {
            System.out.println("No appointments booked yet.");
            return;
        }
        for (Appointment a : appointments) {
            System.out.println(a);
        }
    }

    // ---------- Billing functions ----------
    static void createBill() {
        System.out.println("\n--- Create Bill ---");
        int patientId = readInt("Patient ID: ");
        if (findPatient(patientId) == null) {
            System.out.println("❌ No patient with ID " + patientId);
            return;
        }
        double amount = readPositiveDouble("Amount: ");
        Bill b = new Bill(nextBillId++, patientId, amount, false);
        bills.add(b);
        System.out.println("✅ Bill created. ID = " + b.id);
    }

    static void viewBills() {
        System.out.println("\n--- All Bills ---");
        if (bills.isEmpty()) {
            System.out.println("No bills yet.");
            return;
        }
        for (Bill b : bills) {
            System.out.println(b);
        }
    }

    static void markBillPaid() {
        System.out.println("\n--- Mark Bill as Paid ---");
        int id = readInt("Bill ID: ");
        Bill target = null;
        for (Bill b : bills) {
            if (b.id == id) { target = b; break; }
        }
        if (target == null) {
            System.out.println("❌ No bill with ID " + id);
            return;
        }
        target.paid = true;
        System.out.println("✅ Bill marked as PAID.");
    }

    // ---------- Safe input helpers ----------
    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid number.");
            }
        }
    }

    static int readPositiveInt(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v > 0) return v;
            System.out.println("⚠️ Value must be positive.");
        }
    }

    static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double v = Double.parseDouble(line);
                if (v >= 0) return v;
                System.out.println("⚠️ Value must be 0 or more.");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid number.");
            }
        }
    }

    static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("⚠️ This field cannot be empty.");
        }
    }

    static String readOptional(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
}
