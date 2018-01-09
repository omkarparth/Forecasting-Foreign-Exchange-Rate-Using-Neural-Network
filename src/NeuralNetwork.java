/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the edito.
 */
package exchangerateforecast;


import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;


/**
 *
 * @author sanju singh
 */
public class NeuralNetwork {
    static {
        Locale.setDefault(Locale.ENGLISH); //??
    }
 
    private boolean isTrained = false;
    final DecimalFormat df; //??
    final Random rand = new Random();
    final ArrayList<Neuron> inputLayer1 = new ArrayList<>();
    final ArrayList<Neuron> inputLayer2 = new ArrayList<>();
    final ArrayList<Neuron> inputLayer3 = new ArrayList<>();
    final ArrayList<Neuron> hiddenLayer11 = new ArrayList<>();
    final ArrayList<Neuron> hiddenLayer12 = new ArrayList<>();
    final ArrayList<Neuron> hiddenLayer13 = new ArrayList<>();
    final ArrayList<Neuron> hiddenLayer21 = new ArrayList<>();
    final ArrayList<Neuron> hiddenLayer22 = new ArrayList<>();
    final ArrayList<Neuron> hiddenLayer23 = new ArrayList<>();
    final ArrayList<Neuron> outputLayer = new ArrayList<>();
    final Neuron bias = new Neuron();
    final int[] layers;
    final int randomWeightMultiplier = 1; //??
 
    final double epsilon = 0.5; //??
 
     double learningRate = 0.35f; //??
     double momentum = 0.7f; //??
 
    // Inputs for xor problem
    final List<Double> inputs;
 
    // Corresponding outputs, xor training data
    final Double expectedOutputs[];
    double resultOutputs[][] = { { -1 } }; // dummy init
    double output[];
 
    // for weight update all
    final HashMap<String, Double> weightUpdate = new HashMap<>(); //??
    
