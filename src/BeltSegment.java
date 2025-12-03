class BeltSegment extends Segment {
    private Belt belt;
    private int startUnit;

    public BeltSegment(Belt belt){
        this.belt = belt;
    }
    public Belt getBelt(){
        return belt;
    }

    public void setStartUnit(int startUnit){
        this.startUnit = startUnit;
    }

    public int getStartUnit(){
        return startUnit;
    }

    @Override 
    public int getUnits(){
        return belt.getLength();
    }
}
