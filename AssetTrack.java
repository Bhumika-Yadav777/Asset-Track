import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AssetTrack {

private final DataManager dataManager;
private final AssetManager assetManager;
private final EmployeeManager employeeManager;
private final MaintenanceManager maintenanceManager;
private final Scanner scanner;
private String userRole = "Admin";

public AssetTrack() {
    this.dataManager = new DataManager();
    this.assetManager = new AssetManager(dataManager);
    this.employeeManager = new EmployeeManager(dataManager);
    this.maintenanceManager = new MaintenanceManager(dataManager);
    this.scanner = new Scanner(System.in);
    dataManager.loadData();
}
public void start() {
    System.out.println("--- AssetTrack: Small Office Inventory System ---");
    System.out.println("Current Role: " + userRole + " (Change via option 5)");
    while (true) {
        displayMainMenu();
        String choice = dataManager.getValidInput(scanner, "Enter your choice (1-5): ",
        s -> s.matches("[1-5]"), "Invalid choice. Please enter a number from 1 to 5.");
        try {
            switch (choice) {
                case "1":
                    handleAssetManagement();
                    break;
                case "2":
                    handleEmployeeManagement();
                    break;
                case "3":
                    handleMaintenanceTracking();
                    break;
                case "4":
                    dataManager.saveData();
                    System.out.println("\n[INFO] Data saved successfully. Exiting AssetTrack. Goodbye!");
                    return;
                case "5":
                    toggleUserRole();
                    break;
                }
            } 
            catch (Exception e) {
                System.err.println("[ERROR] An unexpected error occurred: " + e.getMessage());
            }
        }
    }
    private void displayMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1) Manage Assets");
        System.out.println("2) Manage Employees");
        System.out.println("3) Maintenance Tracking & Reports");
        System.out.println("4) Exit & Save Data");
        System.out.println("5) Toggle User Role (Admin/Staff)");
        System.out.print(">");
    }
    private void toggleUserRole() {
        userRole = userRole.equals("Admin") ? "Staff" : "Admin";
        System.out.println("\n[INFO] Role switched to: " + userRole);
    }

