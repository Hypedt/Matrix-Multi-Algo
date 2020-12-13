import java.util.*;

public class matrix_Multi {

    /**
        This program runs continuously until it breaks or is stop manually.
        The unit of time being used is nanoseconds
     */
    public static void main(String[] args) {

        final int TIMES = 50; //The amount of times a matrix is running through a algorithm
        int n; // size of matrix
        long startTime, endTime;
        long totalTimeC = 0;
        long totalTimeDC = 0;
        long totalTimeS = 0;
        int[][] A, B; //1st matrix and 2nd matrix

        for (int i = 1; i > 0; i++) {
            n = (int) Math.pow(2, i); //Increase size of the matrix of 2^n

            //Creates two matrix of random ints
            A = generateMatrix(n);
            B = generateMatrix(n);

            //Start loop and end loop till J = 50 or TIMES
            for (int j = 0; j < TIMES; j++) {
                startTime = System.nanoTime(); //get the current time using nanoseconds
                classicMM(A, B, A.length); //Pass through these matrix and the size to the algorithm function
                endTime = System.nanoTime(); //Get the end time
                totalTimeC += endTime - startTime; //Calculate the time taken to complete the algorithm

                startTime = System.nanoTime();
                divideAndConquerMM(A, B, A.length);
                endTime = System.nanoTime();
                totalTimeDC += endTime - startTime;

                startTime = System.nanoTime();
                strassenMM(A, B, A.length);
                endTime = System.nanoTime();
                totalTimeS += endTime - startTime;
            }

            /*Once the loop finishes, calculate the time it takes to do the algorithm 1 time
            by dividing the total time with the amount of time the loop runs.
            */
            totalTimeC = totalTimeC / TIMES;
            totalTimeDC = totalTimeDC / TIMES;
            totalTimeS = totalTimeS / TIMES;

            //Prints out the result
            System.out.println("For n="+ n
                            + ": \n\tClassic Matrix Multiplication time: "
                            + totalTimeC
                            + " nanoseconds.\n\tDivide and Conquer Matrix Multiplication time: "
                            + totalTimeDC
                            + " nanoseconds.\n\tStrassen's Matrix Multiplication time: "
                            + totalTimeS + " nanoseconds.\n");
        }
    }

