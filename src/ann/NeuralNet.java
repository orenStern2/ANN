package ann;

import java.util.ArrayList;

import ann.learn.Adaline;
import ann.learn.Backpropagation;
import ann.learn.LevenbergMarquardt;
import ann.learn.Perceptron;
import ann.learn.Training.ActivationFncENUM;
import ann.learn.Training.TrainingTypesENUM;
import som.Kohonen;


public class NeuralNet {

	private InputLayer inputLayer;
	private HiddenLayer hiddenLayer;
	private ArrayList<HiddenLayer> listOfHiddenLayer;
	private OutputLayer outputLayer;
	private int numberOfHiddenLayers;
	
	private double[][] trainSet;
        private double[][] validationSet;
	private double[] realOutputSet;
	private double[][] realMatrixOutputSet;
	private int maxEpochs;
	private double learningRate;
	private double targetError;
	private double trainingError;
	private double errorMean;
	
	private ArrayList<Double> listOfMSE = new ArrayList<Double>();
	private ActivationFncENUM activationFnc;
	private ActivationFncENUM activationFncOutputLayer;
	private TrainingTypesENUM trainType;
	
	
	public NeuralNet initNet(int numberOfInputNeurons, 
			int numberOfHiddenLayers,
			int numberOfNeuronsInHiddenLayer,
			int numberOfOutputNeurons){
		inputLayer = new InputLayer();
		inputLayer.setNumberOfNeuronsInLayer( numberOfInputNeurons );
		
		listOfHiddenLayer = new ArrayList<HiddenLayer>();
		for (int i = 0; i < numberOfHiddenLayers; i++) {
			hiddenLayer = new HiddenLayer();
			hiddenLayer.setNumberOfNeuronsInLayer( numberOfNeuronsInHiddenLayer );
			listOfHiddenLayer.add( hiddenLayer );
		}
		
		outputLayer = new OutputLayer();
		outputLayer.setNumberOfNeuronsInLayer( numberOfOutputNeurons );
		
		inputLayer = inputLayer.initLayer(inputLayer);
		
		if(numberOfHiddenLayers > 0) {
			listOfHiddenLayer = hiddenLayer.initLayer(hiddenLayer, listOfHiddenLayer, inputLayer, outputLayer);
		}

		outputLayer = outputLayer.initLayer(outputLayer);
		
		NeuralNet newNet = new NeuralNet();
		newNet.setInputLayer(inputLayer);
		newNet.setHiddenLayer(hiddenLayer);
		newNet.setListOfHiddenLayer(listOfHiddenLayer);
		newNet.setNumberOfHiddenLayers(numberOfHiddenLayers);
		newNet.setOutputLayer(outputLayer);
	
		return newNet;
	}
	
	public void printNet(NeuralNet n){
		inputLayer.printLayer(n.getInputLayer());
		System.out.println();
		if(n.getHiddenLayer() != null){
			hiddenLayer.printLayer(n.getListOfHiddenLayer());
			System.out.println();
		}
		outputLayer.printLayer(n.getOutputLayer());
	}
	
	public NeuralNet trainNet(NeuralNet n){

		NeuralNet trainedNet = new NeuralNet();
		
		switch (n.trainType) {
		case PERCEPTRON:
			Perceptron t = new Perceptron();
			trainedNet = t.train(n);
			return trainedNet;
		case ADALINE:
			Adaline a = new Adaline();
			trainedNet = a.train(n);
			return trainedNet;
		case BACKPROPAGATION:
			Backpropagation b = new Backpropagation();
			trainedNet = b.train(n);
			return trainedNet;
                case  KOHONEN:
                    Kohonen k = new Kohonen();
                    trainedNet = k.train(n);
                    return trainedNet;
                case LEVENBERG_MARQUARDT:
                        LevenbergMarquardt mq = new LevenbergMarquardt();
                        trainedNet = mq.train(n);
                        return trainedNet;
		default:
			throw new IllegalArgumentException(n.trainType+" does not exist in TrainingTypesENUM");
		}
		
	}
	
	public void printTrainedNetResult(NeuralNet n) {
		switch (n.trainType) {
		case PERCEPTRON:
			Perceptron t = new Perceptron();
			t.printTrainedNetResult( n );
			break;
		case ADALINE:
			Adaline a = new Adaline();
			a.printTrainedNetResult( n );
			break;
		case BACKPROPAGATION:
			Backpropagation b = new Backpropagation();
			b.printTrainedNetResult( n );
			break;
		default:
			throw new IllegalArgumentException(n.trainType+" does not exist in TrainingTypesENUM");
		}
	}
        
