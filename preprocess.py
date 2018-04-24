import pandas as pd
import datetime as dt
import datetime
import numpy as np

df = pd.read_csv('usdjpy.csv')
df.Date = df.Date.astype(str)
df['Date'] = df['Date'].apply(lambda x: dt.datetime.strptime(x,'%Y%m%d'))

df = df[df['Date'] >= datetime.date(2010,4,1)]
df = df.set_index('Date')

price = df['Price'] 
#print(price)

window_size = 5
price_threshold = 0.5

n = int(len(price) / window_size)

xs = []
ys = []
ys_sign = np.full(n, 1, dtype=int)

for i in range(n):
    fr = i * window_size
    to = fr + window_size
    x = list(price[fr:to].values)
    xs.append(x)
    
    y = np.mean(x)
    ys.append(y)
       
#print(xs)
#print(ys)

for i in range(1, n):
    if ys[i] - ys[i - 1] > price_threshold:
        ys_sign[i] = 2
    elif ys[i - 1] - ys[i] > price_threshold:
        ys_sign[i] = 0
                
ys_sign = ys_sign.tolist()        
print(ys_sign)

df = pd.DataFrame(list(price.values), columns=['x'])
df.to_csv("intermediate/x.csv", index=False)

df = pd.DataFrame(ys_sign, columns=['y'])
df.to_csv("intermediate/y.csv", index=False)















