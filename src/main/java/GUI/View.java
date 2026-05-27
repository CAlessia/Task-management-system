package GUI;

import BusinessLogic.TasksManagement;
import BusinessLogic.Utility;
import DataModel.ComplexTask;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;
import java.util.Map;

public class View extends JFrame implements Serializable{
    private JPanel panel;
    private JButton btnAddEmployee;
    private JButton btnAddTask;
    private JButton btnViewEmployee;
    private JButton btnModifyTask;
    private JButton btnViewTasks;
    private JButton btnFilterAndSortEmployees;
    private JButton btnCalculateTaskStatuses;
    private TasksManagement tasksManagement;


    public View(String name) {
        super(name);
        this.tasksManagement = loadTasksManagement();
        this.prepareGUI();
    }

    public void prepareGUI() {
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.panel = new JPanel();
        this.setContentPane(this.panel);
        this.panel.setLayout(new FlowLayout());

        btnAddEmployee = new JButton("Add Employee");
        btnAddTask = new JButton("Add Task");
        btnViewEmployee = new JButton("View Employees");
        btnModifyTask = new JButton("Modify Task Status");
        btnViewTasks = new JButton("View Tasks");
        btnFilterAndSortEmployees = new JButton("Filter Employees");
        btnCalculateTaskStatuses = new JButton("Calculate Task Statuses");

        this.panel.add(btnAddEmployee);
        this.panel.add(btnAddTask);
        this.panel.add(btnViewEmployee);
        this.panel.add(btnAddTask);
        this.panel.add(btnViewTasks);
        this.panel.add(btnModifyTask);
        this.panel.add(btnFilterAndSortEmployees);
        this.panel.add(btnCalculateTaskStatuses);

        btnAddEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddEmployeePanel();
            }

        });

        btnAddTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddTaskPanel();
            }
        });

        btnViewEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEmployeeListPanel();
            }
        });

        btnModifyTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showModifyTaskPanel();
            }
        });

        btnViewTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                panel.setLayout(new FlowLayout());

                JTextField txtEmployeeId = new JTextField(5);

                panel.add(new JLabel("Enter Employee ID:"));
                panel.add(txtEmployeeId);

                int option = JOptionPane.showConfirmDialog(View.this, panel, "Enter Employee ID", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        int employeeId = Integer.parseInt(txtEmployeeId.getText());
                        Employee employee = tasksManagement.findEmployeeById(employeeId);

                        if (employee != null) {
                            showTasksPanel(employee);
                        } else {
                            JOptionPane.showMessageDialog(View.this, "Employee not found.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(View.this, "Invalid Employee ID.");
                    }
                }
            }
        });

        btnFilterAndSortEmployees.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utility utility = new Utility();
                List<Employee> filteredSortedEmployees = utility.filterAndSortEmployeesByWorkDuration(tasksManagement);
                JPanel employeePanel = new JPanel();
                employeePanel.setLayout(new BoxLayout(employeePanel, BoxLayout.Y_AXIS));

                if (filteredSortedEmployees.isEmpty()) {
                    employeePanel.add(new JLabel("No employees found after filtering and sorting."));
                } else {
                    for (Employee employee : filteredSortedEmployees) {
                        String employeeInfo = "ID: " + employee.getIdEmployee() + ", Name: " + employee.getName();
                        employeePanel.add(new JLabel(employeeInfo));
                    }
                }

                JScrollPane scrollPane = new JScrollPane(employeePanel);
                JOptionPane.showMessageDialog(View.this, scrollPane, "Filtered and Sorted Employees", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnCalculateTaskStatuses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Map<String, Integer>> taskStatusMap = Utility.calculateTaskStatuses(tasksManagement.getTasksEmployee());
                displayTaskStatuses(taskStatusMap);
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveEmployeeData();
                saveTaskData();
                System.exit(0);
            }
        });
    }

    private void showAddEmployeePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JTextField txtEmployeeName = new JTextField(20);

        panel.add(new JLabel("Employee Name:"));
        panel.add(txtEmployeeName);

        int option = JOptionPane.showConfirmDialog(this, panel, "Add Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = txtEmployeeName.getText();
                Employee employee = tasksManagement.createNewEmployee(name);
                saveEmployeeData();
                JOptionPane.showMessageDialog(View.this, "Employee added successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(View.this, "An error occurred while adding the employee: " + ex.getMessage());
            }
        }
    }

    private void showEmployeeListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (tasksManagement.getTasksEmployee().isEmpty()) {
            panel.add(new JLabel("No employees found."));
        } else {
            for (Employee employee : tasksManagement.getTasksEmployee().keySet()) {
                String employeeInfo = "ID: " + employee.getIdEmployee() + " Name: " + employee.getName();
                panel.add(new JLabel(employeeInfo));
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        JOptionPane.showMessageDialog(this, scrollPane, "Employee List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddTaskPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JTextField txtEmployeeId = new JTextField(5);
        JTextField txtStartHour = new JTextField(5);
        JTextField txtEndHour = new JTextField(5);

        String[] taskTypes = { "Simple", "Complex" };
        JComboBox<String> taskTypeComboBox = new JComboBox<>(taskTypes);

        panel.add(new JLabel("Employee ID:"));
        panel.add(txtEmployeeId);
        panel.add(new JLabel("Task Type:"));
        panel.add(taskTypeComboBox);

        panel.add(new JLabel("Start Hour:"));
        panel.add(txtStartHour);
        panel.add(new JLabel("End Hour:"));
        panel.add(txtEndHour);

        int option = JOptionPane.showConfirmDialog(this, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int employeeId = Integer.parseInt(txtEmployeeId.getText());
                String taskType = (String) taskTypeComboBox.getSelectedItem();

                int taskId = tasksManagement.getNextTaskId();
                tasksManagement.setNextTaskId(taskId + 1);

                Task task = null;
                if ("Simple".equals(taskType)) {
                    int startHour = Integer.parseInt(txtStartHour.getText());
                    int endHour = Integer.parseInt(txtEndHour.getText());
                    task = new SimpleTask(taskId, startHour, endHour);
                } else if ("Complex".equals(taskType)) {
                    task = new ComplexTask(taskId);
                }

                tasksManagement.assignTaskToEmployee(employeeId, task);
                saveTaskData();
                JOptionPane.showMessageDialog(View.this, "Task added successfully.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(View.this, "Invalid Input.");
            }
        }
    }

    private void showTasksPanel(Employee employee) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<Task> tasks = tasksManagement.getTasksEmployee().get(employee);

        if (tasks == null || tasks.isEmpty()) {
            panel.add(new JLabel("No tasks found for this employee."));
        } else {
            for (Task task : tasks) {
                String taskInfo = "Task ID: " + task.getIdTask() + ", Type: " + task.getTaskType() + ", Status: " + task.getStatusTask();

                if (task instanceof SimpleTask) {
                    SimpleTask simpleTask = (SimpleTask) task;
                    taskInfo += ", Start Hour: " + simpleTask.getStartHour() + ", End Hour: " + simpleTask.getEndHour();
                }

                panel.add(new JLabel(taskInfo));
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        JOptionPane.showMessageDialog(this, scrollPane, "Employee Tasks", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showModifyTaskPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JTextField txtEmployeeId = new JTextField(5);
        JTextField txtTaskId = new JTextField(5);
        JButton btnModify = new JButton("Modify Task Status");

        panel.add(new JLabel("Employee ID:"));
        panel.add(txtEmployeeId);
        panel.add(new JLabel("Task ID:"));
        panel.add(txtTaskId);
        panel.add(btnModify);

        btnModify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int employeeId = Integer.parseInt(txtEmployeeId.getText());
                    int taskId = Integer.parseInt(txtTaskId.getText());
                    tasksManagement.modifyTaskStatus(employeeId, taskId);
                    JOptionPane.showMessageDialog(View.this, "Task status modified successfully.");

                } catch (NumberFormatException ex) {

                    JOptionPane.showMessageDialog(View.this, "Invalid input. Please enter valid Employee and Task IDs.");
                }
            }
        });

        JOptionPane.showOptionDialog(this, panel, "Modify Task Status", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, new Object[] { "OK" }, null);

    }

    private void displayTaskStatuses(Map<String, Map<String, Integer>> taskStatusMap) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (String employeeName : taskStatusMap.keySet()) {
            panel.add(new JLabel("Employee: " + employeeName));
            Map<String, Integer> taskStatuses = taskStatusMap.get(employeeName);
            for (String status : taskStatuses.keySet()) {
                panel.add(new JLabel(status + ": " + taskStatuses.get(status)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        JOptionPane.showMessageDialog(this, scrollPane, "Task Statuses", JOptionPane.INFORMATION_MESSAGE);
    }

    private TasksManagement loadTasksManagement() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("employee.ser"))) {
            return (TasksManagement) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(View.this, "Error loading data: " + e.getMessage());
            return new TasksManagement();
        }
    }

    public void saveEmployeeData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employee.ser"))) {
            out.writeObject(tasksManagement);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(View.this, "Error saving data: " + e.getMessage());
        }
    }

    public void saveTaskData(){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tasks.ser"))) {
            out.writeObject(tasksManagement.getTasksEmployee());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(View.this, "Error saving tasks data: " + e.getMessage());
        }
    }
}
