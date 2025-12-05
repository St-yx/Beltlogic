// Conveyor manages entire production line: segments, parts movement rules, line component linking

import java.util.*;

public class Conveyor {
    private List<Segment> segments;
    private List<Part> parts = new ArrayList<>();
    private int totalUnits;
    private boolean set;

    public Conveyor(List<Segment> segments){
        this.segments = segments;

        // Calculate and assign start positions for all segments in line
        int currentUnit = 0;
        for (Segment s : segments) {
            if (s instanceof BeltSegment){
                ((BeltSegment) s).setStartUnit(currentUnit);
            } else if (s instanceof MachineSegment){
                ((MachineSegment) s).setStartUnit(currentUnit);
            }
            currentUnit += s.getUnits();
        }
        this.totalUnits = currentUnit;
    }

    public void addPart(Part p){
        parts.add(p);
    }

    public void tick(){
        // Trigger SensorB for all belts when parts reach trigger position
        // Trigger position = belt start - (belt length + 2 units buffer)
        for (Segment s : segments){
            if (s instanceof BeltSegment) {
                BeltSegment bs = (BeltSegment) s;
                Belt belt = bs.getBelt();
                int beltStart = bs.getStartUnit();
                int triggerDist = belt.getLength() + 2;
                int triggerPos = beltStart - triggerDist;

                for (Part p : parts) {
                    if (p.getPos() == triggerPos){
                        belt.triggerB();
                        break;
                    }
                }
            }
        }

        // Tick machines and belts
        for (Segment s : segments){
            if (s instanceof BeltSegment){
                ((BeltSegment) s).getBelt().tick();
            } else if (s instanceof MachineSegment){
                ((MachineSegment) s).getMachine().tick();
            }
        }

        // Collision avoiding by sorting backwards
        parts.sort((a, b) -> Integer.compare(b.getPos(), a.getPos()));

        // Track occupied positions
        Set<Integer> occupiedPos = new HashSet<>();
        for (Part p : parts){
            occupiedPos.add(p.getPos());
        }

        for (Part p : parts){
            int currentPos = p.getPos();
            int nextPos = p.getPos() + 1;

            boolean canMove = true;

            // Check 1: next position free?
            if (occupiedPos.contains(nextPos)){
                canMove = false;
            }

            // Check 2: part in busy machine?
            if (canMove) {
                for (Segment s : segments){
                    if (s instanceof MachineSegment){
                        MachineSegment ms = (MachineSegment) s;
                        if (currentPos == ms.getStartUnit() && ms.getMachine().isBusy()){
                            canMove = false;
                            break;
                        }
                    }
                }                
            }

            // Check 3: belt running?
            if (canMove && currentPos >= 0){
                for (Segment s : segments){
                    if (s instanceof BeltSegment){
                        BeltSegment bs = (BeltSegment) s;
                        int beltStart = bs.getStartUnit();
                        int beltEnd = beltStart + bs.getBelt().getLength() -1;

                        if (currentPos >= beltStart && currentPos <= beltEnd){
                            if (!bs.getBelt().isRunning()){
                                canMove = false; // this should never happen
                            }
                            break;
                        }
                    }
                }
            }

            // execute movement, trigger sensors
            if (canMove){
                occupiedPos.remove(currentPos);
                p.move();
                occupiedPos.add(p.getPos());

                // Trigger Sensor A1 when entering belt, A2 when leaving
                for (Segment s : segments){
                    if (s instanceof BeltSegment){
                        BeltSegment bs = (BeltSegment) s;
                        int beltStart = bs.getStartUnit();
                        int beltEnd = beltStart + bs.getBelt().getLength() -1;

                        if (p.getPos() == beltStart - 1){
                            bs.getBelt().hitA1();
                            set = true;
                        }
                        if ((p.getPos() == beltStart) && !(set)){
                            bs.getBelt().hitA1();
                            set = false;
                        }
                        if (currentPos == beltEnd && p.getPos() == beltEnd + 1){
                            bs.getBelt().hitA2();
                        }
                    }
                }
            }
        }

        // start machines for newly arrived parts
        for (Segment s : segments){
            if (s instanceof MachineSegment){
                MachineSegment ms = (MachineSegment) s;
                Machine machine = ms.getMachine();
                int machinePos = ms.getStartUnit();

                for (Part p : parts){
                    if (p.getPos() == machinePos && !machine.isBusy()){
                        machine.start();
                        break;
                    }
                }
            }
        }

        // remove parts on last segment of line
        Iterator<Part> it = parts.iterator();
        while (it.hasNext()){
            Part p = it.next();
            if (p.getPos() >= totalUnits) {
                it.remove();
            }
        }
    }

    // Terminal output for state information visualization 
    public void printState(){
        System.out.println("\n===System Status===");

        int beltCount = 0;
        int machineCount = 0;
        for (int i = 0; i < segments.size(); i++) {
            Segment s = segments.get(i);
            if (s instanceof BeltSegment) {
                BeltSegment bs = (BeltSegment) s;
                Belt belt = bs.getBelt();
                boolean sensorA = !(belt.getCountA1() == belt.getCountA2());

                System.out.println("Segment " + i + " = Belt " + (beltCount++) + 
                    " | Running: " + bs.getBelt().isRunning() +
                    " | Start: " + bs.getStartUnit());
                System.out.println("  └─ SensorA: " + sensorA + 
                    " (A1=" + belt.getCountA1() + ", A2=" + belt.getCountA2() + ")" +
                    " | SensorB: " + belt.getSensorB() +
                    " (Timer=" + belt.getSensorBTime() + ", B=" + belt.getCountB() +
                    ", InCrit=" + belt.getInCritical() + ", set=" + belt.getSet() + ")");
                System.out.println();
            }else if (s instanceof MachineSegment){
                MachineSegment ms = (MachineSegment) s;
                System.out.println("Segment " + i + " = Machine" + (machineCount++) + 
                    " | Remaining: " + ms.getMachine().getRemainTime() +
                    " | Pos: " + ms.getStartUnit());
                System.out.println();
            }
        }
        System.out.print("Parts positions: ");
        for (Part p : parts){
            System.out.print(p.getPos() + " ");
        }
        System.out.println();

        System.out.print(Renderer.renderTwoLine(segments, parts));
    }
}