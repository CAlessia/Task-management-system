package DataModel;

import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task {

    private List<Task> subTasks;

    public ComplexTask() {}
    public ComplexTask(int idTask) {
        super(idTask);
        subTasks = new ArrayList<>();
    }

    public void addTask(Task task) { subTasks.add(task); }

    public void deleteTask(Task task) { subTasks.remove(task); }

    @Override
    public int estimateDuration() {
        int totalDuration = 0;
        for (Task task : subTasks) {
            totalDuration += task.estimateDuration();
        }
        return totalDuration;
    }

    @Override
    public String getTaskType() {
        return "Complex";
    }

}