    public NeuralNetwork(int input, int hidden1,int hidden2, int output, boolean isTrained) {
        this.layers = new int[] { input, hidden1,hidden2 };
        this.isTrained = isTrained;
        df = new DecimalFormat("#.0#"); //??
        inputs = new ArrayList<>();
        expectedOutputs = new Double[output];
        int i=0,j=0;
        /**
         * Create all neurons and connections Connections are created in the
         * neuron class
         */
        
        
                    // timestamp 1
                    for ( j = 0; j < layers[0]; j++) {
                        Neuron neuron = new Neuron();
                        inputLayer1.add(neuron);
                    } 
                    for ( j = 0; j < layers[1]; j++) {
                        Neuron neuron = new Neuron();
                        neuron.addInConnectionsS(inputLayer1);
                        neuron.addBiasConnection(bias);
                        hiddenLayer11.add(neuron);
                    }
                     for ( j = 0; j < layers[2]; j++) {
                        Neuron neuron = new Neuron();
                        neuron.addInConnectionsS(hiddenLayer11);
                        neuron.addBiasConnection(bias);
                        hiddenLayer21.add(neuron);
                    } 
                     
                     
                    //timestamp 2 
                    for ( j = 0; j < layers[0]; j++) {
                        Neuron neuron = new Neuron();
                        inputLayer2.add(neuron);
                    }
                    for ( j = 0; j < layers[1]; j++) {
                        Neuron neuron = new Neuron();
                        neuron.addInConnectionsS(inputLayer2);
                        neuron.addInConnectionsS(hiddenLayer21);
                        neuron.addBiasConnection(bias);
                        hiddenLayer12.add(neuron);
                    }
                    for ( j = 0; j < layers[2]; j++) {
                        Neuron neuron = new Neuron();
                        neuron.addInConnectionsS(hiddenLayer12);
                        neuron.addBiasConnection(bias);
                        hiddenLayer22.add(neuron);
                    }
                    
                    
                    //timestamp 3
                    for ( j = 0; j < layers[0]; j++) {
                        Neuron neuron = new Neuron();
                        inputLayer3.add(neuron);
                    }
                    for ( j = 0; j < layers[1]; j++) {
                        Neuron neuron = new Neuron();
                        neuron.addInConnectionsS(inputLayer3);
                        neuron.addInConnectionsS(hiddenLayer22);
                        neuron.addBiasConnection(bias);
                        hiddenLayer13.add(neuron);
                    } 
                    for ( j = 0; j < output; j++) {
                        Neuron neuron = new Neuron();
                        neuron.addInConnectionsS(hiddenLayer13);
                        neuron.addBiasConnection(bias);
                        outputLayer.add(neuron);
                    }   
            
        
 
        // initialize random weights
        for (Neuron neuron : hiddenLayer11) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : hiddenLayer12) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : hiddenLayer13) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : hiddenLayer21) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : hiddenLayer22) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : hiddenLayer23) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : outputLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
 
        // reset id counters
        Neuron.counter = 0;
        Connection.counter = 0;
 
        if (isTrained) {
            trainedWeights();
            updateAllWeights();
        }
    }
 
    // random
    final double getRandom() {
        return randomWeightMultiplier * (rand.nextDouble() * 2 - 1); // [-1;1[
    }
 
    /**
     * 
     * @param inputs
     *            There is equally many neurons in the input layer as there are
     *            in input variables
     */
    public void setInput(List<Double> inputs) {
        int i;
        for (i = 0; i < inputLayer1.size(); i++) {
            inputLayer1.get(i).setOutput(inputs.get(i));
            inputLayer2.get(i).setOutput(inputs.get(i));
            inputLayer3.get(i).setOutput(inputs.get(i));
        }
    }
 
    public double[] getOutput() {
        double[] outputs = new double[outputLayer.size()];
        int i;
        for ( i = 0; i < outputLayer.size(); i++)
            outputs[i] = outputLayer.get(i).getOutput();
        return outputs;
    }
 
    /**
     * Calculate the output of the neural network based on the input The forward
     * operation
     */
    public void activate() {
        for (Neuron n : hiddenLayer11)
            n.calculateOutput();
        for (Neuron n : hiddenLayer21)
            n.calculateOutput();
        for (Neuron n : hiddenLayer12)
            n.calculateOutput();
        for (Neuron n : hiddenLayer22)
            n.calculateOutput();
        for (Neuron n : hiddenLayer13)
            n.calculateOutput();
        for (Neuron n : outputLayer)
            n.calculateOutput();
    }
 
    /**
     * all output propagate back
     * 
     * @param expectedOutput
     *            first calculate the partial derivative of the error with
     *            respect to each of the weight leading into the output neurons
     *            bias is also updated here
     */
    public void applyBackpropagation(Double expectedOutput[]) {
 
        // error check, normalize value ]0;1[
    /*    for (int i = 0; i < expectedOutput.length; i++) {
            double d = expectedOutput[i];
            if (d < 0 || d > 1) {
                if (d < 0)
                    expectedOutput[i] = 0 + epsilon;
                else
                    expectedOutput[i] = 1 - epsilon;
            }
        }*/
        double ak,ai,desiredOutput,partialDerivative,deltaWeight,newWeight;
        double aj,sumKoutputs,wjk;
        int i = 0,j;
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                ak = n.getOutput();
                ai = con.leftNeuron.getOutput();
                desiredOutput = expectedOutput[i];
                
                // MAIN ALGORITHM
                partialDerivative = -ak * (1 - ak) * ai
                        * (desiredOutput - ak);
                deltaWeight = -learningRate * partialDerivative;
                newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
                // END
            }
            i++;
        }
 
        // update weights for the hidden layer
        for (Neuron n : hiddenLayer13) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                aj = n.getOutput();
                ai = con.leftNeuron.getOutput();
                sumKoutputs = 0;
                j = 0;
                for (Neuron out_neu : inputLayer3) {
                    wjk = out_neu.getConnection(n.id).getWeight();
                    desiredOutput = (double) expectedOutput[j];
                    ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
                for (Neuron hl2 : hiddenLayer22) {
                    wjk = hl2.getConnection(n.id).getWeight();
                    desiredOutput = (double) expectedOutput[j];
                    ak = hl2.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
                partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                deltaWeight = -learningRate * partialDerivative;
                newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
        for (Neuron n : hiddenLayer22) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                aj = n.getOutput();
                ai = con.leftNeuron.getOutput();
                sumKoutputs = 0;
                j = 0;
                for (Neuron out_neu : hiddenLayer12) {
                    wjk = out_neu.getConnection(n.id).getWeight();
                    desiredOutput = (double) expectedOutput[j];
                    ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
                partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                deltaWeight = -learningRate * partialDerivative;
                newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
        for (Neuron n : hiddenLayer12) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                aj = n.getOutput();
                ai = con.leftNeuron.getOutput();
                sumKoutputs = 0;
                j = 0;
                for (Neuron out_neu : inputLayer2) {
                    wjk = out_neu.getConnection(n.id).getWeight();
                    desiredOutput = (double) expectedOutput[j];
                    ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
                for (Neuron hl2 : hiddenLayer21) {
                    wjk = hl2.getConnection(n.id).getWeight();
                    desiredOutput = (double) expectedOutput[j];
                    ak = hl2.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
                partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                deltaWeight = -learningRate * partialDerivative;
                newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
        for (Neuron n : hiddenLayer21) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                aj = n.getOutput();
                ai = con.leftNeuron.getOutput();
                sumKoutputs = 0;
                j = 0;
                for (Neuron out_neu : hiddenLayer11) {
                    wjk = out_neu.getConnection(n.id).getWeight();
                    desiredOutput = (double) expectedOutput[j];
                    ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
                partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                deltaWeight = -learningRate * partialDerivative;
                newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
        for (Neuron n : hiddenLayer11) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                aj = n.getOutput();
                ai = con.leftNeuron.getOutput();
                sumKoutputs = 0;
                j = 0;
                for (Neuron out_neu : inputLayer1) {
                    wjk = out_neu.getConnection(n.id).getWeight();
                    desiredOutput = (double) expectedOutput[j];
                    ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
                partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                deltaWeight = -learningRate * partialDerivative;
                newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
    }
 
    void run(int maxSteps, double minError, String fileName, JLabel testLabel) {
        int i;
        // Train neural network until minError reached or maxSteps exceeded
        double error = 11;
        BufferedReader br = null;
        
        System.out.println("NN Foreign Exchange Rate Forecasting.");
int p;
        for (i = 0; i < maxSteps && error > minError; i++) {
            try {
                if(i>=500)
                {
                    momentum=0.1f;
                    learningRate=0.9f;
                }
                File file = new File(fileName);
                
                br = new BufferedReader(new FileReader(file));
                error = 0;
                
                String outputString = "";  
                testLabel.setText((i/maxSteps) * 100 + "%");
                for ( p = 0; readInputOutput(br); p++) {
                    
                    setInput(inputs);
                    activate();
                    
                    output = getOutput();
                    resultOutputs[p] = output;
                    
                    outputString += getOutputString()+"\n";
                    
                    // calculate error for every run.
                    for (int j = 0; j < expectedOutputs.length; j++) {
                        double err = Math.pow((output[j] * 100) - (expectedOutputs[j] * 100), 2);
                        error += err;
                    }
                    
                    
                    applyBackpropagation(expectedOutputs);
                }
                
                if((i+1) == maxSteps || error <= minError){
                    System.out.println(outputString);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
         
        System.out.println("Sum of squared errors = " + error);
        System.out.println("##### EPOCH " + i+"\n");
        
      //  if (i == maxSteps) {
        //    System.out.println("!Error training try again");
        //} else {
            printAllWeights();
            
            printWeightUpdate();
    }
    
    
    public Double testRun(List<Double> input){
        setInput(input);
        activate();
        return (getOutput()[0] * 100);
    }
    
    private boolean readInputOutput(BufferedReader br){
        String line;
       
        
        try {  
            int i;
            if(inputs.isEmpty()){               
            
                for ( i = 0; i < inputLayer1.size(); i++){
                    if((line = br.readLine()) != null) {
                        // use comma as separator
                        String[] cols = line.split(",");
                        inputs.add(Double.parseDouble(cols[1])/100);
                        //System.out.println("Coulmn 4= " + cols[4] + " , Column 5=" + cols[5]);
                    } else{
                        return false;                
                    }                
               }
           
            } else{
                //shift every input to left and add previous expected output to last
                //and read expected output from next row.
                inputs.remove(0);
                inputs.add(expectedOutputs[0]);                              
            }
            
            if((line = br.readLine()) != null){
                String[] cols = line.split(",");
                expectedOutputs[0] = Double.parseDouble(cols[1])/100;                          
            } else{
                return false;
            }    
            
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;       
    }
     
    private String getOutputString(){
        
        String outputStr = "EXPECTED: ";
        outputStr += expectedOutputs[0] * 100 + " ";
        outputStr += "ACTUAL: ";
        outputStr += resultOutputs[0][0] * 100 + " ";
              
        return outputStr;
    }
 
    String weightKey(int neuronId, int conId) {
        return "N" + neuronId + "_C" + conId;
    }
 
    /**
     * Take from hash table and put into all weights
     */
    public final void updateAllWeights() {
        double newWeight;
        // update weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
        // update weights for the hidden layer
        for (Neuron n : hiddenLayer13) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                 newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
        for (Neuron n : inputLayer3) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                 newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }for (Neuron n : hiddenLayer22) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                 newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }for (Neuron n : hiddenLayer12) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                 newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }for (Neuron n : inputLayer2) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                 newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }for (Neuron n : hiddenLayer21) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                 newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }for (Neuron n : hiddenLayer11) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                 newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }for (Neuron n : inputLayer1) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                 newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
    }
 
    // trained data
    final void trainedWeights() {
        weightUpdate.clear();
        String line;
        BufferedReader br = null;
        Integer n, c;
        Double w;
        File file = new File("outputWeights.csv");
        try {
            
             br = new BufferedReader(new FileReader(file));
             //for removing the input,hidden,output neurons count.
             br.readLine();
             
             while((line = br.readLine()) != null){
               String[] cols = line.split(",");
               n = Integer.parseInt(cols[0]);
               c = Integer.parseInt(cols[1]);
               w = Double.parseDouble(cols[2]);
               
               weightUpdate.put(weightKey(n, c), w);
           } 
            
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
    public void printWeightUpdate(){
        
        PrintWriter printWriter = null;
        try {
            File file = new File("outputWeights.csv");
            file.createNewFile();
            printWriter = new PrintWriter(file);
            printWriter.write("" + layers[0] + "," + layers[1] + "," + layers[2]  + "," + output+ "\n");             
            
            System.out.println("printWeightUpdate, put this i trainedWeights() and set isTrained to true");
            // weights for the hidden layer
            for (Neuron n : hiddenLayer11) {
                ArrayList<Connection> connections = n.getAllInConnections();
                for (Connection con : connections) {
                    String w = df.format(con.getWeight());
                    printWriter.write(""+ n.id + "," + con.id + "," + w +"\n");
                    printWriter.flush();
                    System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                            + con.id + "), " + w + ");");
                }
            }
            for (Neuron n : hiddenLayer21) {
                ArrayList<Connection> connections = n.getAllInConnections();
                for (Connection con : connections) {
                    String w = df.format(con.getWeight());
                    printWriter.write(""+ n.id + "," + con.id + "," + w +"\n");
                    printWriter.flush();
                    System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                            + con.id + "), " + w + ");");
                }
            }
            for (Neuron n : hiddenLayer12) {
                ArrayList<Connection> connections = n.getAllInConnections();
                for (Connection con : connections) {
                    String w = df.format(con.getWeight());
                    printWriter.write(""+ n.id + "," + con.id + "," + w +"\n");
                    printWriter.flush();
                    System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                            + con.id + "), " + w + ");");
                }
            }
            for (Neuron n : hiddenLayer22) {
                ArrayList<Connection> connections = n.getAllInConnections();
                for (Connection con : connections) {
                    String w = df.format(con.getWeight());
                    printWriter.write(""+ n.id + "," + con.id + "," + w +"\n");
                    printWriter.flush();
                    System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                            + con.id + "), " + w + ");");
                }
            }
            for (Neuron n : hiddenLayer13) {
                ArrayList<Connection> connections = n.getAllInConnections();
                for (Connection con : connections) {
                    String w = df.format(con.getWeight());
                    printWriter.write(""+ n.id + "," + con.id + "," + w +"\n");
                    printWriter.flush();
                    System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                            + con.id + "), " + w + ");");
                }
            }
            // weights for the output layer
            for (Neuron n : outputLayer) {
                ArrayList<Connection> connections = n.getAllInConnections();
                for (Connection con : connections) {
                    String w = df.format(con.getWeight());
                    printWriter.write(""+ n.id + "," + con.id + "," + w +"\n");
                    printWriter.flush();
                    System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                            + con.id + "), " + w + ");");
                }
            }
            System.out.println();
        } catch (IOException ex) {
            Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try{
                printWriter.close();
            }catch(Exception e){}
        }
        
    }
 
    public void printAllWeights() {
        System.out.println("printAllWeights");
        // weights for the hidden layer
        for (Neuron n : hiddenLayer11) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        for (Neuron n : hiddenLayer21) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        for (Neuron n : hiddenLayer12) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        for (Neuron n : hiddenLayer22) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        for (Neuron n : hiddenLayer13) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        // weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        System.out.println();
    }
}
   
