package tr.com.beinplanner.packetsale.dao;

import java.util.Comparator;

public class PacketSaleComparator implements Comparator<PacketSaleFactory> {

	@Override
    public int compare(PacketSaleFactory ps1, PacketSaleFactory ps2) {
        return ps2.getSalesDate().compareTo(ps1.getSalesDate());
    }
}
