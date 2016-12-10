def is_triangle(s1, s2, s3):
    return ((s1 + s2) > s3) and ((s1 + s3) > s2) and ((s2 + s3) > s1)

f = open("input_3")
count = 0
for line in f:
    num = [int(i) for i in line.split()]
    if ((num[0] + num[1]) > num[2]) and ((num[0] + num[2]) > num[1]) and ((num[1] + num[2]) > num[0]):
        count += 1

print(count)