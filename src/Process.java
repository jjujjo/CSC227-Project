public class Process{
    private String processID;
    private int burstTime;
    private double turnaroundTime;
    private int arrivalTime;
    private double waitingTime; //I changed to double 
    private int remainingTime;
    private int completionTime;
    private int startTime;

    public Process(int index, int arrivalTime, int burstTime)
    {
        this.processID="P"+index;
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
        this.remainingTime=burstTime;
    }

    public String getProcessID()
    {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
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

    public int getStartTime()
    {
        return startTime;
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

    public void setTurnaroundTime(double turnaroundTime)
    {
        this.turnaroundTime=turnaroundTime;
    }

    public void setStartTime(int startTime)
    {
        this.startTime=startTime;
    }

 }

 