// Conveyor belt with automatic shutdown based on sensors

public class Belt {
    private int length;
    private int critical = 5;
    private boolean running = false;
    private boolean zoneB = false;
    private boolean set = false;
    
    // Sensor counters
    private int countA1 = 0; // Parts entering Belt
    private int countA2 = 0; // Parts leaving Belt
    private int countB = 0;  // Parts passing SensorB before Belt

    private boolean sensorA = false;
    private boolean sensorB = false;
    private int sensorBTimer = 0;

    public Belt(int length){
        this.length = length;
    }

    public int getLength(){
        return length;
    }

    public int getCritical(){
        return critical;
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
        sensorBTimer = critical + 1; // reset timer
        sensorB = false;
        countB++;

        if (running){
            set = true; // remember that belt was running when part entered zoneB
        }
    }

    public void tick(){
        if (sensorBTimer > 0){
            sensorBTimer--;
        }

        sensorA = !(countA1 == countA2); // Sensor A true if part(s) on Belt

        // check if part has entered zone B, but not left
        if (countB > countA1)
            zoneB = true;
        else {
            zoneB = false;
            set = false;
        }

        // set sensorB if belt was running when part entered zoneB
        if (zoneB && set){
            sensorB = true;
        } else {
            sensorB = false;
        }

        if ((sensorBTimer == 0) && !(zoneB)){
            sensorB = false;
        }

        running = sensorA || sensorB;
    }
 
    public int getCountA1(){ return countA1; }
    public int getCountA2(){ return countA2; }
    public int getCountB(){ return countB; }
    public int getSensorBTime(){return sensorBTimer; }
    public boolean getSensorB(){ return sensorB; }
    public boolean getInCritical(){ return zoneB; }
    public boolean getSet(){ return set; }
}
