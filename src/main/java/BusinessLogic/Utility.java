package BusinessLogic;

import DataModel.Employee;
import DataModel.Task;

import java.util.*;
import java.util.stream.Collectors;

public class Utility {
    public List<Employee>  filterAndSortEmployeesByWorkDuration(TasksManagement tasksManagement) {
        Map<Employee, List<Task>> tasksEmployee = tasksManagement.getTasksEmployee();

        List<Employee> sortedEmployees = new ArrayList<>(tasksEmployee.keySet());

        sortedEmployees = sortedEmployees.stream()
                .filter(employee -> {
                    int totalDuration = tasksManagement.calculateEmployeeWorkDuration(employee.getIdEmployee());
                    return totalDuration > 40;
                })
                .sorted((e1, e2) -> {
                    int duration1 = tasksManagement.calculateEmployeeWorkDuration(e1.getIdEmployee());
                    int duration2 = tasksManagement.calculateEmployeeWorkDuration(e2.getIdEmployee());
                    return Integer.compare(duration1, duration2); // Ascending order
                })
                .collect(Collectors.toList());

        return sortedEmployees;
    }

    public static Map<String, Map<String, Integer>> calculateTaskStatuses(Map<Employee, List<Task>> tasksEmployee) {
        Map<String, Map<String, Integer>> employeeTaskStatuses = new HashMap<>();

        for (Map.Entry<Employee, List<Task>> entry : tasksEmployee.entrySet()) {
            Employee employee = entry.getKey();
            List<Task> tasks = entry.getValue();

            Map<String, Integer> statusCountMap = new HashMap<>();
            statusCountMap.put("Completed", 0);
            statusCountMap.put("Uncompleted", 0);

            for (Task task : tasks) {
                String status = task.getStatusTask();
                if (status.equals("Completed")) {
                    statusCountMap.put("Completed", statusCountMap.get("Completed") + 1);
                } else {
                    statusCountMap.put("Uncompleted", statusCountMap.get("Uncompleted") + 1);
                }
            }

            employeeTaskStatuses.put(employee.getName(), statusCountMap);
        }

        return employeeTaskStatuses;
    }

}
