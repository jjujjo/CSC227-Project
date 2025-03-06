import java.util.*;

public class ProcessSchedulingNew2 {

    private int currentTime = 0, completed = 0, contextSwitchTime = 1;
    private int totalWaitingTime = 0, totalTurnaroundTime = 0, contextSwitchCount = 0;
    private int totalBusyTime = 0;
    private int totalTimeElapsed = 0;
    private Process currentProcess = null;

    public void simulateProcess(List<Process> processes) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        PriorityQueue<Event> eventQueue = new PriorityQueue<>(Comparator.comparingInt(Event::getTime));
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingTime)
                        .thenComparingInt(Process::getArrivalTime)
        );

        System.out.printf("%-10s%-10s%n", "Time", "Process/CS");

        // Schedule initial process arrivals
        for (Process p : processes) {
            eventQueue.add(new Event(p.getArrivalTime(), "PROCESS_ARRIVAL", p));
        }

        while (!eventQueue.isEmpty() || !readyQueue.isEmpty()) {
            Event event = eventQueue.poll();
            if (event != null) {
                currentTime = event.getTime();
            }

            // Handle idle time before processing the event
            if (currentTime > totalTimeElapsed) {
                int idleStart = totalTimeElapsed;
                int idleEnd = currentTime;
                if (idleStart != idleEnd) {
                    System.out.printf("%-10s%s%n", (idleStart + "-" + idleEnd), "IDLE");
                }
                totalTimeElapsed = currentTime;
            }

            // Handle event types
            switch (event != null ? event.getType() : "") {
                case "PROCESS_ARRIVAL":
                    readyQueue.add(event.getProcess());

                    // If no process is currently executing, start execution
                    if (currentProcess == null) {
                        startNewProcess(readyQueue, eventQueue);
                    } else if (event.getProcess().getRemainingTime() < currentProcess.getRemainingTime()) {
                        // Preempt the current process if the new process has a shorter remaining time
                        readyQueue.add(currentProcess);
                        currentProcess = null;
                        startNewProcess(readyQueue, eventQueue);
                    }
                    break;

                case "PROCESS_COMPLETION":
                    // Ensure we are not calling methods on a null process
                    if (currentProcess != null) {
                        completed++;
                        currentProcess.setCompletionTime(currentTime);
                        currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                        currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

                        totalTurnaroundTime += currentProcess.getTurnaroundTime();
                        totalWaitingTime += currentProcess.getWaitingTime();
                        System.out.printf("%-10s%s%n", (currentTime - 1 + "-" + currentTime), currentProcess.getProcessID());

                        if (completed == processes.size()) {
                            break;
                        }

                        // After process completes, check if another process can be executed
                        if (!readyQueue.isEmpty()) {
                            eventQueue.add(new Event(currentTime + contextSwitchTime, "CONTEXT_SWITCH", null));
                        }
                        currentProcess = null;
                    }
                    break;

                case "CONTEXT_SWITCH":
                    contextSwitchCount++;
                    totalTimeElapsed += contextSwitchTime;
                    System.out.printf("%-10s%s%n", (currentTime + "-" + (currentTime + contextSwitchTime)), "CS");
                    currentTime += contextSwitchTime;
                    if (!readyQueue.isEmpty()) {
                        startNewProcess(readyQueue, eventQueue);
                    }
                    break;
            }

            // If the ready queue is not empty and there's no process running, start the next process
            if (!readyQueue.isEmpty() && currentProcess == null) {
                startNewProcess(readyQueue, eventQueue);
            }
        }

        // Calculate and display performance metrics
        double cpuUtilization = (double) totalBusyTime / totalTimeElapsed * 100;
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        double avgWaitingTime = (double) totalWaitingTime / processes.size();

        System.out.println("\nPerformance Metrics:");
        System.out.printf("Average Turnaround Time: %.2f%n", avgTurnaroundTime);
        System.out.printf("Average Waiting Time: %.2f%n", avgWaitingTime);
        System.out.printf("CPU Utilization: %.2f%%%n", cpuUtilization);
    }

    private void startNewProcess(PriorityQueue<Process> readyQueue, PriorityQueue<Event> eventQueue) {
        Process process = readyQueue.poll();
        if (process != null) {
            currentProcess = process;
            int executionTime = process.getRemainingTime(); // Execute the process until completion

            // Ensure the new process does not interfere with an ongoing process
            for (Event e : eventQueue) {
                if (e.getTime() < currentTime + executionTime && e.getType().equals("PROCESS_ARRIVAL")) {
                    Process newProcess = e.getProcess();
                    if (newProcess.getRemainingTime() < currentProcess.getRemainingTime()) {
                        // Preempt the current process
                        executionTime = e.getTime() - currentTime;
                        break;
                    }
                }
            }

            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);
            totalBusyTime += executionTime;
            totalTimeElapsed = currentTime + executionTime;

            if (currentProcess.getRemainingTime() > 0) {
                readyQueue.add(currentProcess);
            } else {
                eventQueue.add(new Event(currentTime + executionTime, "PROCESS_COMPLETION", currentProcess));
            }

            System.out.printf("%-10s%s%n", (currentTime + "-" + (currentTime + executionTime)), currentProcess.getProcessID());
            currentTime += executionTime;
        }
    }
}
