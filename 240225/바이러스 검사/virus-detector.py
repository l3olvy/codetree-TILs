import sys

input = sys.stdin.readline

n = int(input())
shop = list(map(int, input().split()))
manager, member = map(int, input().split())

count = 0

for i in shop:
    if i > manager:
        i -= manager
        count = round(i // member) + 1
    else:
        count += 1

print(count)