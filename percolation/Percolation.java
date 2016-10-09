import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF uf;
    private boolean[][] siteStatus;
    private int N; // N-by-N grid
    private int indexForTopVirtualSite, indexForBottomVirtualSite;

    /**
    create n-by-n grid, with all site blocked
    */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("grid size n must be greater than 0");
        }
        // we add two virtual site, one at top connected to every site on 1st row
        // the other at bottom connected to every site on last row
        // virtual sites at (1, 0) and (n+1, 1)
        uf = new WeightedQuickUnionUF(n * n + 2);
        siteStatus = new boolean[n + 1][n + 1];
        N = n;
        indexForTopVirtualSite = 0;
        indexForBottomVirtualSite = n * n + 1;
    }

    public void open(int i, int j) {
        checkIndex(i, j);
        if (!isOpen(i, j)) {
            siteStatus[i][j] = true;
            int indexForThisSite = xyTo1D(i, j);
            if (i == 1) {
                uf.union(indexForTopVirtualSite, indexForThisSite);
            }
            if (i == N) {
                uf.union(indexForBottomVirtualSite, indexForThisSite);
            }
            if (i - 1 >= 1 && isOpen(i - 1, j)) {
                uf.union(xyTo1D(i - 1, j), indexForThisSite);
            }
            if (i + 1 <= N && isOpen(i + 1, j)) {
                uf.union(xyTo1D(i + 1, j), indexForThisSite);
            }
            if (j - 1 >= 1 && isOpen(i, j - 1)) {
                uf.union(xyTo1D(i, j - 1), indexForThisSite);
            }
            if (j + 1 <= N && isOpen(i, j + 1)) {
                uf.union(xyTo1D(i, j + 1), indexForThisSite);
            }
        }
    }

    public boolean percolates() {
        return uf.connected(indexForTopVirtualSite, indexForBottomVirtualSite);
    }

    public boolean isOpen(int i, int j) {
        return siteStatus[i][j];
    }

    public boolean isFull(int i, int j) {
        return uf.connected(indexForTopVirtualSite, xyTo1D(i, j));
    }

    private int xyTo1D(int x, int y) {
        return (x-1) * N + y;
    }

    private void checkIndex(int i, int j) {
        if (i < 1 || i > N) {
            throw new IndexOutOfBoundsException("row index must between 1 and " + N);
        }
        if (j < 1 || j > N) {
            throw new IndexOutOfBoundsException("column index must between 1 and " + N);
        }
    }
}