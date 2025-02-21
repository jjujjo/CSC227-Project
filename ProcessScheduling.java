import java.util.List;

public class ProcessScheduling {

    public void simulateProcess(List<Process> processes) {
        int currentTime = 0, completed = 0, contextSwitchTime = 1;
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        int contextSwitchCount = 0; //Counter for context switching
        int timeAfterSwitch = 0; //for printing format, when a process end I want to keep value of its begin time since last CS
        Process currentProcess = null; //initial value 

        while (completed < processes.size()) { //still we are not done with all processes 
            Process shortest = null; //initial value we will change later when we find the shortest process
            int minRemainingTime = Integer.MAX_VALUE; //Integer.MAX_VALUE is the largest value for an integer accepted by java -> we do this because we are searching for min, when we compare we want this to be for sure bigger

            // The goal here is to find the process with the shortest remaining time (burst time) 
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) { 
                    if (p.getRemainingTime() < minRemainingTime || 
                        (p.getRemainingTime() == minRemainingTime && p.getArrivalTime() < (shortest != null ? shortest.getArrivalTime() : Integer.MAX_VALUE))) {
                        /* we have two cases to enter the if:
                            1- Remaining time is less than the min -> we will enter so it can become the min
                            2- Remaining time equals the min and arrival time has two conditions:
                                2a- If the shortest is null (first time we enter) then we will compare that the arrival time is less than the Integer.MAX_VALUE (which is always true)
                                2b- If the shortest time is not null then we must then we will compare the arrival times since both burst times are equal -> apply FCFS
                         */
                        shortest = p;
                        minRemainingTime = p.getRemainingTime(); //update the min remaining time so we use it in the comparision the next time we enter the loop
                    }
                }
            }

            /*if (shortest == null) {
                // check is it null because all of them the remainingtime is zero
                boolean allDone = true;
                for (Process p : processes) {
                    if (p.getRemainingTime() > 0) {
                        allDone = false;
                        break;
                    }
                }
                if (allDone) {
                    break; // No more runnable processes, exit loop
                }

                currentTime++;
                continue;
            }*/

            if (currentProcess != null && currentProcess != shortest) { //we have a process that is shorter than the process we are working on -> context switch
                System.out.printf("%-10s%s%n" ,(timeAfterSwitch +"-" + currentTime), currentProcess.getProcessID());
                currentTime += contextSwitchTime;
                timeAfterSwitch = currentTime;
                System.out.printf("%-10s%s%n" ,((currentTime-1) + "-" + currentTime), "CS");
                contextSwitchCount++; // Increment context switching counter
            }

            // Process execution
            shortest.setRemainingTime(shortest.getRemainingTime()-1);
            currentTime++;

            if (shortest.getRemainingTime() == 0) {
                // Process finished
                completed++;
                shortest.setCompletionTime(currentTime);
                shortest.setTurnaroundTime(shortest.getCompletionTime() - shortest.getArrivalTime());
                shortest.setWaitingTime(shortest.getTurnaroundTime() - shortest.getBurstTime()); //this is why I changes waiting time to double !!!!
                
                totalTurnaroundTime += shortest.getTurnaroundTime();
                totalWaitingTime += shortest.getWaitingTime();
                System.out.printf("%-10s%s%n" ,(timeAfterSwitch +"-" + currentTime), shortest.getProcessID());//print process when it ends
                if (completed == processes.size()){//if there are no more processes then I dont need to do a CS, break bye.
                    break;
                }

                //if there are processes still, i need to CS
                currentTime += contextSwitchTime; 
                timeAfterSwitch = currentTime;
                System.out.printf("%-10s%s%n" ,((currentTime-1) + "-" + currentTime), "CS");
                contextSwitchCount++;
                currentProcess = null;//because current was terminated
                continue;//i dont want next code to excute since shortest is the terminated process
            }

            currentProcess = shortest;
        }
    }
}
