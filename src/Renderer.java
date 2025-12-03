// Renderer generating visualization for entire production line in terminal

import java.util.*;

public class Renderer {
    public static String renderTwoLine(List<Segment> segments, List<Part> parts){
        int totalUnits = 0;
        for (Segment s : segments) {
            totalUnits += s.getUnits();
        }

        StringBuilder lineChars = new StringBuilder();
        int[] unitCharIdx = new int[totalUnits];
        int unitIdx = 0;

        for (int si = 0; si < segments.size(); si++) {
            Segment s = segments.get(si);
            if (s instanceof BeltSegment){
                int L = s.getUnits();
                for (int u = 0; u < L; u++) {
                    unitCharIdx[unitIdx] = lineChars.length();
                    lineChars.append('-');
                    unitIdx++;
                }
            } else if (s instanceof MachineSegment){
                unitCharIdx[unitIdx] = lineChars.length();
                lineChars.append('|');
                unitIdx++;
            }

            if (si < segments.size() - 1){
                lineChars.append('/');
            }
        }

        char[] secondLine = new char[lineChars.length()];
        for (int i = 0; i < secondLine.length; i++) {
            secondLine[i] = ' ';
        }

        for (Part p : parts){
            int pos = p.getPos();
            if (pos >= 0 && pos < totalUnits){
                int charIdx = unitCharIdx[pos];
                secondLine[charIdx] = '*';
            }
        }

        return lineChars.toString() + "\n" + new String(secondLine) + " ";
    }
}