private void handleAssetManagement() {
    String choice;
    do {
        System.out.println("\n--- ASSET MANAGEMENT ---");
        System.out.println("1) Add New Asset");
        System.out.println("2) View All Assets");
        System.out.println("3) Update Asset Status");
        System.out.println("4) Delete Asset (Admin Only)");
        System.out.println("5) Back to Main Menu");
        System.out.print(">");
        choice = dataManager.getValidInput(scanner, "Enter your choice (1-5): ",
        s -> s.matches("[1-5]"), "Invalid choice. Enter 1-5.");
        try {switch (choice) {
            case "1": assetManager.addAsset(scanner); break;
            case "2": assetManager.viewAllAssets(); break;
            case "3": handleAssetUpdateSubmenu(); break;
            case "4":
                if (userRole.equals("Admin")) {
                    assetManager.deleteAsset(scanner);
                } else {
                    System.out.println("[ACCESS DENIED] Only Admin can delete assets.");
                }
                break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[FAILURE] Operation failed: " + e.getMessage());
        }
    } while (!choice.equals("5"));
}

private void handleAssetUpdateSubmenu() {
    String subChoice;
    do {
        System.out.println("\n--- UPDATE ASSET STATUS ---");
        System.out.println("1) Assign to Employee");
        System.out.println("2) De-assign (Return to Stock)");
        System.out.println("3) Change Maintenance/Retirement Status");
        System.out.println("4) Back to Asset Management Menu");
        System.out.print(">");
        subChoice = dataManager.getValidInput(scanner, "Enter your choice (1-4): ",
            s -> s.matches("[1-4]"), "Invalid choice. Enter 1-4.");
            try {
                switch (subChoice) {
                    case "1": assetManager.assignAsset(scanner, employeeManager); break;
                    case "2": assetManager.deassignAsset(scanner); break;
                    case "3": assetManager.updateAssetStatus(scanner); break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("[FAILURE] Operation failed: " + e.getMessage());
            }
        } while (!subChoice.equals("4"));
    }
    private void handleEmployeeManagement() {
        String choice;
        do {
            System.out.println("\n--- EMPLOYEE MANAGEMENT ---");
            System.out.println("1) Add New Employee");
            System.out.println("2) View All Employees");
            System.out.println("3) Delete Employee (Admin Only)");
            System.out.println("4) View Assets Assigned to Employee (Report)");
            System.out.println("5) Back to Main Menu");
            System.out.print(">");
            choice = dataManager.getValidInput(scanner, "Enter your choice (1-5): ",
            s -> s.matches("[1-5]"), "Invalid choice. Enter 1-5.");
            try { switch (choice) {
                case "1": employeeManager.addEmployee(scanner); break;
                case "2": employeeManager.viewAllEmployees(); break;
                case "3":
                    if (userRole.equals("Admin")) {
                        employeeManager.deleteEmployee(scanner, assetManager);
                    } else {
                            System.out.println("[ACCESS DENIED] Only Admin can delete employees.");
                        }
                        break;
                case "4": employeeManager.viewEmployeeAssets(scanner, assetManager); break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[FAILURE] Operation failed: " + e.getMessage());
        }
    } while (!choice.equals("5"));
}

private void handleMaintenanceTracking() {
    String choice;
    do {
        System.out.println("\n--- MAINTENANCE & REPORTS ---");
        System.out.println("1) Record Maintenance Event");
        System.out.println("2) View Maintenance History for Asset (Report)");
        System.out.println("3) Back to Main Menu");
        System.out.print(">");
        choice = dataManager.getValidInput(scanner, "Enter your choice (1-3): ",
        s -> s.matches("[1-3]"), "Invalid choice. Enter 1-3.");
        try { switch (choice) {
            case "1": maintenanceManager.recordMaintenance(scanner, assetManager); break;
            case "2": maintenanceManager.viewAssetHistory(scanner); break;
        }
    } catch (IllegalArgumentException e) {
        System.out.println("[FAILURE] Operation failed: " + e.getMessage());
    }
} while (!choice.equals("3"));
}

public static void main(String[] args) {
    new AssetTrack().start();
}
}

class Asset {
    private String assetId;
    private String name;
    private String category;
    private String serialNumber;
    private String status;
    private String assignedTo;
    public Asset(String assetId, String name, String category, String serialNumber, String status, String assignedTo) {
        this.assetId = assetId;
        this.name = name;
        this.category = category;
        this.serialNumber = serialNumber;
        this.status = status;
        this.assignedTo = assignedTo;
    }
    public String getAssetId() { return assetId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getSerialNumber() { return serialNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    @Override
    public String toString() {
    return String.format("A,%s,%s,%s,%s,%s,%s", assetId, name, category, serialNumber, status, assignedTo);
    }

    public static Asset fromString(String line) {
        String[] parts = line.split(",", 7);
        if (parts.length != 7 || !parts[0].equals("A")) 
            throw new IllegalArgumentException("Invalid Asset data format.");
        return new Asset(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6].equals("null") ? null : parts[6]);
    }
}

class Employee {
    private String employeeId;
    private String name;
    private String department;
    private String email;
    public Employee(String employeeId, String name, String department, String email) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.email = email;
    }
    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format("E,%s,%s,%s,%s", employeeId, name, department, email);
    }

    public static Employee fromString(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length != 5 || !parts[0].equals("E")) throw new IllegalArgumentException("Invalid Employee data format.");
        return new Employee(parts[1], parts[2], parts[3], parts[4]);
    }
}

class MaintenanceRecord {
    private String recordId;
    private String assetId;
    private LocalDateTime date;
    private String description;
    private double cost;
    public MaintenanceRecord(String assetId, String description, double cost) {
        this.recordId = UUID.randomUUID().toString();
        this.assetId = assetId;
        this.date = LocalDateTime.now();
        this.description = description;
        this.cost = cost;
    }
    public MaintenanceRecord(String recordId, String assetId, String dateStr, String description, double cost) {
        this.recordId = recordId;
        this.assetId = assetId;
        this.date = LocalDateTime.parse(dateStr, DataManager.DATE_FORMATTER);
        this.description = description;
        this.cost = cost;
    }
    public String getAssetId() { return assetId; }
    public String getDateFormatted() { return date.format(DataManager.DATE_FORMATTER); }
    public String getDescription() { return description; }
    public double getCost() { return cost; }

