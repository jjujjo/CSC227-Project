
import java.util.Comparator;
import java.util.List;

import java.util.PriorityQueue;

public class ProcessSchedulingNew {

    public double AverageWaitingTime;
    public double AverageTurnaroundTime;
    public double CPUutilization;
    public String Timetable="";

    public void simulateProcess(List<Process> processes) {

        // Sort processes by arrival time in case the user did not enter the processes in order of arrival time
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        // Priority Queue for Ready Queue (sorted by Remaining Time (SJF), then if both have the same Remaining Time sort using Arrival Time (FCFS))
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingTime)
                        .thenComparingInt(Process::getArrivalTime) // FCFS tie-breaker
        );

        int currentTime = 0, completed = 0, contextSwitchTime = 1;
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        int timeAfterSwitch = 0;
        Process currentProcess = null;
        int index = 0; //to keep track of the elements in the list
        int totalBusyTime = 0;
        int totalTimeElapsed = 0;
        int nextArrivalTime = 0;

        //System.out.printf("%-10s%-10s%n", "Time", "Process/CS");
        Timetable+= String.format("%-10s%-10s%n", "Time", "Process/CS");

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
                    Timetable+=String.format("%-10s%s%n", (currentTime + "-" + nextArrivalTime), "IDLE");
                    //System.out.printf("%-10s%s%n", (currentTime + "-" + nextArrivalTime), "IDLE");
                    
                    totalTimeElapsed += nextArrivalTime - currentTime; // Update total time

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
                Timetable+=String.format("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), currentProcess.getProcessID());
                //System.out.printf("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), currentProcess.getProcessID());
                if (!readyQueue.isEmpty()) {
                    currentTime += contextSwitchTime;

                    totalTimeElapsed += contextSwitchTime;

                    timeAfterSwitch = currentTime;
                    Timetable+=String.format("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
                    //System.out.printf("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
                    contextSwitchCount++;
                } else {
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
                Timetable+=String.format("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), shortest.getProcessID());
                //System.out.printf("%-10s%s%n", (timeAfterSwitch + "-" + currentTime), shortest.getProcessID());

                timeAfterSwitch = currentTime;
                if (completed == processes.size()) {
                    break; // from while
                }

                // Context Switch Before Switching to Another Process
                if (!readyQueue.isEmpty()) {
                    currentTime += contextSwitchTime;

                    totalTimeElapsed += contextSwitchTime;
                    timeAfterSwitch = currentTime;
                    Timetable+=String.format("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
                    //System.out.printf("%-10s%s%n", ((currentTime - 1) + "-" + currentTime), "CS");
                    contextSwitchCount++;
                }
                currentProcess = null;
                continue;
            }

            // Reinsert the Process if Not Finished
            readyQueue.add(shortest);
            currentProcess = shortest;
        } // end of while loop

        AverageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        AverageWaitingTime = (double) totalWaitingTime / processes.size();
        
        // Calculate CPU Utilization
        CPUutilization = (double) totalBusyTime / totalTimeElapsed * 100;

     
    }

<<<<<<< HEAD:ProcessSchedulingNew.java
}
=======
    public double getAverageWaitingTime() {
        return AverageWaitingTime;
    }

    public double getAverageTurnaroundTime() {
        return AverageTurnaroundTime;
    }

    public double getCPUutilization() {
        return CPUutilization;
    }

    public String getTimetable() {
        return Timetable;
    }
    
    
    

}
>>>>>>> 78a011e56c693cb7a79c36272781620d5e12c66c:src/ProcessSchedulingNew.java