    /**
     * This method will generate a matrix of size n x n, filled with random
     * numbers between 0 and 100
     */
    public static int[][] generateMatrix(int n) {
        Random r = new Random();
        int[][] matrix = new int[n][n];

        //Populate the array with random int values
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = r.nextInt(100);
            }
        }
        return matrix;
    }

    /**
     * Will perform classic matrix multiplication using 3 nested for loops
     */
    public static int[][] classicMM(int[][] A, int[][] B, int n) {
        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = 0;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    /**
     *  Do Divide and Conquer recursively
     */
    public static int[][] divideAndConquerMM(int[][] A, int[][] B, int n) {
        int[][] C = new int[n][n];

        if (n == 1) {
            C[0][0] = A[0][0] * B[0][0];
            return C;
        } else {
            int[][] A11 = new int[n / 2][n / 2];
            int[][] A12 = new int[n / 2][n / 2];
            int[][] A21 = new int[n / 2][n / 2];
            int[][] A22 = new int[n / 2][n / 2];
            int[][] B11 = new int[n / 2][n / 2];
            int[][] B12 = new int[n / 2][n / 2];
            int[][] B21 = new int[n / 2][n / 2];
            int[][] B22 = new int[n / 2][n / 2];

            breakMatrix(A, A11, 0, 0);
            breakMatrix(A, A12, 0, n / 2);
            breakMatrix(A, A21, n / 2, 0);
            breakMatrix(A, A22, n / 2, n / 2);
            breakMatrix(B, B11, 0, 0);
            breakMatrix(B, B12, 0, n / 2);
            breakMatrix(B, B21, n / 2, 0);
            breakMatrix(B, B22, n / 2, n / 2);

            int[][] C11 = addMatrix(divideAndConquerMM(A11, B11, n / 2),
                    divideAndConquerMM(A12, B21, n / 2), n / 2);
            int[][] C12 = addMatrix(divideAndConquerMM(A11, B12, n / 2),
                    divideAndConquerMM(A12, B22, n / 2), n / 2);
            int[][] C21 = addMatrix(divideAndConquerMM(A21, B11, n / 2),
                    divideAndConquerMM(A22, B21, n / 2), n / 2);
            int[][] C22 = addMatrix(divideAndConquerMM(A21, B12, n / 2),
                    divideAndConquerMM(A22, B22, n / 2), n / 2);

            makeMatrix(C11, C, 0, 0);
            makeMatrix(C12, C, 0, n / 2);
            makeMatrix(C21, C, n / 2, 0);
            makeMatrix(C22, C, n / 2, n / 2);
        }

        return C;
    }

    /**
     * Use the strassenCalcMatrix method to multiply the two matrices
     */
    public static int[][] strassenMM(int[][] A, int[][] B, int n) {
        int[][] C = new int[n][n];
        strassenCalcMatrix(A, B, C, n);
        return C;
    }

    /**
     *  Create the 7 matrices using the Strassen's formula and calulcate and create the 4 new matrix C
     */
    public static void strassenCalcMatrix(int[][] A, int[][] B, int[][] C, int n) {

        if (n == 2) {
            C[0][0] = (A[0][0] * B[0][0]) + (A[0][1] * B[1][0]);
            C[0][1] = (A[0][0] * B[0][1]) + (A[0][1] * B[1][1]);
            C[1][0] = (A[1][0] * B[0][0]) + (A[1][1] * B[1][0]);
            C[1][1] = (A[1][0] * B[0][1]) + (A[1][1] * B[1][1]);
        } else {
            int[][] A11 = new int[n / 2][n / 2];
            int[][] A12 = new int[n / 2][n / 2];
            int[][] A21 = new int[n / 2][n / 2];
            int[][] A22 = new int[n / 2][n / 2];
            int[][] B11 = new int[n / 2][n / 2];
            int[][] B12 = new int[n / 2][n / 2];
            int[][] B21 = new int[n / 2][n / 2];
            int[][] B22 = new int[n / 2][n / 2];

            int[][] P = new int[n / 2][n / 2];
            int[][] Q = new int[n / 2][n / 2];
            int[][] R = new int[n / 2][n / 2];
            int[][] S = new int[n / 2][n / 2];
            int[][] T = new int[n / 2][n / 2];
            int[][] U = new int[n / 2][n / 2];
            int[][] V = new int[n / 2][n / 2];

            breakMatrix(A, A11, 0, 0);
            breakMatrix(A, A12, 0, n / 2);
            breakMatrix(A, A21, n / 2, 0);
            breakMatrix(A, A22, n / 2, n / 2);
            breakMatrix(B, B11, 0, 0);
            breakMatrix(B, B12, 0, n / 2);
            breakMatrix(B, B21, n / 2, 0);
            breakMatrix(B, B22, n / 2, n / 2);

            strassenCalcMatrix(addMatrix(A11, A22, n / 2),
                    addMatrix(B11, B22, n / 2), P, n / 2);
            strassenCalcMatrix(addMatrix(A21, A22, n / 2), B11, Q, n / 2);
            strassenCalcMatrix(A11, subtractMatrix(B12, B22, n / 2), R, n / 2);
            strassenCalcMatrix(A22, subtractMatrix(B21, B11, n / 2), S, n / 2);
            strassenCalcMatrix(addMatrix(A11, A12, n / 2), B22, T, n / 2);
            strassenCalcMatrix(subtractMatrix(A21, A11, n / 2),
                    addMatrix(B11, B12, n / 2), U, n / 2);
            strassenCalcMatrix(subtractMatrix(A12, A22, n / 2),
                    addMatrix(B21, B22, n / 2), V, n / 2);

            int[][] C11 = addMatrix(
                    subtractMatrix(addMatrix(P, S, P.length), T, T.length), V,
                    V.length);
            int[][] C12 = addMatrix(R, T, R.length);
            int[][] C21 = addMatrix(Q, S, Q.length);
            int[][] C22 = addMatrix(
                    subtractMatrix(addMatrix(P, R, P.length), Q, Q.length), U,
                    U.length);

            makeMatrix(C11, C, 0, 0);
            makeMatrix(C12, C, 0, n / 2);
            makeMatrix(C21, C, n / 2, 0);
            makeMatrix(C22, C, n / 2, n / 2);
        }
    }

    /**
     * Creates a new matrix based off of part of another matrix
     */
    private static void makeMatrix(int[][] initialMatrix,
                                        int[][] newMatrix, int a, int b) {

        int y = b;

        for (int i = 0; i < initialMatrix.length; i++) {
            for (int j = 0; j < initialMatrix.length; j++) {
                newMatrix[a][y++] = initialMatrix[i][j];
            }
            y = b;
            a++;
        }
    }

    /**
     * Adds two matrices together
     */
    private static int[][] addMatrix(int[][] A, int[][] B, int n) {

        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    /**
     * Subtracts two matrices
     */
    private static int[][] subtractMatrix(int[][] A, int[][] B, int n) {

        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    /**
     * Creates a new matrix based off of part of another matrix
     * Breaks down a larger matrix to smaller matrix
     */
    private static void breakMatrix(int[][] initialMatrix,
                                          int[][] newMatrix, int a, int b) {

        int y = b;
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                newMatrix[i][j] = initialMatrix[a][y++];
            }
            y = b;
            a++;
        }
    }

}
