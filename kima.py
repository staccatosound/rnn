# Importing the libraries
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd

# Reading CSV file into training set
training_set = pd.read_csv('data/TSLA_Train.csv')

# Getting relevant feature
training_set = training_set.iloc[:,1:2]

# Converting to 2D array
training_set = training_set.values

# Feature Scaling
from sklearn.preprocessing import MinMaxScaler
sc = MinMaxScaler()
training_set = sc.fit_transform(training_set)

# Getting the inputs and the ouputs
len_train = len(training_set)

X_train = training_set[0:len_train - 1]
y_train = training_set[1:len_train]

# Example
today = pd.DataFrame(X_train[0:5])
tomorrow = pd.DataFrame(y_train[0:5])
ex = pd.concat([today, tomorrow], axis=1)
ex.columns = (['today', 'tomorrow'])

# Reshaping into required shape for Keras
X_train = np.reshape(X_train, (len_train - 1, 1, 1))

# Importing the Keras libraries and packages
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM

# Initializing the Recurrent Neural Network
regressor = Sequential()

# Adding the input layer and the LSTM layer
regressor.add(LSTM(units = 4, activation = 'sigmoid', input_shape = (None, 1)))

# Adding the output layer
regressor.add(Dense(units = 1))

# Compiling the Recurrent Neural Network
regressor.compile(optimizer = 'adam', loss = 'mean_squared_error')

# Fitting the Recurrent Neural Network to the Training set
regressor.fit(X_train, y_train, batch_size = 32, epochs = 200)


###############################test############################################
test_set = pd.read_csv('data/TSLA_Test.csv')
test_set = test_set[:30]
real_stock_price = test_set.iloc[:,1:2]
real_stock_price = real_stock_price.values
len_test = len(real_stock_price)

n = 29
# Getting the predicted stock price of 2017
inputs = real_stock_price[:len_test]
inputs = sc.transform(inputs)
inputs = np.reshape(inputs, (len_test, 1, 1))
predicted_stock_price = regressor.predict(inputs)
predicted_stock_price = sc.inverse_transform(predicted_stock_price)
predicted_stock_price = np.insert(predicted_stock_price, 0, 200)

#for i in range(0, n):
#    print(real_stock_price[i], predicted_stock_price[i], real_stock_price[i + 1])    

# Visualizing the results
plt.figure(figsize=(10,10),  dpi=100)
plt.plot(real_stock_price, color = 'red', label = 'Real Price')
plt.plot(predicted_stock_price, color = 'blue', label = 'Predicted Price')
plt.xlabel('Days')
plt.ylabel('Price')
plt.show()

