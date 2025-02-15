public class Process{
    private int processID;
    private int burstTime;
    private double turnaroundTime;
    private int arrivalTime;
    private double waitingTime; //I changed to double 
    private int remainingTime;
    private int completionTime;

    public Process(int processID,  int arrivalTime, int burstTime)
    {
        this.processID=processID;
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
        this.remainingTime=burstTime;
    }

    public int getProcessID()
    {
        return processID;
    }

    public int getCompletionTime()
    {
        return completionTime;
    }

    public int getBurstTime()
    {
        return burstTime;
    }

    public double getTurnaroundTime()
    {
        return turnaroundTime;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }

    public double getWaitingTime()
    {
        return waitingTime;
    }

    public int getRemainingTime()
    {
        return remainingTime;
    }

    public void setBurstTime(int burstTime)
    {
        this.burstTime=burstTime;
    }

    public void setWaitingTime(double waitingTime)
    {
        this.waitingTime=waitingTime;
    }

    public void setRemainingTime(int remainingTime)
    {
        this.remainingTime=remainingTime;
    }

    public void setCompletionTime(int completionTime)
    {
        this.completionTime=completionTime;
    }

    public void setTurnaroundTime(int turnaroundTime)
    {
        this.turnaroundTime=turnaroundTime;
    }

 }