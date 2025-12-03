import java.util.*;

public class Conveyor {
    private List<Segment> segments;
    private List<Part> parts = new ArrayList<>();
    private int totalUnits;

    public Conveyor(List<Segment> segments){
        this.segments = segments;

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

        for (Segment s : segments){
            if (s instanceof BeltSegment){
                ((BeltSegment) s).getBelt().tick();
            } else if (s instanceof MachineSegment){
                ((MachineSegment) s).getMachine().tick();
            }
        }

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

        parts.sort((a, b) -> Integer.compare(b.getPos(), a.getPos()));

        Set<Integer> occupiedPos = new HashSet<>();
        for (Part p : parts){
            occupiedPos.add(p.getPos());
        }

        for (Part p : parts){
            int currentPos = p.getPos();
            int nextPos = p.getPos() + 1;

            boolean canMove = true;

            if (occupiedPos.contains(nextPos)){
                canMove = false;
            }

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

            if (canMove && currentPos >= 0){
                for (Segment s : segments){
                    if (s instanceof BeltSegment){
                        BeltSegment bs = (BeltSegment) s;
                        int beltStart = bs.getStartUnit();
                        int beltEnd = beltStart + bs.getBelt().getLength() -1;

                        if (currentPos >= beltStart && currentPos <= beltEnd){
                            if (!bs.getBelt().isRunning()){
                                canMove = false;
                            }
                            break;
                        }
                    }
                }
            }

            if (canMove){
                occupiedPos.remove(currentPos);
                p.move();
                occupiedPos.add(p.getPos());

                for (Segment s : segments){
                    if (s instanceof BeltSegment){
                        BeltSegment bs = (BeltSegment) s;
                        int beltStart = bs.getStartUnit();
                        int beltEnd = beltStart + bs.getBelt().getLength() -1;

                        if (p.getPos() == beltStart){
                            bs.getBelt().hitA1();
                        }
                        if (currentPos == beltEnd && p.getPos() == beltEnd + 1){
                            bs.getBelt().hitA2();
                        }
                    }
                }
            }
        }

        Iterator<Part> it = parts.iterator();
        while (it.hasNext()){
            Part p = it.next();
            if (p.getPos() >= totalUnits) {
                it.remove();
            }
        }
    }

    public void printState(){
        System.out.println("\n===System Status===");

        int beltCount = 0;
        int machineCount = 0;
        for (int i = 0; i < segments.size(); i++) {
            Segment s = segments.get(i);
            if (s instanceof BeltSegment) {
                BeltSegment bs = (BeltSegment) s;
                System.out.println("Segment " + i + " = Belt " + (beltCount++) + 
                    " | Running: " + bs.getBelt().isRunning() +
                    " | Start: " + bs.getStartUnit());
            }else if (s instanceof MachineSegment){
                MachineSegment ms = (MachineSegment) s;
                System.out.println("Segment " + i + " = Machine" + (machineCount++) + 
                    " | Remaining: " + ms.getMachine().getRemainTime() +
                    " | Pos: " + ms.getStartUnit());
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