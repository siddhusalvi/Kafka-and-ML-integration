#!C:\Users\Siddesh\AppData\Local\Programs\Python\Python38-32\python.exe
import numpy as np
import pandas as pd
from sklearn.linear_model import LinearRegression
import sys
import pickle
import os

a = sys.stdin.read()
if a == "" or a == " ":
    sys.exit()
data = []
data = a.split(',')
for each in range(0,len(data)):
    data[each] = float(data[each])
data = np.array(data).reshape(1,-1)
model_path = "C:\\Users\\Siddesh\\IdeaProjects\\KafkaServer\\src\main\\resourece\\output.pkl"
with open(model_path,'rb') as f:
    model = pickle.load(f)
pred = model.predict(data)
try:
   sys.stdout.write(str(pred[0]))
except Exception as e:
       print("Cannot return the path beacause ",e)