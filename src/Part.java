// Defines workpieces moving through production line
public class Part {
    private int pos = -1; // start at Posistion -1 -> first tick to 0

    public void move(){
        pos++;
    }

    public int getPos(){
        return pos;
    }
}
