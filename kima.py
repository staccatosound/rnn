# Importing the libraries
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM

def train():
    training_set = pd.read_csv('data/TSLA_Train.csv')
    training_set = training_set.iloc[:,1:2]
    training_set = training_set.values
    training_set = training_set[1:]


    sc = MinMaxScaler()
    training_set = sc.fit_transform(training_set)    
    len_train = len(training_set)
    
    X_train = training_set[0:len_train - 4]
    y_train = training_set[4:len_train]
    
#    today = pd.DataFrame(X_train[0:5])
#    tomorrow = pd.DataFrame(y_train[0:5])
#    ex = pd.concat([today, tomorrow], axis=1)
#    ex.columns = (['today', 'tomorrow'])
    
    X_train = np.reshape(X_train, ( int((len_train - 1) / 4), 1, 4))
    y_train = np.reshape(y_train, ( int((len_train - 1) / 4), 1, 4))
    y_train = [np.sum(a) for a in y_train]
    regressor = Sequential()
    regressor.add(LSTM(units = 4, activation = 'sigmoid', input_shape = (None, 4)))
    regressor.add(Dense(units = 1))
    regressor.compile(optimizer = 'adam', loss = 'mean_squared_error')
    regressor.fit(X_train, y_train, batch_size = 32, epochs = 200)


def test():
    test_set = pd.read_csv('data/TSLA_Test.csv')
    test_set = test_set[1:33]
    real_stock_price = test_set.iloc[:,1:2]
    real_stock_price = real_stock_price.values
    len_test = len(real_stock_price)
    
    # Getting the predicted stock price of 2017
    X_test = real_stock_price[0:len_test - 4]
    y_test = training_set[4:len_test]
    
#    X_test = sc.transform(X_test)
    X_test = np.reshape(X_test, (int((len_test - 1) / 4), 1, 4))
    y_test = np.reshape(y_test, ( int((len_test - 1) / 4), 1, 4))
    y_test = [np.sum(a) for a in y_test]

    print(X_test)
    y_predict = regressor.predict(X_test)
#    y_predict = sc.inverse_transform(y_predict)
#    y_predict = np.insert(y_predict, 0, 200)
    
    #for i in range(0, n):
    #    print(real_stock_price[i], predicted_stock_price[i], real_stock_price[i + 1])    
    
    # Visualizing the results
    plt.figure(figsize=(10,10),  dpi=100)
    plt.plot(real_stock_price, color = 'red', label = 'Real Price')
    plt.plot(predicted_stock_price, color = 'blue', label = 'Predicted Price')
    plt.xlabel('Days')
    plt.ylabel('Price')
    plt.show()

def main():    
    train()
    test()


if __name__ == '__main__':
    main()