import java.util.*;
public class demo {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("Enter the number of processes:");
        int numOfProcess = input.nextInt();

        List<Process> pList = new ArrayList<>();

        for (int i = 1; i <= numOfProcess; i++) {
            System.out.println("Enter Process ID:");
            String processID = input.next();

            System.out.println("Enter Arrival Time:");
            int arrivalTime = input.nextInt();

            System.out.println("Enter Burst Time:");
            int burstTime = input.nextInt(); 

            Process process = new Process(processID, arrivalTime, burstTime);
            pList.add(process);
        }

        ProcessScheduling p = new ProcessScheduling();
        
        /*Process p1 = new Process("P1", 0, 8);
        Process p2 = new Process("P2", 1, 4);
        Process p3 = new Process("P3", 2, 5);
        Process p4 = new Process("P4", 3, 5);

        pList.add(p1);
        pList.add(p2);
        pList.add(p3);
        pList.add(p4);*/

        p.simulateProcess(pList);
    }
}