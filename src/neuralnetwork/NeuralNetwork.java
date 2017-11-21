package neuralnetwork;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class NeuralNetwork {

//    private DoubleMatrix layer0 = new DoubleMatrix(new double[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
//    private DoubleMatrix layer1 = new DoubleMatrix(new double[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
//    private DoubleMatrix layer2 = new DoubleMatrix(new double[][] { { 0 }, { 0 }, { 0 }, { 0 } });
    private DoubleMatrix layer0 = new DoubleMatrix(new double[][] { { 0, 0, 0 } });
    private DoubleMatrix layer1 = null;
    private DoubleMatrix layer2 = null;

    public NeuralNetwork() {
    }

    /**
     * Apply sigmoid function element wise and return result in a new matrix.
     */
    static DoubleMatrix sigmoidi(DoubleMatrix input) {
	DoubleMatrix copy = new DoubleMatrix().copy(input);
	DoubleMatrix ones = DoubleMatrix.ones(copy.rows, copy.columns);
	copy.muli(-1);
	MatrixFunctions.expi(copy);
	copy.addi(1);
	return ones.divi(copy);
    }

    public DoubleMatrix calculate() {
	layer1 = sigmoidi(layer0);
	layer2 = sigmoidi(layer1);
	return layer2;
    }

}