    @Override
    public String toString() {
        return String.format("M,%s,%s,%s,%s,%.2f", recordId, assetId, date.format(DataManager.DATE_FORMATTER), description, cost);
    }
    public static MaintenanceRecord fromString(String line) {
        String[] parts = line.split(",", 6);
        if (parts.length != 6 || !parts[0].equals("M")) throw new IllegalArgumentException("Invalid Maintenance data format.");
        return new MaintenanceRecord(parts[1], parts[2], parts[3], parts[4], Double.parseDouble(parts[5]));
    }
}
class DataManager {
    private static final String FILE_PATH = "data/inventory.txt";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<Asset> assets;
    private final List<Employee> employees;
    private final List<MaintenanceRecord> maintenanceRecords;
    public DataManager() {
        this.assets = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.maintenanceRecords = new ArrayList<>();
    }
    public List<Asset> getAssets() { return assets; }
    public List<Employee> getEmployees() { return employees; }
    public List<MaintenanceRecord> getMaintenanceRecords() { return maintenanceRecords; }
    public void saveData() {
        Path path = Paths.get(FILE_PATH);
        try {
            Files.createDirectories(path.getParent());
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) 
            {
                writer.println("# Assets:");
                assets.forEach(asset -> writer.println(asset.toString()));
                writer.println("\n# Employees:");
                employees.forEach(employee -> writer.println(employee.toString()));
                writer.println("\n# Maintenance Records:");
                maintenanceRecords.forEach(record -> writer.println(record.toString()));
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save data to file: " + e.getMessage());
        }
    }
    public void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("[INFO] Data file not found. Starting with empty inventory.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            assets.clear();
            employees.clear();
            maintenanceRecords.clear();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    if (line.startsWith("A,")) {
                        assets.add(Asset.fromString(line));
                    } else if (line.startsWith("E,")) {
                        employees.add(Employee.fromString(line));
                    } else if (line.startsWith("M,")) {
                        maintenanceRecords.add(MaintenanceRecord.fromString(line));
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("[WARNING] Skipping corrupted data line: " + line + " (" + e.getMessage() + ")");
                }
            }
            System.out.println("[INFO] Data loaded successfully: " + assets.size() + " Assets, " + employees.size() + " Employees, " + maintenanceRecords.size() + " Records.");
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load data from file: " + e.getMessage());
        }
    }
    public String getValidInput(Scanner scanner, String prompt, Predicate<String> validator, String errorMessage) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (validator.test(input) && !input.isEmpty()) {
                return input;
            } else if (input.isEmpty()) {
                System.out.println("[VALIDATION ERROR] Input cannot be blank. Please try again.");
            } else {
                System.out.println("[VALIDATION ERROR] " + errorMessage);
            }
        }
    }
}

