import java.util.*;
public class demo {
    public static void main(String[] args) {
        //ProcessSchedulingNew p = new ProcessSchedulingNew();
        ProcessSchedulingIIX p = new ProcessSchedulingIIX();

        Scanner input = new Scanner(System.in);

        System.out.print("Enter the number of processes:");
        int numOfProcess = input.nextInt();

        System.out.print("(");
        for (int i = 1; i < numOfProcess; i++) {
            System.out.print("P" + i + ", ");
        }
        System.out.println("P" + numOfProcess + ")");

        System.out.println("Arrival times and burst times as follows:");

        //List<Process> pList = new ArrayList<>();

        for (int i = 1; i <= numOfProcess; i++) {
            String processID = "P" + i;
            System.out.print("Enter Arrival Time For " + processID + ": ");
            int arrivalTime = input.nextInt();

            System.out.print("Enter Burst Time For " + processID + ": ");
            int burstTime = input.nextInt(); 

            Process process = new Process(processID, arrivalTime, burstTime);
            //process.setProcessID(processID);

            p.addProcess(process);
        }

        p.run();
        
        /*Process p1 = new Process("P1", 0, 8);
        Process p2 = new Process("P2", 1, 4);
        Process p3 = new Process("P3", 2, 5);
        Process p4 = new Process("P4", 3, 5);

        pList.add(p1);
        pList.add(p2);
        pList.add(p3);
        pList.add(p4);*/

        //p.simulateProcess(pList);
    }
}