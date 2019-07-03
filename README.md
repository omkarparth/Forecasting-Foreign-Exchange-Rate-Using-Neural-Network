# Forecasting-Foreign-Exchange-Rate-Using-Neural-Network
Designed the system architecture and developed a program using Java, Neural Network, and Backpropagation Algorithm for ‘Forecasting Foreign Exchange Rate Using Neural Network’. The system forecasts exchange rates of US Dollar, Euro, Pound and Yen against Indian Rupee.


README


Forecasting Foreign Exchange Rate Using Neural Network is a software for developed in JavaTM for performing foriegn exchange rate forecasts using feedforward and recurrent neural networks. It enables user to train the network with his/her own parameters, test the network and plot graphs.

Running the software (On Windows):
• Double click the file_name.
• java -Djava.library.path=./lib -jar ExchangeRateForecast.jar

Running the software (On Linux or Solaris OS or Mac OS X):
• java -Djava.library.path=./lib -jar ExchangeRateForecast.jar

To train the neural network:
1. Execute the software and on display of the home tab select the neural network you want to train.
2. In the forecast tab click on train NN button.
3. In the training dailog box select the currency, network parameters and provide the training data and start the training.
4. After complete training click on finish.

To test the neural network:
1. Train a neural network as per the above given steps.
2. On training a network and returning to the forecast tab, select the currency, provide the test data and click on forecast to perform the forecasting.
3. On forecasting the exchange rates, the forecasted values will be shown in the table and can also view on a graph by clicking on the plot graph button.


NOTE:

To run the project from the command line, go to the dist folder and
type the following:

java -jar "ExchangeRateForecast.jar" 

To distribute this project, zip up the dist folder (including the lib folder)
and distribute the ZIP file.
