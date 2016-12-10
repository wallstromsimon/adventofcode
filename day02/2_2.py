up = {1:1,
      2:2, 3:1, 4:4,
      5:5, 6:2, 7:3, 8:4, 9:9,
      'A':6, 'B':7, 'C':8,
      'D':'B'}

down = {1:3,
      2:6, 3:7, 4:8,
      5:5, 6:'A', 7:'B', 8:'C', 9:9,
      'A':'A', 'B':'D', 'C':'C',
      'D':'D'}

left = {1:1,
      2:2, 3:2, 4:3,
      5:5, 6:5, 7:6, 8:7, 9:8,
      'A':'A', 'B':'A', 'C':'B',
      'D':'D'}

right = {1:1,
      2:3, 3:4, 4:4,
      5:6, 6:7, 7:8, 8:9, 9:9,
      'A':'C', 'B':'C', 'C':'C',
      'D':'D'}


def find_digit(l, start):
    tmp = start
    for c in l:
        if c == 'U':
            tmp = up[tmp]
        if c == 'D':
            tmp = down[tmp]
        if c == 'L':
            tmp = left[tmp]
        if c == 'R':
            tmp = right[tmp]
    return tmp


f = open("input_2_1")
digit = 5
code = ""

for line in f:
    digit = find_digit(line, digit)
    code += str(digit)

print(code)
