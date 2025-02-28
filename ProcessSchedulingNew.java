import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ProcessSchedulingNew {
    public void simulateProcess(List<Process> processes) {
        // Sort processes by arrival time initially
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        // Priority Queue for Ready Queue (sorted by Remaining Time, then Arrival Time)
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
            Comparator.comparingInt(Process::getRemainingTime)
                      .thenComparingInt(Process::getArrivalTime) // FCFS tie-breaker
        );

        int currentTime = 0, completed = 0, contextSwitchTime = 1;
        int totalWaitingTime = 0, totalTurnaroundTime = 0, contextSwitchCount = 0;
        int timeAfterSwitch = 0;
        Process currentProcess = null;
        int index = 0;

        while (completed < processes.size()) {
            // Add all processes that have arrived to the ready queue
            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index));
                index++;
            }

            // If no process is available, move time to the next arrival
            if (readyQueue.isEmpty()) {
                if (index < processes.size()) {
                    currentTime = processes.get(index).getArrivalTime();
                } else {
                    break;
                }
                continue;
            }

            // Get the shortest remaining time process
            Process shortest = readyQueue.poll();

            // **Preemption Check**
            if (currentProcess != null && currentProcess != shortest) {
                System.out.printf("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), currentProcess.getProcessID());
                currentTime += contextSwitchTime;
                timeAfterSwitch = currentTime;
                System.out.printf("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
                contextSwitchCount++;
                
                // Put the preempted process back into the ready queue
                readyQueue.add(currentProcess);
            }

            // **Execute Process for 1 Time Unit**
            shortest.setRemainingTime(shortest.getRemainingTime() - 1);
            currentTime++;

            // **Check if Process is Finished**
            if (shortest.getRemainingTime() == 0) {
                completed++;
                shortest.setCompletionTime(currentTime);
                shortest.setTurnaroundTime(shortest.getCompletionTime() - shortest.getArrivalTime());
                shortest.setWaitingTime(shortest.getTurnaroundTime() - shortest.getBurstTime());

                totalTurnaroundTime += shortest.getTurnaroundTime();
                totalWaitingTime += shortest.getWaitingTime();
                System.out.printf("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), shortest.getProcessID());

                if (completed == processes.size()) {
                    break;
                }

                // **Context Switch Before Switching to Another Process**
                currentTime += contextSwitchTime;
                timeAfterSwitch = currentTime;
                System.out.printf("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
                contextSwitchCount++;
                currentProcess = null;
                continue;
            }

            // **Reinsert the Process if Not Finished**
            readyQueue.add(shortest);
            currentProcess = shortest;
        }

        // **Final Statistics**
        System.out.println("\nAverage Turnaround Time: " + (double) totalTurnaroundTime / processes.size());
        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processes.size());
    }

}
