import java.util.*;

public class ProcessSchedulingNew2 {

    class Event {
        int time;
        String type; // "arrival", "execution", "completion", "CS", "IDLE"
        Process process;

        public Event(int time, String type, Process process) {
            this.time = time;
            this.type = type;
            this.process = process;
        }
    }

    public void simulateProcess(List<Process> processes) {
        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.time));
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingTime)
                        .thenComparingInt(Process::getArrivalTime));

        int currentTime = 0, completed = 0, contextSwitchTime = 1;
        int totalWaitingTime = 0, totalTurnaroundTime = 0, totalBusyTime = 0, totalTimeElapsed = 0;
        Process currentProcess = null;

        for (Process p : processes) {
            eventQueue.add(new Event(p.getArrivalTime(), "arrival", p));
        }

        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            
            // Handle CPU idle time
            if (currentTime < event.time) {
                System.out.printf("%-10s%s%n", currentTime + "-" + event.time, "IDLE");
                currentTime = event.time;
            }

            switch (event.type) {
                case "arrival":
                    readyQueue.add(event.process);
                    scheduleNextExecution(readyQueue, eventQueue, currentProcess, currentTime);
                    break;
                case "execution":
                    System.out.printf("%-10s%s%n", currentTime + "-", event.process.getProcessID());
                    currentProcess = executeProcess(event.process, currentTime, eventQueue);
                    break;
                case "completion":
                    System.out.printf("%-10s%s (Completed)%n", currentTime + "-", event.process.getProcessID());
                    completeProcess(event.process, currentTime, readyQueue, eventQueue);
                    completed++;
                    currentProcess = null;
                    break;
                case "CS":
                    System.out.printf("%-10s%s%n", currentTime + "-", "CS");
                    handleContextSwitch(eventQueue, currentTime);
                    break;
            }
        }

        System.out.println("\nAverage Turnaround Time: " + (double) totalTurnaroundTime / processes.size());
        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processes.size());
        System.out.println("CPU Utilization: " + ((double) totalBusyTime / totalTimeElapsed) * 100);
    }

    private void scheduleNextExecution(PriorityQueue<Process> readyQueue, PriorityQueue<Event> eventQueue,
                                       Process currentProcess, int currentTime) {
        if (currentProcess == null && !readyQueue.isEmpty()) {
            Process next = readyQueue.poll();
            eventQueue.add(new Event(currentTime, "execution", next));
        } else if (!readyQueue.isEmpty()) {
            Process next = readyQueue.peek();
            if (next.getRemainingTime() < currentProcess.getRemainingTime()) {
                eventQueue.add(new Event(currentTime, "CS", null));
            }
        }
    }

    private Process executeProcess(Process process, int currentTime, PriorityQueue<Event> eventQueue) {
        int executionTime = Math.min(process.getRemainingTime(), getNextArrivalGap(eventQueue, currentTime));
        process.setRemainingTime(process.getRemainingTime() - executionTime);
        if (process.getRemainingTime() == 0) {
            eventQueue.add(new Event(currentTime + executionTime, "completion", process));
        } else {
            eventQueue.add(new Event(currentTime + executionTime, "CS", null));
        }
        return process;
    }

    private void completeProcess(Process process, int currentTime, PriorityQueue<Process> readyQueue,
                                  PriorityQueue<Event> eventQueue) {
        process.setCompletionTime(currentTime);
        process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
        process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
        eventQueue.add(new Event(currentTime, "CS", null));
    }

    private void handleContextSwitch(PriorityQueue<Event> eventQueue, int currentTime) {
        eventQueue.add(new Event(currentTime + 1, "execution", null));
    }

    private int getNextArrivalGap(PriorityQueue<Event> eventQueue, int currentTime) {
        for (Event e : eventQueue) {
            if (e.type.equals("arrival") && e.time > currentTime) {
                return e.time - currentTime;
            }
        }
        return Integer.MAX_VALUE;
    }
}