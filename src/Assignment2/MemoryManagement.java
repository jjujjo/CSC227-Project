package Assignment2;

import java.util.*;

public class MemoryManagement {
    LinkedList<Block> memory;

    public MemoryManagement(LinkedList<Block> memory) {
        this.memory = memory;
    }

    public void allocateProcess(String processID, int size, int strategy) {
        switch (strategy) {
            case 1:
                for (int i = 0; i < memory.size(); i++) {
                    Block b = memory.get(i);
                    if (b.getIsAllocated() == false && size <= b.getSize()) {
                        b.setProcessID(processID);
                        b.setIsAllocated(true);
                        b.setInternalFragmentation(b.getSize() - size);
                        System.out.println(b.getProcessID() + "allocated at address: " + b.getStartAddress()
                                + ", and the internal fragmentation is: " + b.getInternalFragmentation());
                        break;
                    }
                }
                break;

            case 2:
                int min = -1;
                for (int i = 0; i < memory.size(); i++) {
                    Block b = memory.get(i);
                    if (b.getIsAllocated() == false && size <= b.getSize()) {
                        min = i;
                        break;
                    }
                }
                for (int i = min; i < memory.size(); i++) {
                    Block b = memory.get(i);
                    if (b.getIsAllocated() == false && size <= b.getSize() && b.getSize() < min) {
                        min = i;
                    }
                }
                if (min != -1) {
                    Block b = memory.get(min);
                    b.setProcessID(processID);
                    b.setIsAllocated(true);
                    b.setInternalFragmentation(b.getSize() - size);
                    System.out.println(b.getProcessID() + "allocated at address: " + b.getStartAddress()
                            + ", and the internal fragmentation is: " + b.getInternalFragmentation());
                }
                break;
            case 3:
                int max = -1;
                for (int i = 0; i < memory.size(); i++) {
                    Block b = memory.get(i);
                    if (b.getIsAllocated() == false && size <= b.getSize()) {
                        max = i;
                        break;
                    }
                }
                for (int i = max; i < memory.size(); i++) {
                    Block b = memory.get(i);
                    if (b.getIsAllocated() == false && size <= b.getSize() && b.getSize() > max) {
                        max = i;
                    }
                }
                if (max != -1) {
                    Block b = memory.get(max);
                    b.setProcessID(processID);
                    b.setIsAllocated(true);
                    b.setInternalFragmentation(b.getSize() - size);
                    System.out.println(b.getProcessID() + "allocated at address: " + b.getStartAddress()
                            + ", and the internal fragmentation is: " + b.getInternalFragmentation());
                }
                break;

            default:
                System.out.println("Invalid strategy");
                break;
        }
    }

    public void deAllocateProcess(String processID) {
        for (int i = 0; i < memory.size(); i++) {
            Block b = memory.get(i);
            if (b.getIsAllocated() && processID.equalsIgnoreCase(b.getProcessID())) {
                b.setProcessID(null);
                b.setIsAllocated(false);
                b.setInternalFragmentation(0);
                System.out.println(
                        "processID: " + b.getProcessID() + " is deallocated from address " + b.getStartAddress());
                break;
            }
        }
    }

public void printMemoryReport() {

    System.out.println("===========================================================");
    System.out.println("Block#\tSize\tStart-End\tStatus\tProcessID\tFragmentation");
    System.out.println("===========================================================");

    for (int i = 0; i < memory.size(); i++) {

        Block block = memory.get(i);
        System.out.printf("%d\t%d\t%d-%d\t%s\t%s\t%d\n",

                i, block.getSize(), block.getStartAddress(), block.getEndAddress(),

                (block.getIsAllocated() ? "Allocated" : "Free"),

                block.getProcessID(), block.getInternalFragmentation());
    }
    System.out.println("===========================================================");
}
}
