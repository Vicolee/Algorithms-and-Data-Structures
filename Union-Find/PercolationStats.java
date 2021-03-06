import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] sampleThreshold;
    private final int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int t) {
        trials = t;
        int gridLength = n;
        sampleThreshold = new double[trials];
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("Invalid values.");
        for (int trial = 0; trial < trials; trial++) {
            Percolation monteCarlo = new Percolation(gridLength);
            while (!monteCarlo.percolates()) {
                int row = StdRandom.uniform(1, gridLength + 1);
                int col = StdRandom.uniform(1, gridLength + 1);
                monteCarlo.open(row, col);
            }
            double dn = gridLength;
            sampleThreshold[trial] = monteCarlo.numberOfOpenSites() / (dn * dn);
        }
    }

    public double mean() {
        return StdStats.mean(sampleThreshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(sampleThreshold);
    }

    // returns the lower bound of the 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
    }

    // returns the higher bound of the 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        int trials = StdIn.readInt();
        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.printf("mean = %f\n", stats.mean());
        StdOut.printf("stddev = %f\n", stats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", stats.confidenceLo(), stats.confidenceHi());
    }
}

