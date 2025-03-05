
import java.util.Comparator;
import java.util.PriorityQueue;

public class ProcessSchedulingIIX {

    PriorityQueue<Event> eventQueue;  //for all upcoming events
    PriorityQueue<Process> readyQueue;  //for waiting processes (sorted by SRTF)
    int currentTime = 0;
    final int CONTEXT_SWITCH_TIME = 1;
    int numOfProcess = 0;
    int completed = 0; // ***************
    int totalWaitingTime = 0;
    int totalTurnaroundTime = 0;
    int timeAfterSwitch = 0; // ************
    Process currentProcess = null;
    int index = 0; //to keep track of the elements in the list ***********
    int totalBusyTime = 0;
    int totalTimeElapsed = 0;
    int nextArrivalTime = 0;
    
    public ProcessSchedulingIIX() {
        
        eventQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.time)); //sorting by arrival
        
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getRemainingTime).thenComparingInt(Process::getArrivalTime)); //sorting SRTF
    }

    

    void addProcess(Process p) {
        eventQueue.add(new Event(p.getArrivalTime(), p, "arrived"));
        numOfProcess++;
    }


    void run() {
        System.out.printf("%-10s%-10s%n", "Time", "Process/CS");

        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            currentTime = event.time;

            switch (event.type) {
                case "arrived":
                    readyQueue.add(event.process);
                    scheduleNextProcess();
                    break;
                case "started":
                    scheduleNextProcess();
                    break;
                case "terminated":
                    readyQueue.poll();
                    scheduleNextProcess();
                    break;
            }
        }

        
        System.out.println(
            "\nAverage Turnaround Time: " + (double) totalTurnaroundTime
            / numOfProcess
        );
        System.out.println(
                "Average Waiting Time: " + (double) totalWaitingTime
                / numOfProcess
        );
        // Calculate CPU Utilization
        double cpuUtilization = (double) totalBusyTime / totalTimeElapsed * 100;

        System.out.printf("CPU Utilization: %.2f" , cpuUtilization);
    }

    void scheduleNextProcess() {
        if (!readyQueue.isEmpty()) {
            Process nextProcess = readyQueue.peek();

            if (currentProcess != null && currentProcess != nextProcess) {
                System.out.printf("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), currentProcess.getProcessID());
                currentTime += CONTEXT_SWITCH_TIME;
                totalTimeElapsed += CONTEXT_SWITCH_TIME;
                timeAfterSwitch = currentTime;
                System.out.printf("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
            }
            
            
            nextProcess.setRemainingTime(nextProcess.getRemainingTime() - 1);
            totalBusyTime++;
            currentTime++;
            totalTimeElapsed++;

            if (nextProcess.getRemainingTime() == 0) {
                completed++;
                nextProcess.setCompletionTime(currentTime);
                nextProcess.setTurnaroundTime(nextProcess.getCompletionTime() - nextProcess.getArrivalTime());
                nextProcess.setWaitingTime(nextProcess.getTurnaroundTime() - nextProcess.getBurstTime());

                totalTurnaroundTime += nextProcess.getTurnaroundTime();
                totalWaitingTime += nextProcess.getWaitingTime();
                System.out.printf("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), nextProcess.getProcessID());
                eventQueue.add(new Event(currentTime +nextProcess.getRemainingTime() , nextProcess, "terminated"));
                
                timeAfterSwitch=currentTime;
                currentProcess = null;


                // Context Switch Before Switching to Another Process
                /*if (!readyQueue.isEmpty()) { 
                    currentTime += CONTEXT_SWITCH_TIME;

                    totalTimeElapsed += CONTEXT_SWITCH_TIME;
                    timeAfterSwitch = currentTime;
                    System.out.printf("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
                }*/

                
            }

            else {
                eventQueue.add(new Event(currentTime + nextProcess.getRemainingTime(), nextProcess, "started"));
                currentProcess = nextProcess;
            }

        }
    }
}
