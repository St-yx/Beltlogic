// Conveyor belt with automatic shutdown based in sensors

public class Belt {
    private int length;
    private final int criticalTime;
    private boolean running = true;
    private boolean inCritical = false;
    
    // Sensor counters
    private int countA1 = 0; // Parts entering Belt
    private int countA2 = 0; // Parts leaving Belt
    private int countB = 0; // Parts passing SensorB before Belt

    private boolean sensorB = false;
    private int sensorBTime = 0;

    public Belt(int length, int criticalTime){
        this.length = length;
        this.criticalTime = criticalTime;
    }

    public int getLength(){
        return length;
    }

    public boolean isRunning(){
        return running;
    }

    public void hitA1(){
        countA1++;
    }

    public void hitA2(){
        countA2++;
    }

    public void triggerB(){
        sensorBTime = criticalTime + 2; // reset timer + safety buffer
        sensorB = false;
        countB++;
    }

    public void tick(){
        if (sensorBTime > 0){
            sensorBTime--;
        }

        // Determine if part in critical zone before Belt
        if ((countB == countA1) && (sensorBTime == 0)){
            inCritical = false; // No part in zone B, timer expired
        } else if ((sensorBTime <= criticalTime) && !(countB == countA1)){
            inCritical = true; // Part in critical zone
        } else if ((countB == countA1) && (sensorBTime > criticalTime)){
            inCritical = false; // Part left critical zone, new part didn't pass B yet
        }


        boolean sensorA = !((countA1 - countA2) == 0); // Sensor A true if part(s) on Belt

        // Decide if SensorB should trigger belt stop
        if (inCritical && sensorA){
            sensorB = true;
        } else {
            sensorB = false;
        }

        running = sensorA || sensorB;
    }
 
    public int getCountA1(){ return countA1; }
    public int getCountA2(){ return countA2; }
    public int getCountB(){ return countB; }
    public int getSensorBTime(){return sensorBTime; }
    public boolean getSensorB(){ return sensorB; }
    public boolean getInCritical(){ return inCritical; }
}