        public double[][] getNetOutputValues(NeuralNet trainedNet){
            
            int rows = trainedNet.getTrainSet().length;
            int cols = trainedNet.getOutputLayer().getNumberOfNeuronsInLayer();
         double[][] matrixOutputValues = new double[rows][cols];
         
         switch(trainedNet.trainType){
             case BACKPROPAGATION:
                 Backpropagation b = new Backpropagation();
                 
                 for(int rows_i = 0 ; rows_i < rows ; rows_i++){
                     for (int cols_i = 0 ; cols_i < cols; cols_i++){
                         
                         matrixOutputValues[rows_i][cols_i] = b.forward(trainedNet, rows_i).getOutputLayer().getListOfNeurons().get(cols_i).getOutputValue();
                     }
                 }
                 
                 break;
             default:
                 throw new IllegalArgumentException(trainedNet.trainType + " does not exist in TrainingTypsENUM");
         }
         
         return matrixOutputValues;
            
        }
	
        public void netValidation(NeuralNet n){
            
            switch (n.trainType){
            case KOHONEN:
                Kohonen k = new Kohonen();
                k.netValidation(n);
                break;
                        default:
                            throw new IllegalArgumentException(n.trainType+" does not exist in TrainingTypesENUM");
        }
            
      }
        
	public InputLayer getInputLayer() {
		return inputLayer;
	}

	public void setInputLayer(InputLayer inputLayer) {
		this.inputLayer = inputLayer;
	}

	public HiddenLayer getHiddenLayer() {
		return hiddenLayer;
	}

	public void setHiddenLayer(HiddenLayer hiddenLayer) {
		this.hiddenLayer = hiddenLayer;
	}

	public ArrayList<HiddenLayer> getListOfHiddenLayer() {
		return listOfHiddenLayer;
	}

	public void setListOfHiddenLayer(ArrayList<HiddenLayer> listOfHiddenLayer) {
		this.listOfHiddenLayer = listOfHiddenLayer;
	}

	public OutputLayer getOutputLayer() {
		return outputLayer;
	}

	public void setOutputLayer(OutputLayer outputLayer) {
		this.outputLayer = outputLayer;
	}

	public int getNumberOfHiddenLayers() {
		return numberOfHiddenLayers;
	}

	public void setNumberOfHiddenLayers(int numberOfHiddenLayers) {
		this.numberOfHiddenLayers = numberOfHiddenLayers;
	}

	public double[][] getTrainSet() {
		return trainSet;
	}

	public void setTrainSet(double[][] trainSet) {
		this.trainSet = trainSet;
	}

	public double[] getRealOutputSet() {
		return realOutputSet;
	}

	public void setRealOutputSet(double[] realOutputSet) {
		this.realOutputSet = realOutputSet;
	}

	public int getMaxEpochs() {
		return maxEpochs;
	}

	public void setMaxEpochs(int maxEpochs) {
		this.maxEpochs = maxEpochs;
	}

	public double getTargetError() {
		return targetError;
	}

	public void setTargetError(double targetError) {
		this.targetError = targetError;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getTrainingError() {
		return trainingError;
	}

	public void setTrainingError(double trainingError) {
		this.trainingError = trainingError;
	}

	public ActivationFncENUM getActivationFnc() {
		return activationFnc;
	}

	public void setActivationFnc(ActivationFncENUM activationFnc) {
		this.activationFnc = activationFnc;
	}

	public TrainingTypesENUM getTrainType() {
		return trainType;
	}

	public void setTrainType(TrainingTypesENUM trainType) {
		this.trainType = trainType;
	}

	public ArrayList<Double> getListOfMSE() {
		return listOfMSE;
	}

	public void setListOfMSE(ArrayList<Double> listOfMSE) {
		this.listOfMSE = listOfMSE;
	}

	public double[][] getRealMatrixOutputSet() {
		return realMatrixOutputSet;
	}

	public void setRealMatrixOutputSet(double[][] realMatrixOutputSet) {
		this.realMatrixOutputSet = realMatrixOutputSet;
	}

	public double getErrorMean() {
		return errorMean;
	}

	public void setErrorMean(double errorMean) {
		this.errorMean = errorMean;
	}

	public ActivationFncENUM getActivationFncOutputLayer() {
		return activationFncOutputLayer;
	}

	public void setActivationFncOutputLayer(
			ActivationFncENUM activationFncOutputLayer) {
		this.activationFncOutputLayer = activationFncOutputLayer;
	}
        
        public double[][] getValidationSet(){
            return validationSet;
        }
        
        public void setValidationSet(double[][] validationSet){
            this.validationSet = validationSet;
        }
	
}
