
import java.util.Comparator;
import java.util.List;

import java.util.PriorityQueue;

public class ProcessSchedulingNew {

    public void simulateProcess(List<Process> processes) {

        // Sort processes by arrival time in case the user did not enter the processes in order of arrival time
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        // Priority Queue for Ready Queue (sorted by Remaining Time (SJF), then if both have the same Remaining Time sort using Arrival Time (FCFS))
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingTime)
                        .thenComparingInt(Process::getArrivalTime) // FCFS tie-breaker
        );

        int currentTime = 0, completed = 0, contextSwitchTime = 1;
        int totalWaitingTime = 0, totalTurnaroundTime = 0, contextSwitchCount = 0;
        int timeAfterSwitch = 0;
        Process currentProcess = null;
        int index = 0; //to keep track of the elements in the list
        int totalBusyTime = 0;
        int totalTimeElapsed = 0;
        int nextArrivalTime = 0;

        while (completed < processes.size()) {
            // Add all processes that have arrived (arrival time <= current time) to the ready queue 
            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index));
                index++;
            }

            // If no process is available, move time to the next arrival (like if p1 arrives at 0 and has burst time=2 but p2 arrives at 5)
            if (readyQueue.isEmpty()) {
                if (index < processes.size()) {
                    nextArrivalTime = processes.get(index).getArrivalTime();
                    if (currentTime < nextArrivalTime) {
                        System.out.printf("%-10s%s%n", (currentTime + "-" + nextArrivalTime), "IDLE");
                        totalTimeElapsed += nextArrivalTime - currentTime; // Update total time
                    }
                    currentTime = nextArrivalTime;
                    timeAfterSwitch = currentTime;
                    continue; // go back to the loop header
                } else {
                    break; //break from loop because all process are done
                }
            } //end if

            // Get the shortest remaining time process which is the first one in the PQueue
            Process shortest = readyQueue.poll();

            // Preemption Check
            if (currentProcess != null && currentProcess != shortest) {
                System.out.printf("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), currentProcess.getProcessID());
                if (!readyQueue.isEmpty()) {
                    currentTime += contextSwitchTime;

                    totalTimeElapsed += contextSwitchTime;

                    timeAfterSwitch = currentTime;
                    System.out.printf("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
                    contextSwitchCount++;
                }
                else{ 
                    timeAfterSwitch = currentTime;
                }
            }
            // excute the process 
            shortest.setRemainingTime(shortest.getRemainingTime() - 1);
            totalBusyTime++;
            currentTime++;
            totalTimeElapsed++;

            // Check if Process is Finished
            if (shortest.getRemainingTime() == 0) {
                completed++;
                shortest.setCompletionTime(currentTime);
                shortest.setTurnaroundTime(shortest.getCompletionTime() - shortest.getArrivalTime());
                shortest.setWaitingTime(shortest.getTurnaroundTime() - shortest.getBurstTime());

                totalTurnaroundTime += shortest.getTurnaroundTime();
                totalWaitingTime += shortest.getWaitingTime();
                System.out.printf("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), shortest.getProcessID());
                
                timeAfterSwitch=currentTime; 
                if (completed == processes.size()) {
                    break; // from while
                }

                // Context Switch Before Switching to Another Process
                if (!readyQueue.isEmpty()) { //why??
                    currentTime += contextSwitchTime;

                    totalTimeElapsed += contextSwitchTime;
                    timeAfterSwitch = currentTime;
                    System.out.printf("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
                    contextSwitchCount++;
                }
                currentProcess = null;
                continue; 
            }

            // Reinsert the Process if Not Finished
            readyQueue.add(shortest);
            currentProcess = shortest;
        } // end of while loop

        System.out.println(
                "\nAverage Turnaround Time: " + (double) totalTurnaroundTime
                / processes.size()
        );
        System.out.println(
                "Average Waiting Time: " + (double) totalWaitingTime
                / processes.size()
        );
        // Calculate CPU Utilization
        double cpuUtilization = (double) totalBusyTime / totalTimeElapsed * 100;

        System.out.println(
                "CPU Utilization:  " + cpuUtilization);

    }

}
