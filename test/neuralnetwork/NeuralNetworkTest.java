package neuralnetwork;

import org.jblas.DoubleMatrix;
import org.testng.annotations.Test;

import spherepong.UnitTest;

public class NeuralNetworkTest extends UnitTest {

    @Test
    public void testCreateUninitializedNeuralNetwork() {
	NeuralNetwork network = new NeuralNetwork();
	DoubleMatrix input = new DoubleMatrix(new double[][] { { 0, 0, 1 }, { 0, 1, 1 }, { 1, 0, 1 }, { 1, 1, 1 } });
//	DoubleMatrix output = new DoubleMatrix(new double[][] { { 0 }, { 1 }, { 1 }, { 0 } });
	DoubleMatrix output = new DoubleMatrix(new double[][] { { 0 }, { 0 }, { 0 }, { 0 } });
	assertEquals(network.calculate(), output);
    }
    
    @Test
    public void testOneTrainingSample() {
	NeuralNetwork network = new NeuralNetwork();
	DoubleMatrix input = new DoubleMatrix(new double[][] { { 0, 0, 1 }, { 0, 1, 1 }, { 1, 0, 1 }, { 1, 1, 1 } });
//	DoubleMatrix output = new DoubleMatrix(new double[][] { { 0 }, { 1 }, { 1 }, { 0 } });
	DoubleMatrix output = new DoubleMatrix(new double[][] { { 0 }, { 0 }, { 0 }, { 0 } });
	assertEquals(network.calculate(), output);
    }

    @Test
    public void testSigmoidOnMatrix() {
	DoubleMatrix input = new DoubleMatrix(new double[][] { { 0, 1, 2 }, { 2, 1, 0 } });
	double sigmoidZero = 1 / (1 + Math.exp(-0));
	double sigmoidOne = 1 / (1 + Math.exp(-1));
	double sigmoidTwo = 1 / (1 + Math.exp(-2));

	DoubleMatrix expected = new DoubleMatrix(
		new double[][] { { sigmoidZero, sigmoidOne, sigmoidTwo }, { sigmoidTwo, sigmoidOne, sigmoidZero } });
	assertEquals(NeuralNetwork.sigmoidi(input), expected);
    }

}
