public class Belt {
    private int length;
    private final int criticalTime;
    private boolean running = true;
    private boolean inCritical = false;
    
    private int countA1 = 0;
    private int countA2 = 0;
    private int countB = 0;

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
        sensorBTime = criticalTime + 2; // reset timer
        sensorB = false;
        countB++;
    }

    public void tick(){
        if (sensorBTime > 0){
            sensorBTime--;
        }

        if ((countB == countA1) && (sensorBTime == 0)){
            inCritical = false; // Kein Teil im Bereich B, Teil lange nicht durch SensorB
        } else if ((sensorBTime <= criticalTime) && !(countB == countA1)){
            inCritical = true; // Teil im krit Bereich, Belt schaltet nicht mehr ab
        } else if ((countB == countA1) && (sensorBTime > criticalTime)){
            inCritical = false; // Teil hat krit Bereich verlassen, neues Teil noch nicht in krit Bereich
        }

        boolean sensorA = !((countA1 - countA2) == 0);
        if (inCritical && sensorA){
            sensorB = true;
        } else {
            sensorB = false;
        }

        running = sensorA || sensorB;
    }
}
