import pandas as pd
#import matplotlib.pyplot as plt
import datetime as dt
import datetime
import numpy as np

def bbands(price, length=30, numsd=2):
    ave = price.rolling(length).mean()
    sd = price.rolling(length).std()
    upband = ave + (sd*numsd)
    dnband = ave - (sd*numsd)
    return np.round(ave,3), np.round(upband,3), np.round(dnband,3)

df = pd.read_csv('usdjpy.csv')
df.Date = df.Date.astype(str)
df['Date'] = df['Date'].apply(lambda x: dt.datetime.strptime(x,'%Y%m%d'))

df = df[df['Date'] >= datetime.date(2017,4,1)]
df = df.set_index('Date')

df['SMA(5)'] = df['Price'].rolling(5).mean()
df['SMA(20)'] = df['Price'].rolling(20).mean()
df['SMA(30)'], df['BB-Upper'], df['BB-Lower'] = bbands(df['Price'])

df.plot(figsize=(20,10));


price = df['Price'] 
#print(price)