package BusinessLogic;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksManagement implements Serializable {
    private static final long serialVersionUID = 1L;
    private int nextEmployeeId = 1;
    private int nextTaskId = 1;
    private Map<Employee, List<Task>> tasksEmployee = new HashMap<>();

    public TasksManagement() {}

    public Employee createNewEmployee(String name) {
        Employee employee = new Employee(nextEmployeeId++, name);
        tasksEmployee.put(employee, new ArrayList<>());
        return employee;
    }

    public void assignTaskToEmployee(int idEmployee, Task task){
        Employee employee = findEmployeeById(idEmployee);
        if (employee != null) {
            List<Task> employeeTasks = tasksEmployee.get(employee);
            if (employeeTasks == null) {
                employeeTasks = new ArrayList<>();
                tasksEmployee.put(employee, employeeTasks);
            }


            employeeTasks.add(task);

        }
    }

    public int calculateEmployeeWorkDuration(int idEmployee){
        Employee employee = findEmployeeById(idEmployee);
        if (employee != null && tasksEmployee.containsKey(employee)) {
            int totalDuration = 0;
            for (Task task : tasksEmployee.get(employee)) {
                totalDuration += task.estimateDuration();
            }
            return totalDuration;
        }
        return 0;
    }

    public void modifyTaskStatus(int idEmployee, int idTask) {
        Employee employee = findEmployeeById(idEmployee);
        if (employee != null && tasksEmployee.containsKey(employee)) {
            for (Task task : tasksEmployee.get(employee)) {
                if (task.getIdTask() == idTask) {
                    if(task.getStatusTask().equals("Completed")){
                        task.setStatusTask("Uncompleted");
                        return;
                    }
                    else {
                        task.setStatusTask("Completed");
                        return;
                    }
                }
            }
        }
    }

    public Employee findEmployeeById(int id) {
        for (Employee employee : tasksEmployee.keySet()) {
            if (employee.getIdEmployee() == id) {
                return employee;
            }
        }
        return null;
    }

    public int getNextEmployeeId() {
        return nextEmployeeId;
    }

    public void setNextEmployeeId(int nextEmployeeId) {
        this.nextEmployeeId = nextEmployeeId;
    }

    public int getNextTaskId() {
        return nextTaskId;
    }

    public void setNextTaskId(int nextTaskId) {
        this.nextTaskId = nextTaskId;
    }

    public Map<Employee, List<Task>> getTasksEmployee() {
        return tasksEmployee;
    }

    public void setTasksEmployee(Map<Employee, List<Task>> tasksEmployee) {
        this.tasksEmployee = tasksEmployee;
    }
}
