public class MachineSegment extends Segment{
    private Machine machine;
    private int startUnit;

    public MachineSegment(Machine machine){
        this.machine = machine;
    }
    public Machine getMachine(){
        return machine;
    }

    public void setStartUnit(int startUnit){
        this.startUnit = startUnit;
    }

    public int getStartUnit(){
        return startUnit;
    }

    @Override 
    public int getUnits(){
        return 1;
    }
}
