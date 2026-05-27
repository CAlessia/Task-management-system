package DataModel;

public final class SimpleTask extends DataModel.Task {
    private int startHour;
    private int endHour;

    public SimpleTask() {}
    public SimpleTask(int idTask, int startHour, int endHour) {
        super(idTask);
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    @Override
    public int estimateDuration() {
        return endHour - startHour;
    }

    @Override
    public String getTaskType() {
        return "Simple";
    }
}
