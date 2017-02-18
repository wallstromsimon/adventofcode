#!/usr/bin/python

f = open("input_3")
count = 0
num = []
for line in f:
    num.append([int(i) for i in line.split()])

for i in range(0, len(num), 3):
    for j in range(0, 3):
        if ((num[i][j] + num[i +1][j]) > num[i + 2][j]) and ((num[i][j] + num[i + 2][j]) > num[i + 1][j]) and ((num[i + 1][j] + num[i + 2][j]) > num[i][j]):
            count += 1

print(count)
