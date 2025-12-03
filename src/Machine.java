// Machine holds parts for specific cycle time

class Machine {
    private int cycleTime;
    private int remainTime = 0;

    public Machine(int cycleTime){
        this.cycleTime = cycleTime;
    }

    public void start(){
        remainTime = cycleTime;
    }

    public void tick(){
        if (remainTime > 0){
            remainTime--;
        }
    }

    public boolean isBusy(){
        return remainTime > 0;
    }

    public int getRemainTime(){
        return remainTime;
    } 
}
