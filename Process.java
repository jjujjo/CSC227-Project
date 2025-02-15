public class Process{
    private int processID;
    private int burstTime;
    private int turnaroundTime;
    private int arrivalTime;
    private int waitingTime;
    private int executionTime;

    public Process(int processID,  int arrivalTime, int burstTime)
    {
        this.processID=processID;
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
    }

    public int getProcessID()
    {
        return processID;
    }

    public int getBurstTime()
    {
        return burstTime;
    }

    public int getTurnaroundTime()
    {
        return turnaroundTime;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }

    public int getWaitingTime()
    {
        return waitingTime;
    }

    public void setBurstTime(int burstTime)
    {
        this.burstTime=burstTime;
    }

    public void setWaitingTime(int waitingTime)
    {
        this.waitingTime=waitingTime;
    }

    public void updateProcess()
    {
        //update attributes 
    }
}