class AssetManager {
    private final DataManager dataManager;
    public AssetManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    public void addAsset(Scanner scanner) {
        System.out.println("\n--- ADD NEW ASSET ---");
        String id = dataManager.getValidInput(scanner, "Enter unique Asset ID (e.g., LAP-001): ",s -> dataManager.getAssets().stream().noneMatch(a -> a.getAssetId().equalsIgnoreCase(s)),"Asset ID already exists. Try a different one.");
        String name = dataManager.getValidInput(scanner, "Enter Asset Name (e.g., MacBook Pro 2023): ", s -> !s.isEmpty(), "Name cannot be empty.");String category = dataManager.getValidInput(scanner, "Enter Category (e.g., IT, Furniture, Tool): ", s -> !s.isEmpty(), "Category cannot be empty.");
        String serial = dataManager.getValidInput(scanner, "Enter Serial Number (if none, enter NONE): ", s -> !s.isEmpty(), "Serial cannot be empty.");
        Asset newAsset = new Asset(id.toUpperCase(), name, category, serial, "Unassigned", null);dataManager.getAssets().add(newAsset);dataManager.saveData();
        System.out.println("\n[SUCCESS] Asset " + id + " (" + name + ") added successfully.");
    }
    public void viewAllAssets() {
        if (dataManager.getAssets().isEmpty()) {
            System.out.println("\n[INFO] No assets found in the inventory.");
            return;
            //
        }
        System.out.println("\n--- ALL ASSETS ---");
        System.out.printf("%-10s | %-25s | %-15s | %-15s | %-10s%n","ID", "Name", "Category", "Status", "Assigned To");
        System.out.println("---------------------------------------------------------------------------------");
        dataManager.getAssets().stream()
        .sorted(Comparator.comparing(Asset::getAssetId))
        .forEach(asset ->System.out.printf("%-10s | %-25s | %-15s | %-15s | %-10s%n",
        asset.getAssetId(),
        asset.getName(),
        asset.getCategory(),
        asset.getStatus(),
        asset.getAssignedTo() != null ? asset.getAssignedTo() : "N/A"));
    }
    public void assignAsset(Scanner scanner, EmployeeManager employeeManager) {
        System.out.println("\n--- ASSIGN ASSET ---");
        String assetId = dataManager.getValidInput(scanner, "Enter Asset ID to assign: ", s -> !s.isEmpty(), "Asset ID cannot be empty.").toUpperCase();Asset asset = findAssetById(assetId);
        if (asset == null) {
            throw new IllegalArgumentException("Asset ID " + assetId + " not found.");
        }
        String employeeId = dataManager.getValidInput(scanner, "Enter Employee ID to assign asset to: ", s -> !s.isEmpty(), "Employee ID cannot be empty.").toUpperCase();
        if (employeeManager.findEmployeeById(employeeId) == null) {
            throw new IllegalArgumentException("Employee ID " + employeeId + " not found. Cannot assign asset.");
        }
        if (asset.getStatus().equals("Retired")) {
            throw new IllegalArgumentException("Asset is Retired and cannot be reassigned.");
        }
        asset.setAssignedTo(employeeId);
        asset.setStatus("Assigned");
        dataManager.saveData();
        System.out.println("\n[SUCCESS] Asset " + assetId + " assigned to Employee " + employeeId + ".");
    }
    public void deassignAsset(Scanner scanner) {
        System.out.println("\n--- DE-ASSIGN ASSET ---");
        String assetId = dataManager.getValidInput(scanner, "Enter Asset ID to de-assign: ", s -> !s.isEmpty(), "Asset ID cannot be empty.").toUpperCase();
        Asset asset = findAssetById(assetId);
        if (asset == null) {
            throw new IllegalArgumentException("Asset ID " + assetId + " not found.");
        }
        if (asset.getAssignedTo() == null) {
            throw new IllegalArgumentException("Asset " + assetId + " is already Unassigned.");
        }
        asset.setAssignedTo(null);
        asset.setStatus("Unassigned");
        dataManager.saveData();
        System.out.println("\n[SUCCESS] Asset " + assetId + " de-assigned and status set to Unassigned.");
    }
    public void updateAssetStatus(Scanner scanner) {
        System.out.println("\n--- UPDATE ASSET STATUS ---");
        String assetId = dataManager.getValidInput(scanner, "Enter Asset ID to update: ", s -> !s.isEmpty(), "Asset ID cannot be empty.").toUpperCase();
        Asset asset = findAssetById(assetId);
        if (asset == null) {
            throw new IllegalArgumentException("Asset ID " + assetId + " not found.");
        }
        System.out.println("Current Status: " + asset.getStatus());
        String newStatus = dataManager.getValidInput(scanner, "Enter NEW Status (e.g., In Maintenance, Retired, Unassigned): ",
        s -> List.of("ASSIGNED", "UNASSIGNED", "IN MAINTENANCE", "RETIRED").contains(s.toUpperCase()),"Invalid status. Must be one of: Assigned, Unassigned, In Maintenance, Retired.");
        if (newStatus.equalsIgnoreCase("UNASSIGNED")) {
            asset.setAssignedTo(null);
        } else if (newStatus.equalsIgnoreCase("ASSIGNED") && asset.getAssignedTo() == null) {
            throw new IllegalArgumentException("Cannot set status to Assigned without assigning an employee first (use the Assign option).");
        }
        asset.setStatus(newStatus.substring(0, 1).toUpperCase() + newStatus.substring(1).toLowerCase());
        dataManager.saveData();
        System.out.println("\n[SUCCESS] Asset " + assetId + " status updated to " + asset.getStatus() + ".");
    }
    public void deleteAsset(Scanner scanner) {
        System.out.println("\n--- DELETE ASSET ---");
        String assetId = dataManager.getValidInput(scanner, "Enter Asset ID to DELETE: ", s -> !s.isEmpty(), "Asset ID cannot be empty.").toUpperCase();
        Asset assetToRemove = findAssetById(assetId);
        if (assetToRemove == null) {
            throw new IllegalArgumentException("Asset ID " + assetId + " not found.");
        }
        dataManager.getMaintenanceRecords().removeIf(r -> r.getAssetId().equalsIgnoreCase(assetId));
        dataManager.getAssets().remove(assetToRemove);
        dataManager.saveData();
        System.out.println("\n[SUCCESS] Asset " + assetId + " and all associated maintenance records deleted successfully.");
    }
    public Asset findAssetById(String id) {
        return dataManager.getAssets().stream().filter(a -> a.getAssetId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }
}

class EmployeeManager {
    private final DataManager dataManager;
    public EmployeeManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    public void addEmployee(Scanner scanner) {
        System.out.println("\n--- ADD NEW EMPLOYEE ---");
        String id = dataManager.getValidInput(scanner, "Enter unique Employee ID (e.g., EMP-101): ",
        s -> dataManager.getEmployees().stream().noneMatch(e -> e.getEmployeeId().equalsIgnoreCase(s)),"Employee ID already exists. Try a different one.");
        String name = dataManager.getValidInput(scanner, "Enter Employee Name: ", s -> !s.isEmpty(), "Name cannot be empty.");
        String dept = dataManager.getValidInput(scanner, "Enter Department: ", s -> !s.isEmpty(), "Department cannot be empty.");
         String email = dataManager.getValidInput(scanner, "Enter Email: ",
         s -> s.matches(".*@.*\\..*"),"Invalid email format.");
        Employee newEmployee = new Employee(id.toUpperCase(), name, dept, email);
        dataManager.getEmployees().add(newEmployee);
        dataManager.saveData();
        System.out.println("\n[SUCCESS] Employee " + id + " (" + name + ") added successfully.");
    }
    public void viewAllEmployees() {
        if (dataManager.getEmployees().isEmpty()) {
            System.out.println("\n[INFO] No employees found in the system.");
            return;
        }
    System.out.println("\n--- ALL EMPLOYEES ---");
    System.out.printf("%-10s | %-20s | %-15s | %-30s%n",
    "ID", "Name", "Department", "Email");
    System.out.println("----------------------------------------------------------------------");dataManager.getEmployees().stream().sorted(Comparator.comparing(Employee::getEmployeeId)).forEach(employee ->System.out.printf("%-10s | %-20s | %-15s | %-30s%n",employee.getEmployeeId(),employee.getName(),employee.getDepartment(),employee.getEmail()));
}

public void viewEmployeeAssets(Scanner scanner, AssetManager assetManager) {
    System.out.println("\n--- EMPLOYEE ASSET REPORT ---");
    String employeeId = dataManager.getValidInput(scanner, "Enter Employee ID for report: ", s -> !s.isEmpty(), "Employee ID cannot be empty.").toUpperCase();
    Employee employee = findEmployeeById(employeeId);
    if (employee == null) {
        throw new IllegalArgumentException("Employee ID " + employeeId + " not found.");
    }
    List<Asset> assignedAssets = dataManager.getAssets().stream().filter(a -> employeeId.equalsIgnoreCase(a.getAssignedTo())).collect(Collectors.toList());
    System.out.println("\nAssets assigned to " + employee.getName() + " (" + employeeId + "):");
    if (assignedAssets.isEmpty()) {
        System.out.println("[INFO] No assets currently assigned to this employee.");
    } else {
        System.out.printf("%-10s | %-25s | %-15s%", "Asset ID", "Name", "Category");
        System.out.println("---------------------------------------------------");
        assignedAssets.forEach(asset ->System.out.printf("%-10s | %-25s | %-15s%", asset.getAssetId(), asset.getName(), asset.getCategory()));
    }
}

public void deleteEmployee(Scanner scanner, AssetManager assetManager) {
    System.out.println("\n--- DELETE EMPLOYEE ---");
    String employeeId = dataManager.getValidInput(scanner, "Enter Employee ID to DELETE: ", s -> !s.isEmpty(), "Employee ID cannot be empty.").toUpperCase();
    Employee employeeToRemove = findEmployeeById(employeeId);
    if (employeeToRemove == null) {
        throw new IllegalArgumentException("Employee ID " + employeeId + " not found.");
    }
    long assignedCount = dataManager.getAssets().stream().filter(a -> employeeId.equalsIgnoreCase(a.getAssignedTo())).count();
    if (assignedCount > 0) {
        throw new IllegalArgumentException("Cannot delete Employee " + employeeId + ". They are still assigned " + assignedCount + " asset(s). De-assign all assets first.");
    }
    dataManager.getEmployees().remove(employeeToRemove);
    dataManager.saveData();
    System.out.println("\n[SUCCESS] Employee " + employeeId + " deleted successfully.");
}

public Employee findEmployeeById(String id) {
    return dataManager.getEmployees().stream().filter(e -> e.getEmployeeId().equalsIgnoreCase(id)).findFirst().orElse(null);
}
}

class MaintenanceManager {
    private final DataManager dataManager;
    public MaintenanceManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    public void recordMaintenance(Scanner scanner, AssetManager assetManager) {
        System.out.println("\n--- RECORD MAINTENANCE ---");
        String assetId = dataManager.getValidInput(scanner, "Enter Asset ID for maintenance record: ", s -> !s.isEmpty(), "Asset ID cannot be empty.").toUpperCase();
        Asset asset = assetManager.findAssetById(assetId);
        if (asset == null) {
            throw new IllegalArgumentException("Asset ID " + assetId + " not found.");
        }
        String description = dataManager.getValidInput(scanner, "Enter description of maintenance work: ", s -> !s.isEmpty(), "Description cannot be empty.");
        double cost = 0.0;
        while (true) {
            String costStr = dataManager.getValidInput(scanner, "Enter cost of maintenance (e.g., 50.75): ", s -> !s.isEmpty(), "Cost cannot be empty.");
            try {
                cost = Double.parseDouble(costStr);
                if (cost < 0) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("[VALIDATION ERROR] Please enter a valid non-negative number for the cost.");
            }
        }
        MaintenanceRecord newRecord = new MaintenanceRecord(assetId, description, cost);
        dataManager.getMaintenanceRecords().add(newRecord);
        if (!asset.getStatus().equalsIgnoreCase("In Maintenance")) {
            System.out.println("[INFO] Setting Asset Status to 'In Maintenance'.");
            asset.setStatus("In Maintenance");
        }
        dataManager.saveData();
        System.out.println("\n[SUCCESS] Maintenance record added for Asset " + assetId + ". Current Status: " + asset.getStatus());
    }
    public void viewAssetHistory(Scanner scanner) {
        System.out.println("\n--- ASSET MAINTENANCE HISTORY REPORT ---");
        String assetId = dataManager.getValidInput(scanner, "Enter Asset ID to view history: ", s -> !s.isEmpty(), "Asset ID cannot be empty.").toUpperCase();
        List<MaintenanceRecord> history = dataManager.getMaintenanceRecords().stream().filter(r -> r.getAssetId().equalsIgnoreCase(assetId)).sorted(Comparator.comparing(MaintenanceRecord::getDateFormatted).reversed()).collect(Collectors.toList());
        System.out.println("\nMaintenance History for Asset: " + assetId);
        if (history.isEmpty()) {
            System.out.println("[INFO] No maintenance records found for this asset.");
        } else {
            System.out.printf("%-20s | %-10s | %-40s%", "Date", "Cost", "Description");
            System.out.println("--------------------------------------------------------------------------------");
            history.forEach(record ->
            System.out.printf("%-20s | $%-9.2f | %-40s%",
            record.getDateFormatted(),record.getCost(),record.getDescription()));
        }
    }
}
