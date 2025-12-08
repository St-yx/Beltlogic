import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args){

        // Define belts and machines in order
        Belt b1 = new Belt(8,5);
        Machine m1 = new Machine(8);
        Belt b2 = new Belt(19,5);
        Belt b3 = new Belt(12,5);
        Belt b4 = new Belt(9,5);
        Machine m2 = new Machine(20);
        Belt b5 = new Belt(15,5);

        List<Segment> segments = new ArrayList<>();
        segments.add(new BeltSegment(b1));
        segments.add(new MachineSegment(m1));
        segments.add(new BeltSegment(b2));
        segments.add(new BeltSegment(b3));
        segments.add(new BeltSegment(b4));
        segments.add(new MachineSegment(m2));
        segments.add(new BeltSegment(b5));
            
        Conveyor conveyor = new Conveyor(segments);
            
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
        System.out.println("=== Line Simulation ===");
        System.out.println("ENTER generates new Part");
        System.out.println("Ctrl+C stops the Simulation\n");

        // Handling for Simulation controls
        try{
            for (int tick = 0; tick < 1000; tick++){
                System.out.println("\n>>> TICK: " + tick + " <<<");

                if (reader.ready()){
                    String line = reader.readLine();
                    if (line != null && line.isEmpty()){
                        System.out.println("[Main] New Part generated!");
                        conveyor.addPart(new Part());
                    }
                }

                conveyor.tick();
                conveyor.printState();

                try{
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); 
                    break;
                }
            }

        } catch (IOException e){
            System.out.println("Error reading the input: " + e.getMessage());
        }
    }
}