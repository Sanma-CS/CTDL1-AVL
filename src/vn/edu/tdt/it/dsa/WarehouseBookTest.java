package vn.edu.tdt.it.dsa;

import static org.junit.Assert.*;

/**
 * Created by walpurisnacht on 04/04/2017.
 */
public class WarehouseBookTest {

    WarehouseBook wb;

    @org.junit.Before
    public void setUp() throws Exception {
        wb = new WarehouseBook();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        assertTrue(isBST());
    }

    @org.junit.Test
    public void handle01() throws Exception {
        for (int i = 0; i < 2000; i++) {
            wb.handle01("1"+String.format("%03d", i%1000)+"1");
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(2, wb.search(i).getRecord().getQuantity());
        }
    }

    @org.junit.Test
    public void handle02() throws Exception {
        for (int i = 0; i < 2000; i++) {
            wb.handle01("1"+String.format("%03d", i%1000)+"1");
        }
        for (int i = 0; i < 1000; i++) {
            wb.handle02("2"+String.format("%03d", i)+"1");
            assertEquals(1, wb.search(i).getRecord().getQuantity());
        }
        for (int i = 0; i < 1000; i++) {
            wb.handle02("20019");
        }
        assertNull(wb.getRoot());
    }

    @org.junit.Test
    public void handle03() throws Exception {
        for (int i = 0; i < 1000; i++) {
            wb.handle01("1"+String.format("%03d", (int) (Math.random() * 1000))+"1");
        }
//        WarehouseBook wb_parent;
        for (int i = 0; i < 1000; i++) {
//            wb_parent = new WarehouseBook(wb.toString());
            wb.handle03();
//            assertTrue(wb_parent.toString() + "===SPLIT===" + wb.toString(), isBalanced());
            assertTrue(isBalanced());
        }
    }

    @org.junit.Test
    public void handle04() throws Exception {
        for (int i = 0; i < 1000; i++) {
            wb.handle01("1"+String.format("%03d", (int) (Math.random() * 1000))+"1");
        }
//        WarehouseBook wb_parent;
        for (int i = 0; i < 1000; i++) {
//            wb_parent = new WarehouseBook(wb.toString());
            wb.handle03();
//            assertTrue(wb_parent.toString() + "===SPLIT===" + wb.toString(), isBalanced());
            assertTrue(isBalanced());
        }
    }

    @org.junit.Test
    public void handle05() throws Exception {
        for (int i = 0; i < 2000; i++) {
            wb.handle01("1"+String.format("%03d", i%1000)+"1");
        }
        WarehouseBook wb_parent;
        for (int i = 0; i < 1000; i++) {
            wb.handle05("5"+String.format("%03d", i)+"1");
            assertEquals(i, wb.getRoot().getRecord().getProductID());
        }
    }

    @org.junit.Test
    public void handle06() throws Exception {
        wb.handle06("60");
        assertNull(wb.getRoot());
    }

    private boolean isBST() {
        return isBSTUtil(wb.getRoot(), 0, 999);
    }

    private boolean isBSTUtil(WarehouseBook.WarehouseNode node, int min, int max) {
        if (node == null) return true;

        if (node.getRecord().getProductID() < min || node.getRecord().getProductID() > max) return false;

        return (isBSTUtil(node.getLeft(), min, node.getRecord().getProductID()-1) &&
                isBSTUtil(node.getRight(), node.getRecord().getProductID()+1, max));
    }

    private boolean isBalanced() {
        return isBalancedUtil(wb.getRoot());
    }

    private boolean isBalancedUtil(WarehouseBook.WarehouseNode node) {
        int lh, rh;

        if (node == null) return true;

        lh = getHeight(node.getLeft());
        rh = getHeight(node.getRight());

        if (Math.abs(lh - rh) <= 1
                && isBalancedUtil(node.getLeft())
                && isBalancedUtil(node.getRight()))
            return true;

        return false;
    }

    private int getHeight(WarehouseBook.WarehouseNode node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
    }
}