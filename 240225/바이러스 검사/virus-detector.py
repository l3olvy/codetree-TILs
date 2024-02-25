import sys

input = sys.stdin.readline

# 식당 수 입력받기
n = int(input())
# 각 식당에 있는 고객의 수 리스트로 입력받기
shop = list(map(int, input().split()))
# 팀장과 팀원이 각각 검사할 수 있는 고객의 수 입력받기
manager, member = map(int, input().split())

# 최종 검사자 수 구할 변수
count = 0

for i in shop:
    # 팀장만으로 안 되는 경우
    if i > manager:
        i -= manager
        # 팀장이 맡을 수 있는 검사자 수 제외 후 몇 명의 팀원이 필요한지 계산
        if i % member == 0:
            count += (i // member) + 1
        else:
            count += (i // member) + 2
    # 팀장만으로 되는 경우
    else:
        count += 1

print(count)