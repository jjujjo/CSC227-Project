import java.util.*;
public class demo {
    public static void main(String[] args) {
        ProcessSchedulingNew p = new ProcessSchedulingNew();
        
        Process p1 = new Process("P1", 0, 8);
        Process p2 = new Process("P2", 1, 4);
        Process p3 = new Process("P3", 2, 5);
        Process p4 = new Process("P4", 3, 5);

        List<Process> pList = new ArrayList<>();
        pList.add(p1);
        pList.add(p2);
        pList.add(p3);
        pList.add(p4);

        p.simulateProcess(pList);
    }
}