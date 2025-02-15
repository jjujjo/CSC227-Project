import java.util.*;
public class Scheduling {
    Process[] processes;
    
    public Scheduling(Process[] p)
    {
        processes=p;
    }

    public void SFJ()
    {
        int current=0; 
        Queue<Process> readyQueue=new LinkedList<Process>(); //??
        PriorityQueue<Process> orderedByBurstTime = new PriorityQueue<>(Comparator.comparingInt(p -> p.getArrivalTime()));

        for(int i=0; i<processes.length;i++)
        {
            Process p=processes[i];
            if(p.getArrivalTime()==current)
            {
                orderedByBurstTime.add(p);
                for(int j=0; j<processes.length-i; j++){
                    if(p.getArrivalTime()!=processes[j].getArrivalTime())
                    {
                        break;
                    }
                    orderedByBurstTime.add(processes[j]); //all have the same arrival time
                }

            }
            current++;
            orderedByBurstTime=performProcess(orderedByBurstTime);

        }

        while(orderedByBurstTime.size()!=0)
        {
            orderedByBurstTime=performProcess(orderedByBurstTime);
            current++;

        }
    }

    public PriorityQueue<Process> performProcess(PriorityQueue<Process> orderedByBurstTime)
    {
        PriorityQueue<Process> PQ=orderedByBurstTime;
        
        for(int i=0; i<orderedByBurstTime.size(); i++)
        {
            if(i==0)
            {
                Process p1=PQ.remove();
                p1.setBurstTime(p1.getBurstTime()-1);
                if(p1.getBurstTime()!=0)
                {
                    PQ.add(p1);
                }
            }
            else
            {
                Process p2=PQ.remove();
                p2.setWaitingTime(p2.getWaitingTime()+1);
                PQ.add(p2);
            }

        }
        return PQ;

    }
}
