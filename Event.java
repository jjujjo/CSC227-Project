class Event {
    int time;
    Process process;
    String type;

    public Event(int time, Process process, String type) {
        this.time = time;
        this.process = process;
        this.type = type;
    }
}