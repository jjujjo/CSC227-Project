import.java.util.*
public class demo{
    public static void main (String [] args){
        ProcessScheduling p = new ProcessScheduling();

        Process p1 = new Process("P1", 0, 8);
        Process p2 = new Process("P2", 1, 4);

        List<Process> pList = new List<Process>();
        pList.add(p1);
        pList.add(p2);



        p.simulateProcess(pList);
    }
}