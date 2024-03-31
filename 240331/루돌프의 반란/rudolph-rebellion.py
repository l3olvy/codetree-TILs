import math
INF = int(1e9)
# 게임판의 크기 N, 게임 턴 수 M, 산타의 수 P, 루돌프의 힘 C, 산타의 힘 D 입력 받기
N, M, P, C, D = map(int, input().split())
# 루돌프의 초기 위치 R_r, R_c 입력 받기
R_r, R_c = map(int, input().split())
rudolf = (R_r, R_c)
# 산타 위치 입력 받기
santa = [[] for _ in range(P + 1)]
# 루돌프와 산타의 거리
distance = [INF] * (P + 1)
# 산타 점수
score = [0] * (P + 1)
# 산타 기절
faint = [0] * (P + 1)
# 게임 오버 산타
die = []

# 산타 : P_n
for i in range(P):
    P_n, S_r, S_c = map(int, input().split())
    santa[P_n] = (S_r, S_c)

def get_shortest_index(rudolf, santa):
    global distance
    distance = [INF] * (P + 1)
    for i in range(1, P + 1):
        if i not in die:
            distance[i] = int(math.pow(rudolf[0] - santa[i][0], 2) + math.pow(rudolf[1] - santa[i][1], 2))
    min_value = min(distance)
    min_indexs = [i for i in range(1, P + 1) if distance[i] == min_value]
    # 루돌프와 가장 거리가 가까운 산타가 한 명일 때
    if len(min_indexs) == 1:
        return santa[min_indexs[0]]
    # 여러 명일 때
    else:
        rc = [santa[i] for i in range(1, P + 1) if i in min_indexs]
        rc = list(zip(rc, min_indexs))
        rc = sorted(rc, key = lambda x : x[0], reverse=True)
        return rc[0][0]

def living_santa(die):
    for i in range(1, P + 1):
        if i not in die:
            score[i] += 1
def santa_next_location(rudolf, s_r, s_c, i):
    global D
    global score
    global die
    global faint
    # 상, 우, 하, 좌
    dr = [-1, 0, 1, 0]
    dc = [0, 1, 0, -1]
    r_r, r_c = rudolf
    r = 0
    c = 0
    min_value = math.pow(r_r - s_r, 2) + math.pow(r_c - s_c, 2)
    index = 4
    for j in range(4):
        n_r = s_r + dr[j]
        n_c = s_c + dc[j]
        # 게임 판 밖으로는 이동 불가
        if n_r <= 0 or n_c <= 0 or n_r > N or n_c > N:
            continue
        # 산타가 있는 칸이어도 이동 불가
        if (n_r, n_c) in santa:
            continue
        new_value = math.pow(r_r - n_r, 2) + math.pow(r_c - n_c, 2)
        if min_value > new_value:
            min_value = new_value
            index = j

    if index < 4:
        s_r += dr[index]
        s_c += dc[index]
    else:
        return s_r, s_c

    # 산타랑 루돌프랑 충돌했다면
    if s_r == r_r and s_c == r_c:
        score[i] += D
        s_r += dr[index - 2] * D
        s_c += dc[index - 2] * D

        # 산타 기절
        faint[i] = 2

        # 산타가 밖으로 튕겨져 나갔다면
        if s_r <= 0 or s_c <= 0 or s_r > N or s_c > N:
            die.append(i)
            return s_r, s_c
        # 밀린 자리에 다른 산타가 있다면
        r = s_r
        c = s_c
        si = []
        while (r, c) in santa:
            ind = santa.index((r, c))
            if ind == i:
                break
            si.append(ind)
            r += dr[index - 2]
            c += dc[index - 2]


        for j in si:
            santa[j] = santa[j][0] + dr[index - 2], santa[j][1] + dc[index - 2]
            r, c = santa[j]
            if r <= 0 or c <= 0 or r > N or c > N:
                if j not in die:
                    die.append(j)

    return s_r, s_c

def move_santa(santa, rudolf):
    global faint
    for i in range(1, P + 1):
        if i not in die and faint[i] == 0:
            s_r, s_c = santa[i]
            santa[i] = santa_next_location(rudolf, s_r, s_c, i)

def rudolf_next_location(rudolf, s_r, s_c):
    global C
    # 상, 우상, 우, 우하, 하, 좌하, 좌, 좌상
    dr = [-1, -1, 0, 1, 1, 1, 0, -1]
    dc = [0, 1, 1, 1, 0, -1, -1, -1]
    r_r, r_c = rudolf
    index = 0
    # 산타가 루돌프보다 아래에 있을 때
    if s_r > r_r:
        # 좌하
        if s_c < r_c:
            index = 5
        # 하
        elif s_c == r_c:
            index = 4
        # 우하
        else:
            index = 3
    # 같은 행일 때
    elif s_r == r_r:
        # 좌
        if s_c < r_c:
            index = 6
        # 우
        else:
            index = 2
    # 위에 있을 때
    else:
        # 좌상
        if s_c < r_c:
            index = 7
        # 상
        elif s_c == r_c:
            index = 0
        # 우상
        else:
            index = 1

    r_r += dr[index]
    r_c += dc[index]

    for i in range(1, P + 1):
        s_r, s_c = santa[i]
        if s_r == r_r and s_c == r_c:
            score[i] += C
            s_r += dr[index] * C
            s_c += dc[index] * C

            # 산타 기절
            faint[i] = 2

            # 산타가 밖으로 튕겨져 나갔다면
            if s_r <= 0 or s_c <= 0 or s_r > N or s_c > N:
                if i not in die:
                    die.append(i)
            # 밀린 자리에 다른 산타가 있다면
            r, c = s_r, s_c
            si = []
            while (r, c) in santa:
                ind = santa.index((r, c))
                if ind == i:
                    break
                si.append(ind)
                r += dr[index]
                c += dc[index]

            for j in si:
                santa[j] = santa[j][0] + dr[index], santa[j][1] + dc[index]
                r, c = santa[j]
                if r <= 0 or c <= 0 or r > N or c > N:
                    if j not in die:
                        die.append(j)
                    continue

            santa[i] = s_r, s_c
            break

    return r_r, r_c

def move_rudolf():
    global rudolf
    # 루돌프와 가장 가까운 산타 좌표 구하기
    s_r, s_c = get_shortest_index(rudolf, santa)
    # 루돌프의 다음 좌표 구하기
    rudolf = rudolf_next_location(rudolf, s_r, s_c)

def remove_faint(faint):
    for i in range(1, P + 1):
        if faint[i] > 0:
            faint[i] -= 1

# 게임 진행
def play_game():
    global santa
    global rudolf
    global die
    move_rudolf()
    move_santa(santa, rudolf)
    living_santa(die)
    remove_faint(faint)

for i in range(M):
    play_game()
    if len(die) == P:
        break


for i in range(1, P + 1):
    print(score[i], end=' ')